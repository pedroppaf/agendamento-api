package com.pedroppaf.agendamento.controller;

import com.pedroppaf.agendamento.dto.AgendamentoRequest;
import com.pedroppaf.agendamento.dto.AgendamentoResponse;
import com.pedroppaf.agendamento.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@RequestBody @Valid AgendamentoRequest request,
                                                     UriComponentsBuilder uriBuilder) {
        AgendamentoResponse response = service.criar(request);
        URI uri = uriBuilder.path("/api/agendamentos/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
