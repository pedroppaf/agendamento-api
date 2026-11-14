import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 5,
    duration: '15s',
    thresholds: {
        http_req_duration: ['p(95)<200'], // SLO: 95% das requisições abaixo de 200ms
        http_req_failed: ['rate<0.01'],   // SLO: taxa de erro de servidor < 1%
    },
};

export default function () {
    http.setResponseCallback(http.expectedStatuses({ min: 200, max: 499 }));

    const res = http.get('http://localhost:8080/actuator/health');
    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    const uniqueDay = Math.floor(Math.random() * 28) + 1;
    const uniqueMonth = Math.floor(Math.random() * 12) + 1;
    const payload = JSON.stringify({
        clienteNome: 'Teste Carga k6',
        servico: 'Smoke Test',
        dataHoraInicio: `2027-${String(uniqueMonth).padStart(2, '0')}-${String(uniqueDay).padStart(2, '0')}T10:00:00`,
        duracaoMinutos: 15,
    });

    const params = { headers: { 'Content-Type': 'application/json' } };
    const postRes = http.post('http://localhost:8080/api/agendamentos', payload, params);

    check(postRes, {
        'created or business rule handled': (r) => r.status === 201 || r.status === 422,
    });

    sleep(0.5);
}