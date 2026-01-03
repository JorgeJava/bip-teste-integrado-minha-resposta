package com.example.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferenciaDTO {
    
    @NotNull(message = "ID do benefício de origem é obrigatório")
    private Long fromId;
    
    @NotNull(message = "ID do benefício de destino é obrigatório")
    private Long toId;
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    private BigDecimal amount;

    public TransferenciaDTO() {
    }

    public TransferenciaDTO(Long fromId, Long toId, BigDecimal amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    // Getters e Setters
    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

