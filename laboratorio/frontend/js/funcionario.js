const form = document.getElementById("formExame");

const idPaciente = document.getElementById("idPaciente");
const idMedico = document.getElementById("idMedico");
const idTipoExame = document.getElementById("idTipoExame");
const dataExame = document.getElementById("dataExame");
const observacoes = document.getElementById("observacoes");

const statusLaudo = document.getElementById("statusLaudo");
const idLaudo = document.getElementById("idLaudo");
const dataLaudo = document.getElementById("dataLaudo");

const arquivoInput = document.getElementById("arquivo");
const nomeArquivo = document.getElementById("nome-arquivo");


// Nome do arquivo
arquivoInput.addEventListener("change", () => {
    nomeArquivo.textContent =
        arquivoInput.files.length > 0 ? arquivoInput.files[0].name : "Nenhum arquivo selecionado";
});


// Submit do form
form.addEventListener("submit", async (event) => {
    event.preventDefault();

    try {
        // Cria o FormData (necessário para enviar arquivo)
        const formData = new FormData();

        formData.append("idPaciente", idPaciente.value);
        formData.append("idMedico", idMedico.value);
        formData.append("idTipoExame", idTipoExame.value);
        formData.append("dataExame", dataExame.value);
        formData.append("observacoes", observacoes.value);

        formData.append("statusLaudo", statusLaudo.value);
        formData.append("idLaudo", idLaudo.value);
        formData.append("dataLaudo", dataLaudo.value);

        if (arquivoInput.files.length > 0) {
            formData.append("arquivo", arquivoInput.files[0]);
        }

        // URL do backend (ajuste se necessário)
        const url = "http://localhost:8080/laboratorio/rest/exame/cadastrar";

        const resposta = await fetch(url, {
            method: "POST",
            body: formData
        });

        const resultado = await resposta.json();

        if (resposta.ok) {
            alert("Exame cadastrado com sucesso!");

            // Limpa formulário visualmente
            form.reset();
            nomeArquivo.textContent = "Nenhum arquivo selecionado";

        } else {
            alert("Erro ao cadastrar exame: " + (resultado.mensagem || "Erro desconhecido"));
        }

    } catch (erro) {
        console.error("Erro ao enviar:", erro);
        alert("Falha ao conectar com o servidor.");
    }
});










// ===============================
// ELEMENTOS DA PÁGINA
// ===============================

// Requisição
const inputRequisicao = document.getElementById("idRequisicao");
const btnPesquisar = document.getElementById("btnPesquisar");
const btnLimpar = document.getElementById("btnLimpar");
const btnToggle = document.getElementById("btnToggle");

// Formulário completo
const formExame = document.getElementById("formExame");

// Campos do formulário
// lá pra cima já

// Tabela
const corpoTabela = document.querySelector("tbody");

// ===============================
// EVENTOS
// ===============================
btnPesquisar.addEventListener("click", pesquisarRequisicao);
btnLimpar.addEventListener("click", limparTela);
btnToggle.addEventListener("click", toggleFormulario);

arquivoInput.addEventListener("change", () => {
    nomeArquivo.value = arquivoInput.files.length > 0
        ? arquivoInput.files[0].name
        : "Nenhum arquivo selecionado";
});

formExame.addEventListener("submit", gravarExame);

// ===============================
// FUNÇÃO: BUSCAR EXAMES POR REQUISIÇÃO
// ===============================
function pesquisarRequisicao() {
    const id = inputRequisicao.value.trim();

    if (id === "") {
        alert("Informe o número da requisição.");
        return;
    }

    fetch(`http://localhost:8080/laboratorio/rest/exame/listarPorRequisicao/${id}`)
        .then(resp => {
            if (!resp.ok) throw new Error("Erro ao buscar dados.");
            return resp.json();
        })
        .then(lista => preencherTabela(lista))
        .catch(() => {
            alert("Requisição não encontrada.");
            corpoTabela.innerHTML = "";
        });
}

