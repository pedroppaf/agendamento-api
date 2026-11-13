package com.pedroppaf.agendamento.service;

import com.pedroppaf.agendamento.domain.Agendamento;
import com.pedroppaf.agendamento.domain.validation.ValidadorConflitoHorario;
import com.pedroppaf.agendamento.domain.validation.ValidadorHorarioComercial;
import com.pedroppaf.agendamento.dto.AgendamentoRequest;
import com.pedroppaf.agendamento.dto.AgendamentoResponse;
import com.pedroppaf.agendamento.exception.RegraNegocioException;
import com.pedroppaf.agendamento.repository.AgendamentoRepository;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository repository;

    private AgendamentoService service;

    @BeforeEach
    void setUp() {
        var validadores = List.of(
                new ValidadorHorarioComercial(),
                new ValidadorConflitoHorario(repository)
        );
        service = new AgendamentoService(repository, validadores, new SimpleMeterRegistry());
    }

    @Test
    void deveCriarAgendamentoQuandoDadosValidos() {
        LocalDateTime dataFutura = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        AgendamentoRequest request = new AgendamentoRequest("Carlos Silva", "Revisão", dataFutura, 60);

        when(repository.existsConflitoHorario(any(), any())).thenReturn(false);
        when(repository.save(any(Agendamento.class))).thenAnswer(inv -> inv.getArgument(0));

        AgendamentoResponse response = service.criar(request);

        assertNotNull(response);
        assertEquals("Carlos Silva", response.clienteNome());
        assertEquals(dataFutura.plusMinutes(60), response.dataHoraFim());
        verify(repository, times(1)).save(any(Agendamento.class));
    }

    @Test
    void deveLancarExcecaoQuandoForaDoHorarioComercial() {
        LocalDateTime dataForaHorario = LocalDateTime.now().plusDays(1).withHour(20).withMinute(0);
        AgendamentoRequest request = new AgendamentoRequest("Carlos Silva", "Revisão", dataForaHorario, 60);

        RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.criar(request));
        assertTrue(ex.getMessage().contains("08:00 e 18:00"));
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoHouverConflitoDeHorario() {
        LocalDateTime dataFutura = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
        AgendamentoRequest request = new AgendamentoRequest("Ana Souza", "Consultoria", dataFutura, 30);

        when(repository.existsConflitoHorario(any(), any())).thenReturn(true);

        RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.criar(request));
        assertEquals("Já existe um agendamento neste intervalo de horário.", ex.getMessage());
        verify(repository, never()).save(any());
    }
}
