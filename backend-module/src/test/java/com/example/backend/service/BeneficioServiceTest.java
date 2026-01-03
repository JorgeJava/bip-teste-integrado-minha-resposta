package com.example.backend.service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferenciaDTO;
import com.example.backend.entity.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceTest {

    @Mock
    private BeneficioRepository repository;

    @InjectMocks
    private BeneficioService service;

    private Beneficio beneficio1;
    private Beneficio beneficio2;

    @BeforeEach
    void setUp() {
        beneficio1 = new Beneficio();
        beneficio1.setId(1L);
        beneficio1.setNome("Beneficio A");
        beneficio1.setValor(new BigDecimal("1000.00"));
        beneficio1.setAtivo(true);
        beneficio1.setVersion(0L);

        beneficio2 = new Beneficio();
        beneficio2.setId(2L);
        beneficio2.setNome("Beneficio B");
        beneficio2.setValor(new BigDecimal("500.00"));
        beneficio2.setAtivo(true);
        beneficio2.setVersion(0L);
    }

    @Test
    void testCreateBeneficio() {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Novo Beneficio");
        dto.setValor(new BigDecimal("2000.00"));
        dto.setAtivo(true);

        Beneficio saved = new Beneficio();
        saved.setId(3L);
        saved.setNome(dto.getNome());
        saved.setValor(dto.getValor());
        saved.setAtivo(dto.getAtivo());

        when(repository.save(any(Beneficio.class))).thenReturn(saved);

        BeneficioDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(dto.getNome(), result.getNome());
        verify(repository, times(1)).save(any(Beneficio.class));
    }

    @Test
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(beneficio1));

        BeneficioDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Beneficio A", result.getNome());
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void testTransferSuccess() {
        when(repository.findByIdWithLock(1L)).thenReturn(Optional.of(beneficio1));
        when(repository.findByIdWithLock(2L)).thenReturn(Optional.of(beneficio2));

        TransferenciaDTO transferencia = new TransferenciaDTO(1L, 2L, new BigDecimal("100.00"));

        service.transfer(transferencia);

        verify(repository, times(2)).save(any(Beneficio.class));
        assertEquals(new BigDecimal("900.00"), beneficio1.getValor());
        assertEquals(new BigDecimal("600.00"), beneficio2.getValor());
    }

    @Test
    void testTransferInsufficientBalance() {
        when(repository.findByIdWithLock(1L)).thenReturn(Optional.of(beneficio1));
        when(repository.findByIdWithLock(2L)).thenReturn(Optional.of(beneficio2));

        TransferenciaDTO transferencia = new TransferenciaDTO(1L, 2L, new BigDecimal("1500.00"));

        assertThrows(IllegalStateException.class, () -> service.transfer(transferencia));
    }

    @Test
    void testTransferSameBeneficio() {
        TransferenciaDTO transferencia = new TransferenciaDTO(1L, 1L, new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class, () -> service.transfer(transferencia));
    }

    @Test
    void testTransferNullAmount() {
        TransferenciaDTO transferencia = new TransferenciaDTO(1L, 2L, null);

        assertThrows(IllegalArgumentException.class, () -> service.transfer(transferencia));
    }
}

