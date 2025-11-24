// Quando página carrega → busca as requisições do médico
document.addEventListener("DOMContentLoaded", () => {
    carregarRequisicoesDoMedico();
});


// FUNÇÃO
function carregarRequisicoesDoMedico() {

    const medicoId = usuarioLogado.id;

    fetch(`http://localhost:8080/laboratorio/rest/medico/requisicao/${medicoId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao consultar requisições");
            }
            return response.json();
        })
        .then(requisicoes => preencherTabela(requisicoes))
        .catch(err => {
            console.error(err);
            alert("Erro ao carregar requisições do médico.");
        });
}


// FUNÇÃO
function preencherTabela(lista) {

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

    lista.forEach(req => {

        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${req.id}</td>
            <td>${req.descricao}</td>
            <td class="alinhamento-esquerda">${req.nomePaciente}</td>
            <td>${req.dataRequisicao}</td>
            <td>
                <button class="btn-visualizar" data-id="${req.id}" style="padding: 5px 15px;">
                    Visualizar
                </button>
            </td>
        `;

        tbody.appendChild(tr);
    });

    // adiciona eventos aos botões
    document.querySelectorAll(".btn-visualizar").forEach(btn => {
        btn.addEventListener("click", function () {
            const id = this.getAttribute("data-id");

            // salva ID da requisição para exame.html
            sessionStorage.setItem("requisicaoId", id);

            // vai para a tela exame
            window.location.href = "../modules/exame.html";
        });
    });
}
