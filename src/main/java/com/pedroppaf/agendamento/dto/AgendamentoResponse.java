package com.pedroppaf.agendamento.dto;

import com.pedroppaf.agendamento.domain.Agendamento;

import java.time.LocalDateTime;
import java.util.UUID;

public record AgendamentoResponse(
        UUID id,
        String clienteNome,
        String servico,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim
) {
    public static AgendamentoResponse fromEntity(Agendamento entity) {
        return new AgendamentoResponse(
                entity.getId(),
                entity.getClienteNome(),
                entity.getServico(),
                entity.getDataHoraInicio(),
                entity.getDataHoraFim()
        );
    }
}
