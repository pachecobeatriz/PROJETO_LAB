document.addEventListener("DOMContentLoaded", () => {
    carregarRequisicoesDoMedico();
});


function carregarRequisicoesDoMedico() {

    const usuario = JSON.parse(sessionStorage.getItem("usuario"));

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


/* // adiciona eventos aos botões
    document.querySelectorAll(".btn-visualizar").forEach(btn => {
        btn.addEventListener("click", function () {
            const id = this.getAttribute("data-id");

            // salva ID da requisição para exame.html
            sessionStorage.setItem("requisicaoId", id);

            // vai para a tela exame
            window.location.href = "../modules/exame.html";
        });
    }); */