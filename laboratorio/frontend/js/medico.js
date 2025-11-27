/* Inicialização do processo.
Faz o código dentro da função só ser executado após o HTML ser carregado.
 */
document.addEventListener("DOMContentLoaded", () => {
    carregarRequisicoesDoMedico();
});


// Busca de dados do médico e verificação/validação de sessão.
function carregarRequisicoesDoMedico() {

    const usuario = JSON.parse(sessionStorage.getItem("usuario"));

    if (!usuario || usuario.perfil !== "MEDICO") {
        alert("Médico não encontrado na sessão.");
        window.location.href = "../index.html";
        return;
    }

    // Comunicação com o Back e tratamento de erros.
    fetch(`http://localhost:8080/laboratorio/rest/medico/requisicao/${usuario.idUsuario}`)
        .then(resp => {
            if (!resp.ok) throw new Error("Erro ao buscar requisições.");
            return resp.json();
        })
        .then(lista => {
            popularTabela(lista);
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao carregar as requisições do médico.");
        });
}


// Renderização da tabela de requisições.
function popularTabela(lista) {
    const tbody = document.getElementById("tbody");
    tbody.innerHTML = ""; // limpa os placeholders

    if (!lista || lista.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align:center;">Nenhuma requisição encontrada.</td>
            </tr>
        `;
        return;
    }

    lista.forEach(requisicao => {

        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${requisicao.id}</td>
            <td>${requisicao.numeroPedido}</td>
            <td class="alinhamento-esquerda">${requisicao.nome}</td>
            <td>${formatarData(requisicao.data)}</td>
            <td>
                <button onclick="verExame(${requisicao.numeroPedido})" style="padding: 5px 15px;">Visualizar</button>
            </td>
        `;

        tbody.appendChild(tr);
    });
}


// Vai para a página de exames.
function verExame(numeroPedido) {
    sessionStorage.setItem("numeroPedidoSelecionado", numeroPedido);
    window.location.href = "../modules/exame.html";
}


// Converte "yyyy-MM-dd" -> "dd/MM/yyyy" para exibição em tabela.
function formatarData(data) {
    if (!data) return "";
    // se vier "yyyy-MM-dd"
    if (typeof data === "string" && data.includes("-")) {
        const [ano, mes, dia] = data.split("-");
        return `${dia}/${mes}/${ano}`;
    }
    return data;
}
