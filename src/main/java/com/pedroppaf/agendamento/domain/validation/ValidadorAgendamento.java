package com.pedroppaf.agendamento.domain.validation;

import com.pedroppaf.agendamento.dto.AgendamentoRequest;

public interface ValidadorAgendamento {
    void validar(AgendamentoRequest request);
}