// ===============================
// FUNÇÃO: PREENCHER TABELA
// ===============================
function preencherTabela(lista) {
    corpoTabela.innerHTML = "";

    if (!lista || lista.length === 0) {
        corpoTabela.innerHTML = `
            <tr>
                <td colspan="10" style="text-align:center;">Nenhum exame encontrado.</td>
            </tr>
        `;
        return;
    }

    lista.forEach(exame => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${exame.id}</td>
            <td>${exame.paciente?.nome || exame.idPaciente}</td>
            <td>${exame.medico?.nome || exame.idMedico}</td>
            <td>${exame.tipoExame?.nome || exame.idTipoExame}</td>
            <td>${exame.observacoes || "---"}</td>
            <td>${exame.dataExame || "---"}</td>
            <td>${exame.status || "---"}</td>
            <td>${exame.idLaudo || "---"}</td>
            <td>${exame.dataLaudo || "---"}</td>

            <td>
                <button class="botao-editar" data-id="${exame.id}">Editar</button>

                ${exame.status === "PRONTO"
                ? `<button class="botao-laudo" data-id="${exame.id}">Laudo</button>`
                : ""
            }
            </td>
        `;

        corpoTabela.appendChild(tr);
    });

    // eventos dos botões criados dinamicamente
    document.querySelectorAll(".botao-editar").forEach(btn => {
        btn.addEventListener("click", () => carregarParaEdicao(btn.dataset.id));
    });

    document.querySelectorAll(".botao-laudo").forEach(btn => {
        btn.addEventListener("click", () => baixarLaudo(btn.dataset.id));
    });
}

// ===============================
// EDITAR EXAME
// ===============================
function carregarParaEdicao(id) {
    fetch(`http://localhost:8080/laboratorio/rest/exame/${id}`)
        .then(resp => resp.json())
        .then(exame => {
            idPaciente.value = exame.idPaciente;
            idMedico.value = exame.idMedico;
            idTipoExame.value = exame.idTipoExame;
            dataExame.value = exame.dataExame;
            observacoes.value = exame.observacoes;
            idLaudo.value = exame.idLaudo;
            dataLaudo.value = exame.dataLaudo;

            btnToggle.textContent = "Ocultar";
            formExame.parentElement.style.display = "block";
        });
}

// ===============================
// BAIXAR LAUDO
// ===============================
function baixarLaudo(idExame) {
    window.location.href = `http://localhost:8080/laboratorio/rest/exame/laudo/${idExame}`;
}

// ===============================
// GRAVAR EXAME
// ===============================
function gravarExame(event) {
    event.preventDefault();

    const dados = {
        idPaciente: idPaciente.value,
        idMedico: idMedico.value,
        idTipoExame: idTipoExame.value,
        dataExame: dataExame.value,
        observacoes: observacoes.value,
        idLaudo: idLaudo.value,
        dataLaudo: dataLaudo.value
    };

    fetch("http://localhost:8080/laboratorio/rest/exame", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dados)
    })
        .then(resp => {
            if (!resp.ok) throw new Error("Erro ao gravar exame.");
            return resp.json();
        })
        .then(() => {
            alert("Exame gravado com sucesso!");
            formExame.reset();
            nomeArquivo.value = "Nenhum arquivo selecionado";
        })
        .catch(() => alert("Erro ao gravar o exame."));
}

// ===============================
// LIMPAR
// ===============================
function limparTela() {
    inputRequisicao.value = "";
    formExame.reset();
    nomeArquivo.value = "Nenhum arquivo selecionado";
    corpoTabela.innerHTML = "";
}

// ===============================
// MOSTRAR / OCULTAR FORMULÁRIO
// ===============================
function toggleFormulario() {
    const container = formExame.parentElement;

    if (container.style.display === "none") {
        container.style.display = "block";
        btnToggle.textContent = "Ocultar";
    } else {
        container.style.display = "none";
        btnToggle.textContent = "Exibir";
    }
}
