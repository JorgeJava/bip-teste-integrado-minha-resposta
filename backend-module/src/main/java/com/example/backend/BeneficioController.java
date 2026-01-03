package com.example.backend;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferenciaDTO;
import com.example.backend.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/beneficios")
@Tag(name = "Benefícios", description = "API para gerenciamento de benefícios")
@CrossOrigin(origins = "*")
public class BeneficioController {

    @Autowired
    private BeneficioService service;

    @GetMapping
    @Operation(summary = "Listar todos os benefícios")
    public ResponseEntity<List<BeneficioDTO>> findAll() {
        List<BeneficioDTO> beneficios = service.findAll();
        return ResponseEntity.ok(beneficios);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar benefícios ativos")
    public ResponseEntity<List<BeneficioDTO>> findAllAtivos() {
        List<BeneficioDTO> beneficios = service.findAllAtivos();
        return ResponseEntity.ok(beneficios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar benefício por ID")
    public ResponseEntity<BeneficioDTO> findById(@PathVariable Long id) {
        BeneficioDTO beneficio = service.findById(id);
        return ResponseEntity.ok(beneficio);
    }

    @PostMapping
    @Operation(summary = "Criar novo benefício")
    public ResponseEntity<BeneficioDTO> create(@Valid @RequestBody BeneficioDTO dto) {
        BeneficioDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar benefício")
    public ResponseEntity<BeneficioDTO> update(@PathVariable Long id, @Valid @RequestBody BeneficioDTO dto) {
        BeneficioDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar benefício")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transferir valor entre benefícios")
    public ResponseEntity<Map<String, String>> transfer(@Valid @RequestBody TransferenciaDTO transferenciaDTO) {
        service.transfer(transferenciaDTO);
        return ResponseEntity.ok(Map.of("message", "Transferência realizada com sucesso"));
    }
}
