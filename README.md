# Nível: Projeto B - API de Agendamentos

API REST de domínio único para gerenciamento de agendamentos com validação de horário comercial e detecção de conflitos, construída com Java 21, Spring Boot 4 e PostgreSQL.

## Arquitetura e Design
- **SOLID:** Separação estrita entre `Controller`, `Service`, `Repository` e validadores de domínio.
- **Design Pattern:** **Strategy** (`ValidadorAgendamento`) para desacoplar as regras de negócio (`ValidadorHorarioComercial` e `ValidadorConflitoHorario`).
- **Observabilidade:** Logs estruturados em JSON (Logstash Encoder) e métricas expostas em `/actuator/prometheus` (incluindo o contador customizado `agendamentos_criados_total`).
- **SLO:** 95% das requisições (`p95`) respondendo em `< 200ms` com taxa de erro `< 1%`.

## Como Subir a Aplicação (1 Comando)

1. Copie o arquivo de variáveis de ambiente:

        cp .env.example .env

2. Suba o banco de dados e a API com Docker Compose:

        docker compose up --build -d

3. Verifique se a aplicação está saudável:

        curl http://localhost:8080/actuator/health

## Exemplo de Uso do Fluxo Principal

**Criar Agendamento:**

    curl -X POST http://localhost:8080/api/agendamentos \
      -H "Content-Type: application/json" \
      -d '{
        "clienteNome": "João Silva",
        "servico": "Revisão Geral",
        "dataHoraInicio": "2027-01-15T10:00:00",
        "duracaoMinutos": 60
      }'

**Consultar Agendamento (substitua `{id}` pelo UUID retornado):**

    curl http://localhost:8080/api/agendamentos/{id}

## Como Rodar os Testes e Cobertura

A cobertura mínima declarada é de **70% de instruções** (verificada automaticamente pelo JaCoCo).

    ./mvnw verify

## Teste de Desempenho (k6)

Com a aplicação rodando via Docker Compose, execute:

    docker run --rm -i --network host -v "${PWD}/k6:/scripts" grafana/k6 run /scripts/smoke-test.js

Os resultados medidos (`p95 = 23.78 ms`, `0.00%` de falhas) estão documentados em `docs/desempenho.md`.