# ADR 0001: Uso do Design Pattern Strategy para Validações de Domínio e PostgreSQL

## Status
Aceito

## Contexto
A API de Agendamentos precisa validar múltiplas regras de negócio antes de persistir um novo agendamento (horário comercial, conflito de agenda, futuras regras de feriados ou limites por cliente). Concentrar todas as verificações em blocos condicionais no `AgendamentoService` violaria o princípio Aberto/Fechado (OCP) do SOLID e dificultaria testes unitários isolados. Além disso, é necessário garantir integridade transacional na persistência dos horários.

## Decisão
1. Adotar o padrão **Strategy** por meio da interface `ValidadorAgendamento`, permitindo que cada regra de negócio seja implementada como um componente Spring independente injetado automaticamente via lista no `AgendamentoService`.
2. Utilizar **PostgreSQL** via container Docker como banco relacional principal para garantir propriedades ACID nas transações de criação e consulta de intervalos de tempo.

## Consequências
- **Positivas:** Novas regras de validação podem ser adicionadas criando apenas uma nova classe que implementa `ValidadorAgendamento`, sem alterar o `AgendamentoService`. Cada regra é testada unitariamente de forma isolada.
- **Negativas:** Ligeiro aumento no número de classes pequenas no pacote `domain.validation`.