package com.example.backend.service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferenciaDTO;
import com.example.backend.entity.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.OptimisticLockingFailureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeneficioService {

    @Autowired
    private BeneficioRepository repository;

    public List<BeneficioDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<BeneficioDTO> findAllAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BeneficioDTO findById(Long id) {
        Beneficio beneficio = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício não encontrado: " + id));
        return toDTO(beneficio);
    }

    @Transactional
    public BeneficioDTO create(BeneficioDTO dto) {
        Beneficio beneficio = toEntity(dto);
        beneficio = repository.save(beneficio);
        return toDTO(beneficio);
    }

    @Transactional
    public BeneficioDTO update(Long id, BeneficioDTO dto) {
        Beneficio beneficio = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício não encontrado: " + id));
        
        beneficio.setNome(dto.getNome());
        beneficio.setDescricao(dto.getDescricao());
        beneficio.setValor(dto.getValor());
        if (dto.getAtivo() != null) {
            beneficio.setAtivo(dto.getAtivo());
        }
        
        try {
            beneficio = repository.save(beneficio);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockException("Benefício foi modificado por outro usuário. Atualize e tente novamente.");
        }
        
        return toDTO(beneficio);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Benefício não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void transfer(TransferenciaDTO transferenciaDTO) {
        Long fromId = transferenciaDTO.getFromId();
        Long toId = transferenciaDTO.getToId();
        BigDecimal amount = transferenciaDTO.getAmount();

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
        Beneficio from = repository.findByIdWithLock(fromId)
                .orElseThrow(() -> new EntityNotFoundException("Benefício de origem não encontrado: " + fromId));
        
        Beneficio to = repository.findByIdWithLock(toId)
                .orElseThrow(() -> new EntityNotFoundException("Benefício de destino não encontrado: " + toId));

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

        // Salva as entidades (o lock garante consistência)
        try {
            repository.save(from);
            repository.save(to);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockException("Benefício foi modificado durante a transferência. Tente novamente.");
        }
    }

    private BeneficioDTO toDTO(Beneficio beneficio) {
        return new BeneficioDTO(
                beneficio.getId(),
                beneficio.getNome(),
                beneficio.getDescricao(),
                beneficio.getValor(),
                beneficio.getAtivo(),
                beneficio.getVersion()
        );
    }

    private Beneficio toEntity(BeneficioDTO dto) {
        Beneficio beneficio = new Beneficio();
        beneficio.setNome(dto.getNome());
        beneficio.setDescricao(dto.getDescricao());
        beneficio.setValor(dto.getValor());
        beneficio.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        return beneficio;
    }
}

