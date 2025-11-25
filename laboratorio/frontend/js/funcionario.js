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


arquivoInput.addEventListener("change", () => {
    nomeArquivo.textContent =
        arquivoInput.files.length > 0 ? arquivoInput.files[0].name : "Nenhum arquivo selecionado";
});


/* form.addEventListener("submit", async (event) => {
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

        const url = "http://localhost:8080/laboratorio/rest/exame/cadastrar";

        const resposta = await fetch(url, {
            method: "POST",
            body: formData
        });

        const resultado = await resposta.json();

        if (resposta.ok) {
            alert("Exame cadastrado com sucesso!");

            form.reset();
            nomeArquivo.textContent = "Nenhum arquivo selecionado";

        } else {
            alert("Erro ao cadastrar exame: " + (resultado.mensagem || "Erro desconhecido"));
        }

    } catch (erro) {
        console.error("Erro ao enviar:", erro);
        alert("Falha ao conectar com o servidor.");
    }
}); */


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

    fetch("http://localhost:8080/laboratorio/rest/exame/cadastrar", {
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





// ~ ~
/* 
// ... (Seu código inicial e definições de variáveis) ...

// Variável global para saber se estamos editando
let idExameEmEdicao = null;

// Campos do formulário (ADICIONAR: numeroPedido para POST, e o status/idlaudo para visualização)
const inputNumeroPedido = document.getElementById("idRequisicao");
// ... outros inputs ...
const inputStatusLaudo = document.getElementById("statusLaudo"); // PRECISA ADICIONAR ID NO HTML
const inputIdLaudo = document.getElementById("idLaudo");
// ...

// ===============================
// FUNÇÃO: GRAVAR EXAME (POST para novo / PUT para edição ou conclusão)
// ===============================
async function gravarExame(event) {
    event.preventDefault();

    // 1. Validação de Campos Obrigatórios
    if (!idPaciente.value || !idMedico.value || !idTipoExame.value || !dataExame.value) {
        alert("Os campos ID Paciente, ID Médico, ID Tipo Exame e Data do Exame são obrigatórios.");
        return;
    }

    // 2. Preparar FormData para o Endpoint /gravar (POST com arquivo)
    const formData = new FormData();
    const urlEndpoint = "http://localhost:8080/laboratorio/rest/exame/gravar";

    // Adiciona o ID do exame (0 para novo, ID para edição/conclusão)
    formData.append("idExame", idExameEmEdicao || 0);

    // Campos obrigatórios
    formData.append("idPaciente", idPaciente.value);
    formData.append("idMedico", idMedico.value);
    formData.append("idTipoExame", idTipoExame.value);
    formData.append("dataExame", dataExame.value);
    formData.append("numeroPedido", inputRequisicao.value || 0); // Requisicao é necessária
    formData.append("observacoes", observacoes.value || "");

    // Arquivo (se existir, será CONCLUSAO)
    if (arquivoInput.files.length > 0) {
        formData.append("arquivo", arquivoInput.files[0]);
    }

    try {
        const resposta = await fetch(urlEndpoint, {
            method: "POST",
            body: formData // Envia como Multipart
        });

        const textoResposta = await resposta.text();

        if (resposta.ok) {
            alert("Exame gravado/concluído com sucesso! \n" + textoResposta);

            // 3. Após sucesso: limpar e atualizar a tabela
            limparFormulario();
            // Disparar pesquisa novamente para atualizar a tabela
            if (inputRequisicao.value) {
                pesquisarRequisicao();
            }

        } else {
            alert("Erro ao gravar exame: " + textoResposta);
        }

    } catch (erro) {
        console.error("Erro ao enviar:", erro);
        alert("Falha ao conectar com o servidor.");
    }
}

// ===============================
// FUNÇÃO: CARREGAR PARA EDIÇÃO
// ===============================
function carregarParaEdicao(id) {
    fetch(`http://localhost:8080/laboratorio/rest/exame/${id}`)
        .then(resp => {
            if (!resp.ok) throw new Error("Exame não encontrado.");
            return resp.json();
        })
        .then(exame => {
            idExameEmEdicao = id; // Define o ID para a próxima gravação ser um PUT/Conclusão

            // 1. Popular campos
            idPaciente.value = exame.idPaciente;
            idMedico.value = exame.idMedico;
            idTipoExame.value = exame.idTipoExame;
            dataExame.value = exame.dataExame; // Assumindo formato YYYY-MM-DD
            observacoes.value = exame.observacoes;

            // 2. Campos Desabilitados (Visualização)
            // Se o DTO/DAO foi atualizado para retornar idLaudo/dataLaudo, use-os.
            inputIdLaudo.value = exame.idLaudo || "";
            inputStatusLaudo.value = exame.status || "PENDENTE";
            dataLaudo.value = exame.dataLaudo || "";

            // 3. Exibir Formulário
            btnToggle.textContent = "Ocultar";
            formExame.parentElement.style.display = "block";
        })
        .catch(erro => alert(erro.message));
}

// ===============================
// FUNÇÃO: EXCLUIR EXAME
// ===============================
function excluirExame(idExame) {
    if (!confirm(`Deseja realmente excluir o Exame ID ${idExame}?`)) {
        return;
    }

    fetch(`http://localhost:8080/laboratorio/rest/exame/excluir/${idExame}`, {
        method: "DELETE"
    })
        .then(async resp => {
            const mensagem = await resp.text();

            if (resp.ok) {
                alert("Exclusão bem-sucedida: " + mensagem);
                pesquisarRequisicao(); // Atualiza a tabela
            } else if (resp.status === 400 && mensagem.includes("diferente de PENDENTE")) {
                alert("Atenção: Exames com laudos cadastrados não podem ser excluídos.");
            } else {
                alert("Erro ao excluir exame: " + mensagem);
            }
        })
        .catch(() => alert("Falha ao conectar com o servidor para excluir."));
}

// ===============================
// FUNÇÃO: LIMPAR FORMULÁRIO (Adicione esta função)
// ===============================
function limparFormulario() {
    formExame.reset();
    idExameEmEdicao = null; // Zera a variável de edição
    nomeArquivo.value = "Nenhum arquivo selecionado";
    // Define os campos desabilitados para o estado inicial
    inputStatusLaudo.value = "PENDENTE";
    inputIdLaudo.value = "";
    dataLaudo.value = "";
}

// ===============================
// FUNÇÃO: PREENCHER TABELA (ADICIONAR BOTÃO EXCLUIR)
// ===============================
function preencherTabela(lista) {
    corpoTabela.innerHTML = "";

    // ... (parte de lista vazia) ...

    lista.forEach(exame => {
        const tr = document.createElement("tr");

        // O seu HTML atual já inclui os campos Laudo e Data Laudo, que o ExameDTO fornece
        const isPronto = exame.status === "PRONTO";

        // Determina se pode excluir: Apenas se for PENDENTE
        const podeExcluir = exame.status === "PENDENTE";

        tr.innerHTML = `
            <td>${exame.idExame}</td>
            <td>${exame.paciente || exame.idPaciente}</td>
            <td>${exame.medico || exame.idMedico}</td>
            <td>${exame.nomeExame || exame.idTipoExame}</td>
            <td>${exame.observacoes || "---"}</td>
            <td>${exame.dataExame || "---"}</td>
            <td>${exame.status || "---"}</td>
            <td>${exame.idLaudo || "---"}</td>
            <td>${exame.dataLaudo || "---"}</td>

            <td>
                <button class="botao-editar" data-id="${exame.idExame}" style="padding: 5px 15px;">Editar</button>

                ${isPronto
                ? `<button class="botao-laudo" data-id="${exame.idExame}" style="padding: 5px 15px;">Laudo</button>`
                : ""
            }
                
                ${podeExcluir
                ? `<button class="botao-excluir" data-id="${exame.idExame}" style="padding: 5px 15px;">Excluir</button>`
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

    // NOVO EVENTO: EXCLUIR
    document.querySelectorAll(".botao-excluir").forEach(btn => {
        btn.addEventListener("click", () => excluirExame(btn.dataset.id));
    });
}
 */