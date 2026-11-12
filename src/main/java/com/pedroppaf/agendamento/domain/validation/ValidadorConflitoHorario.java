package com.pedroppaf.agendamento.domain.validation;

import com.pedroppaf.agendamento.dto.AgendamentoRequest;
import com.pedroppaf.agendamento.exception.RegraNegocioException;
import com.pedroppaf.agendamento.repository.AgendamentoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorConflitoHorario implements ValidadorAgendamento {

    private final AgendamentoRepository repository;

    public ValidadorConflitoHorario(AgendamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(AgendamentoRequest request) {
        LocalDateTime inicio = request.dataHoraInicio();
        LocalDateTime fim = inicio.plusMinutes(request.duracaoMinutos());

        if (repository.existsConflitoHorario(inicio, fim)) {
            throw new RegraNegocioException("Já existe um agendamento neste intervalo de horário.");
        }
    }
}