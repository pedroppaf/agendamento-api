package com.pedroppaf.agendamento.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoRequest(
        @NotBlank(message = "Nome do cliente é obrigatório")
        String clienteNome,

        @NotBlank(message = "Serviço é obrigatório")
        String servico,

        @NotNull(message = "Data e hora de início são obrigatórias")
        @Future(message = "O agendamento deve ser para uma data futura")
        LocalDateTime dataHoraInicio,

        @Min(value = 15, message = "Duração mínima é de 15 minutos")
        int duracaoMinutos
) {}
