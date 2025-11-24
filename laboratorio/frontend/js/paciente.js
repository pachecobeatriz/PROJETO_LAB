document.addEventListener("DOMContentLoaded", () => {
    carregarRequisicoesDoPaciente();
});


function carregarRequisicoesDoPaciente() {

    const usuario = JSON.parse(sessionStorage.getItem("usuario"));

    if (!usuario) {
        alert("Usuário não encontrado na sessão.");
        window.location.href = "../index.html";
        return;
    }

    fetch(`http://localhost:8080/laboratorio/rest/paciente/requisicao/${usuario.idUsuario}`)
        .then(resp => {
            if (!resp.ok) throw new Error("Erro ao buscar requisições.");
            return resp.json();
        })
        .then(lista => {
            popularTabela(lista);
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao carregar as requisições do paciente.");
        });
}


function popularTabela(lista) {
    const tbody = document.getElementById("tbody");
    tbody.innerHTML = "";

    if (!lista || lista.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align:center;">Nenhuma requisição encontrada.</td>
            </tr>
        `;
        return;
    }

    lista.forEach(item => {

        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${item.id}</td>
            <td>${item.numeroPedido}</td>
            <td class="alinhamento-esquerda">${item.nome}</td>
            <td>${formatarData(item.data)}</td>
            <td>
                <button onclick="verExame(${item.numeroPedido})" style="padding: 5px 15px;">Visualizar</button>
            </td>
        `;

        tbody.appendChild(tr);
    });
}


function verExame(numeroPedido) {
    sessionStorage.setItem("numeroPedidoSelecionado", numeroPedido);
    window.location.href = "../modules/exame.html";
}


function formatarData(data) {
    return data.split("-").reverse().join("/");
}
