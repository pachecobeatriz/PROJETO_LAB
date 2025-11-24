// ~ NOVAS ADIÇÕES - Sandro ~

document.addEventListener("DOMContentLoaded", () => {
    const usuario = JSON.parse(sessionStorage.getItem("usuario"));

    if (!usuario) {
        alert("Usuário não encontrado na sessão.");
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

function carregarExames(numeroPedido) {
    fetch(`http://localhost:8080/laboratorio/rest/exame/listarPorRequisicao/${numeroPedido}`)
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

        tr.innerHTML = `
            <td>${exame.id || exame.idExame}</td>
            <td class="alinhamento-esquerda">${exame.nomeExame || "---"}</td>
            <td class="alinhamento-esquerda">${exame.observacoes || "---"}</td>
            <td>${formatarData(exame.dataExame)}</td>
            <td>${exame.status || "---"}</td>
            <td>
                ${exame.idLaudo && exame.idLaudo !== 0
                    ? `<button style="padding: 5px 15px;">Baixar</button>`
                    : `---`
                }
            </td>
        `;

        tbody.appendChild(tr);
    });
}

function formatarData(data) {
    if (!data) return "";

    if (typeof data === "string" && data.includes("-")) {
        const [ano, mes, dia] = data.split("-");
        return `${dia}/${mes}/${ano}`;
    }

    return data;
}
