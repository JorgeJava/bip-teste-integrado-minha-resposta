package com.example.backend.controller;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferenciaDTO;
import com.example.backend.service.BeneficioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(com.example.backend.BeneficioController.class)
class BeneficioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeneficioService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFindAll() throws Exception {
        BeneficioDTO dto1 = new BeneficioDTO(1L, "Beneficio A", "Desc A", new BigDecimal("1000.00"), true, 0L);
        BeneficioDTO dto2 = new BeneficioDTO(2L, "Beneficio B", "Desc B", new BigDecimal("500.00"), true, 0L);
        List<BeneficioDTO> beneficios = Arrays.asList(dto1, dto2);

        when(service.findAll()).thenReturn(beneficios);

        mockMvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Beneficio A"))
                .andExpect(jsonPath("$[1].nome").value("Beneficio B"));
    }

    @Test
    void testCreate() throws Exception {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Novo Beneficio");
        dto.setValor(new BigDecimal("2000.00"));
        dto.setAtivo(true);

        BeneficioDTO created = new BeneficioDTO(3L, dto.getNome(), null, dto.getValor(), dto.getAtivo(), 0L);
        when(service.create(any(BeneficioDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.nome").value("Novo Beneficio"));
    }

    @Test
    void testTransfer() throws Exception {
        TransferenciaDTO transferencia = new TransferenciaDTO(1L, 2L, new BigDecimal("100.00"));

        mockMvc.perform(post("/api/v1/beneficios/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferencia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transferência realizada com sucesso"));
    }
}

