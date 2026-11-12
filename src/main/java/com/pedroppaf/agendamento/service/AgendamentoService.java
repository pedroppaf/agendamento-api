package com.pedroppaf.agendamento.service;

import com.pedroppaf.agendamento.domain.Agendamento;
import com.pedroppaf.agendamento.domain.validation.ValidadorAgendamento;
import com.pedroppaf.agendamento.dto.AgendamentoRequest;
import com.pedroppaf.agendamento.dto.AgendamentoResponse;
import com.pedroppaf.agendamento.exception.RecursoNaoEncontradoException;
import com.pedroppaf.agendamento.repository.AgendamentoRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AgendamentoService {

    private static final Logger log = LoggerFactory.getLogger(AgendamentoService.class);

    private final AgendamentoRepository repository;
    private final List<ValidadorAgendamento> validadores;
    private final Counter agendamentosCriadosCounter;

    public AgendamentoService(AgendamentoRepository repository,
                              List<ValidadorAgendamento> validadores,
                              MeterRegistry meterRegistry) {
        this.repository = repository;
        this.validadores = validadores;
        this.agendamentosCriadosCounter = Counter.builder("agendamentos_criados_total")
                .description("Total de agendamentos criados com sucesso")
                .register(meterRegistry);
    }

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {
        validadores.forEach(v -> v.validar(request));

        Agendamento agendamento = new Agendamento(
                request.clienteNome(),
                request.servico(),
                request.dataHoraInicio(),
                request.duracaoMinutos()
        );

        Agendamento salvo = repository.save(agendamento);
        agendamentosCriadosCounter.increment();
        log.info("Agendamento criado com sucesso: id={}, servico={}", salvo.getId(), salvo.getServico());

        return AgendamentoResponse.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(UUID id) {
        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado."));
        return AgendamentoResponse.fromEntity(agendamento);
    }
}
