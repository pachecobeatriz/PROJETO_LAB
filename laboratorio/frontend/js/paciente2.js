// Arquivo: ../js/paciente.js

// BASE_URL deve ser definida globalmente ou importada, vamos assumir que é uma constante:
const BASE_URL = 'http://localhost:8080/laboratorio/rest/exame/requsicoes/paciente/';
const TABELA_CORPO = document.getElementById('tbody');

document.addEventListener('DOMContentLoaded', function () {
    carregarRequisicoesDoPaciente();
});

function carregarRequisicoesDoPaciente() {
    // 1. Obter o ID do Paciente Logado
    const usuarioLogado = JSON.parse(sessionStorage.getItem('usuarioLogado'));

    if (!usuarioLogado || usuarioLogado.idUsuario <= 0) {
        alert("Erro: ID do paciente não encontrado na sessão.");
        return;
    }

    const idPaciente = usuarioLogado.idUsuario;

    // 2. Chamar o Backend
    fetch(BASE_URL + idPaciente, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.status === 404) {
                TABELA_CORPO.innerHTML = '<tr><td colspan="5">Nenhuma requisição de exame encontrada.</td></tr>';
                return [];
            }
            if (!response.ok) {
                throw new Error('Falha ao buscar requisições: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            popularTabela(data);
        })
        .catch(error => {
            console.error('Erro na requisição:', error);
            alert('Erro ao carregar as requisições: ' + error.message);
            TABELA_CORPO.innerHTML = '<tr><td colspan="5">Erro ao carregar dados. Tente novamente.</td></tr>';
        });
}

function popularTabela(requisicoes) {
    // Limpa o corpo da tabela antes de popular
    TABELA_CORPO.innerHTML = '';

    if (requisicoes.length === 0) {
        TABELA_CORPO.innerHTML = '<tr><td colspan="5">Nenhuma requisição de exame encontrada.</td></tr>';
        return;
    }

    requisicoes.forEach(req => {
        const linha = document.createElement('tr');

        // Formata a data para dd/mm/aaaa (supondo que a data do DTO seja no formato 'aaaa-mm-dd')
        const dataFormatada = req.data ? new Date(req.data).toLocaleDateString('pt-BR') : 'N/A';

        linha.innerHTML = `
            <td>${req.id}</td>
            <td>${req.numeroPedido}</td>
            <td class="alinhamento-esquerda">${req.nome}</td>
            <td>${dataFormatada}</td>
            <td>
                <a href="../modules/exame.html?pedido=${req.numeroPedido}">
                    <button style="padding: 5px 15px;">Visualizar</button>
                </a>
            </td>
        `;
        TABELA_CORPO.appendChild(linha);
    });
}
