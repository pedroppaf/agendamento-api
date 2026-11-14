# Registro de Uso de Inteligência Artificial

Em conformidade com as diretrizes do projeto, abaixo estão documentados os prompts relevantes utilizados como apoio durante o desenvolvimento:

1. **Estruturação do Projeto e Boilerplate:**
    - *Prompt:* "Gere a estrutura base de uma API REST em Java 21 com Spring Boot 4, PostgreSQL, Docker Compose, JaCoCo e GitHub Actions seguindo princípios SOLID."
    - *Decisão Humana:* Escolhi aplicar o padrão Strategy para separar as regras de validação de horário comercial e conflito de agenda, garantindo extensibilidade sem modificar o serviço principal.

2. **Configuração de Observabilidade e Testes de Carga:**
    - *Prompt:* "Como configurar logs estruturados em JSON com Logback, métricas Prometheus via Spring Actuator e um smoke test básico em k6?"
    - *Decisão Humana:* Defini o SLO de latência p95 < 200ms e ajustei o script k6 para gerar datas futuras aleatórias, evitando falsos positivos de conflito de horário durante o teste.