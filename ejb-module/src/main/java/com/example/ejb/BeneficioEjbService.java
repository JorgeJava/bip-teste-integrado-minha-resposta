package com.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        // Validações iniciais
        if (fromId == null || toId == null || amount == null) {
            throw new IllegalArgumentException("IDs e valor não podem ser nulos");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser maior que zero");
        }
        
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Não é possível transferir para o mesmo benefício");
        }

        // Busca com locking pessimista para evitar condições de corrida
        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.PESSIMISTIC_WRITE);
        Beneficio to = em.find(Beneficio.class, toId, LockModeType.PESSIMISTIC_WRITE);

        if (from == null) {
            throw new IllegalArgumentException("Benefício de origem não encontrado: " + fromId);
        }
        
        if (to == null) {
            throw new IllegalArgumentException("Benefício de destino não encontrado: " + toId);
        }

        if (from.getAtivo() == null || !from.getAtivo()) {
            throw new IllegalStateException("Benefício de origem não está ativo");
        }
        
        if (to.getAtivo() == null || !to.getAtivo()) {
            throw new IllegalStateException("Benefício de destino não está ativo");
        }

        // Validação de saldo suficiente
        BigDecimal novoSaldo = from.getValor().subtract(amount);
        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException(
                String.format("Saldo insuficiente. Saldo atual: %s, Valor solicitado: %s", 
                    from.getValor(), amount)
            );
        }

        // Realiza a transferência
        from.setValor(novoSaldo);
        to.setValor(to.getValor().add(amount));

        // Atualiza as entidades (o lock garante consistência)
        em.merge(from);
        em.merge(to);
        
        // Se houver OptimisticLockException, será propagada para fazer rollback
    }
}
