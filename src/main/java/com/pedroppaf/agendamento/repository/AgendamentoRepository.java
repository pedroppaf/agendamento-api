package com.pedroppaf.agendamento.repository;

import com.pedroppaf.agendamento.domain.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    @Query("""
        SELECT COUNT(a) > 0 FROM Agendamento a
        WHERE a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio
    """)
    boolean existsConflitoHorario(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
