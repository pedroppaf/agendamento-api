package com.pedroppaf.agendamento.domain.validation;

import com.pedroppaf.agendamento.dto.AgendamentoRequest;
import com.pedroppaf.agendamento.exception.RegraNegocioException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorHorarioComercial implements ValidadorAgendamento {

    @Override
    public void validar(AgendamentoRequest request) {
        int horaInicio = request.dataHoraInicio().getHour();
        int horaFim = request.dataHoraInicio().plusMinutes(request.duracaoMinutos()).getHour();

        if (horaInicio < 8 || horaFim > 18) {
            throw new RegraNegocioException("Agendamentos permitidos apenas entre 08:00 e 18:00.");
        }
    }
}
