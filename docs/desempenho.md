# Relatório de Desempenho e SLO

## Objetivo de Nível de Serviço (SLO)
- **Latência:** 95% das requisições (`p95`) ao endpoint principal devem responder em **menos de 200ms**.
- **Disponibilidade / Erro:** Taxa de falhas HTTP (`5xx`) inferior a **1%**.

## Metodologia (Smoke Test com k6)
- **Ferramenta:** k6 (`grafana/k6` via Docker)
- **Script:** `k6/smoke-test.js`
- **Carga:** 5 usuários virtuais (VUs) simultâneos durante 15 segundos (145 iterações completas) contra a aplicação rodando via `docker compose`.

## Resultados Medidos

| Métrica | Valor Medido | Limite (SLO) | Status |
| :--- | :--- | :--- | :--- |
| **Total de Requisições (`http_reqs`)** | `290` (`19.20 req/s`) | — | — |
| **Latência Mínima (`min`)** | `2.09 ms` | — | — |
| **Latência Média (`avg`)** | `9.40 ms` | — | — |
| **Latência Mediana (`med`)** | `6.59 ms` | — | — |
| **Latência p90 (`p(90)`)** | `18.72 ms` | — | — |
| **Latência p95 (`p(95)`)** | **`23.78 ms`** | `< 200 ms` | **Aprovado** |
| **Latência Máxima (`max`)** | `57.87 ms` | — | — |
| **Taxa de Erro (`http_req_failed`)** | **`0.00%`** (`0 de 290`) | `< 1.00%` | **Aprovado** |
| **Checks Aprovados** | `100.00%` (`290 de 290`) | — | **Aprovado** |

## Conclusão
A API atendeu com folga aos objetivos definidos no SLO, mantendo a latência `p95` em **23.78 ms** (aproximadamente 8,4x abaixo do limite de 200 ms) e **0.00%** de falhas de servidor sob carga concorrente.