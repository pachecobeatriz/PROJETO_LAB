// Configuração de endpoints.
const API_BASE = "http://localhost:8080/laboratorio/rest";
const ENDPOINT_EXAMES_REQUISICAO = `${API_BASE}/exame/listarPorRequisicao`;
const ENDPOINT_LAUDO_DOWNLOAD = `${API_BASE}/laudo/download`;

// Inicialização, validação de sessão e contexto.
document.addEventListener("DOMContentLoaded", () => {
    const usuario = JSON.parse(sessionStorage.getItem("usuario"));

    if (!usuario) {
        alert("Usuário não encontrado na sessão.");
        window.location.href = "../index.html";
        return;
    }

    /* Garante que é PACIENTE ou MEDICO.
    Não precisa, pois o FUNCIONARIO não tem navegabilidade para essa página, mas deu vontade de fazer.
    */
    if (usuario.perfil === "FUNCIONARIO" || !usuario.perfil === "PACIENTE" || !usuario.perfil === "MEDICO") {
        alert("Apenas pacientes ou médicos podem acessar esta tela de exames.");
        window.location.href = "../index.html";
        return;
    }

    const numeroPedido = sessionStorage.getItem("numeroPedidoSelecionado");

    if (!numeroPedido) {
        alert("Nenhuma requisição selecionada.");
        window.location.href = "../modules/paciente.html";
        return;
    }

    carregarExames(numeroPedido);
});


// Busca de dados da requisição.
function carregarExames(numeroPedido) {
    fetch(`${ENDPOINT_EXAMES_REQUISICAO}/${numeroPedido}`)
        .then(resp => {
            if (!resp.ok) {
                throw new Error("Erro ao buscar exames.");
            }
            return resp.json();
        })
        .then(lista => preencherTabelaExames(lista))
        .catch(err => {
            console.error(err);
            alert("Erro ao carregar exames da requisição.");
        });
}


// Renderização da tabela de exames e ações.
function preencherTabelaExames(lista) {
    const tbody = document.getElementById("tbody");
    tbody.innerHTML = "";

    if (!lista || lista.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" style="text-align:center;">Nenhum exame encontrado para esta requisição.</td>
            </tr>
        `;
        return;
    }

    lista.forEach(exame => {
        const tr = document.createElement("tr");

        const idExame = exame.idExame || exame.id;
        const nomeExame = exame.nomeExame || "---";
        const obs = exame.observacoes || "---";
        const dataExame = formatarData(exame.dataExame);
        const status = exame.status || "---";
        const temLaudo = exame.idLaudo && exame.idLaudo !== 0;

        // Ser ou não ser, eis a questão
        // Só pode baixar se status PRONTO e existir laudo
        const podeBaixar = status === "PRONTO" && temLaudo;

        tr.innerHTML = `
            <td>${idExame}</td>
            <td class="alinhamento-esquerda">${nomeExame}</td>
            <td class="alinhamento-esquerda">${obs}</td>
            <td>${dataExame}</td>
            <td>${status}</td>
            <td>
                ${podeBaixar
                ? `<button class="btn-baixar" data-id-exame="${idExame}" style="padding: 5px 15px;">Baixar</button>`
                : `---`
            }
            </td>
        `;

        tbody.appendChild(tr);
    });

    // Liga evento de clique nos botões "Baixar"
    tbody.querySelectorAll(".btn-baixar").forEach(btn => {
        btn.addEventListener("click", () => {
            const idExame = btn.getAttribute("data-id-exame");
            baixarLaudo(idExame);
        });
    });
}


// Formatador de datas.
function formatarData(data) {
    if (!data) return "";

    if (typeof data === "string" && data.includes("-")) {
        const [ano, mes, dia] = data.split("-");
        return `${dia}/${mes}/${ano}`;
    }

    return data;
}


// Download do laudo e utilitárioss
function baixarLaudo(idExame) {
    if (!idExame) {
        alert("Exame inválido para download do laudo.");
        return;
    }

    /* O backend ainda faz as verificações (status PRONTO + laudo existente).
    Com isso aqui, não corre o risco do exame ser excluido do banco e o sistema quebrar porque não consegue fazer download
    */
    window.location.href = `${ENDPOINT_LAUDO_DOWNLOAD}/${idExame}`;
}
