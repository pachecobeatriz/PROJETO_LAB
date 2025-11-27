// Configuração de endpoints e elementos.
const API_BASE = "http://localhost:8080/laboratorio/rest";
const ENDPOINT_LISTAR_REQUISICAO = `${API_BASE}/exame/listarPorRequisicao`;
const ENDPOINT_EXCLUIR_EXAME = `${API_BASE}/exame/excluir`;
const ENDPOINT_LAUDO_CADASTRAR = `${API_BASE}/laudo/cadastrar`;
const ENDPOINT_LAUDO_DOWNLOAD = `${API_BASE}/laudo/download`;

const inputRequisicao = document.getElementById("idRequisicao");
const btnPesquisar = document.getElementById("btnPesquisar");
const btnLimpar = document.getElementById("btnLimpar");
const btnToggle = document.getElementById("btnToggle");

const containerFormData = document.getElementById("containerFormData");
const formExame = document.getElementById("formExame");
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

const tbody = document.getElementById("tbody");
let examesCarregados = [];
let idExameEmEdicao = null;


/* Inicialização do processo.
Faz o código dentro da função só ser executado após o HTML ser carregado.
*/
document.addEventListener("DOMContentLoaded", () => {
    validarSessaoFuncionario();
    configurarEventos();
});

// Verificação/validação de sessão.
function validarSessaoFuncionario() {
    const usuario = JSON.parse(sessionStorage.getItem("usuario"));

    if (!usuario || usuario.perfil !== "FUNCIONARIO") {
        alert("Funcionário não encontrado na sessão.");
        window.location.href = "../index.html";
        return;
    }
}

// Configuração de eventos dos botões do formulário de pesquisa.
function configurarEventos() {
    if (btnPesquisar) {
        btnPesquisar.addEventListener("click", (e) => {
            e.preventDefault();
            pesquisarRequisicao();
        });
    }

    if (btnLimpar) {
        btnLimpar.addEventListener("click", (e) => {
            e.preventDefault();
            limparTela();
        });
    }

    if (btnToggle) {
        btnToggle.addEventListener("click", (e) => {
            e.preventDefault();
            toggleFormulario();
        });
    }

    if (arquivoInput) {
        arquivoInput.addEventListener("change", () => {
            nomeArquivo.value = arquivoInput.files.length > 0
                ? arquivoInput.files[0].name
                : "Nenhum arquivo selecionado";
        });
    }

    if (formExame) {
        formExame.addEventListener("submit", gravarLaudo);
    }
}


// Busca de requisições.
function pesquisarRequisicao() {
    const numero = inputRequisicao.value.trim();

    if (!numero) {
        alert("Informe o número da requisição.");
        return;
    }

    fetch(`${ENDPOINT_LISTAR_REQUISICAO}/${numero}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao buscar exames da requisição.");
            }
            return response.json();
        })
        .then(lista => {
            examesCarregados = Array.isArray(lista) ? lista : [];
            preencherTabela(examesCarregados);
        })
        .catch(error => {
            console.error("Erro na busca:", error);
            alert("Requisição não encontrada ou erro ao buscar os exames.");
            tbody.innerHTML = "";
        });
}


// Renderização da tabela de exames.
function preencherTabela(lista) {
    tbody.innerHTML = "";

    if (!lista || lista.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="10" style="text-align: center;">Nenhum exame encontrado para esta requisição.</td>
            </tr>
        `;
        return;
    }

    lista.forEach(exame => {
        const tr = document.createElement("tr");

        const isPronto = exame.status === "PRONTO";
        const podeExcluir = exame.status === "PENDENTE";

        tr.innerHTML = `
            <td>${exame.idExame}</td>
            <td class="alinhamento-esquerda">${exame.paciente || exame.idPaciente}</td>
            <td class="alinhamento-esquerda">${exame.medico || exame.idMedico}</td>
            <td class="alinhamento-esquerda">${exame.nomeExame || exame.idTipoExame}</td>
            <td class="alinhamento-esquerda">${exame.observacoes || "---"}</td>
            <td>${formatarDataBR(exame.dataExame)}</td>
            <td>${exame.status || "---"}</td>
            <td>${exame.idLaudo && exame.idLaudo !== 0 ? exame.idLaudo : "---"}</td>
            <td>${exame.dataLaudo ? formatarDataBR(exame.dataLaudo) : "---"}</td>
            <td>
                <button class="btn-editar" data-id="${exame.idExame}" style="padding: 5px 15px;">Editar</button>
                ${isPronto && exame.idLaudo && exame.idLaudo !== 0
                ? `<button class="btn-laudo" data-id="${exame.idExame}" style="padding: 5px 15px;">Laudo</button>`
                : ""
            }
                ${podeExcluir
                ? `<button class="btn-excluir" data-id="${exame.idExame}" style="padding: 5px 15px;">Excluir</button>`
                : ""
            }
            </td>
        `;

        tbody.appendChild(tr);
    });

    // Eventos dos botões da tabela.
    tbody.querySelectorAll(".btn-editar").forEach(btn => {
        btn.addEventListener("click", () => {
            const id = btn.getAttribute("data-id");
            carregarExameParaEdicao(id);
        });
    });

    tbody.querySelectorAll(".btn-excluir").forEach(btn => {
        btn.addEventListener("click", () => {
            const id = btn.getAttribute("data-id");
            excluirExame(id);
        });
    });

    tbody.querySelectorAll(".btn-laudo").forEach(btn => {
        btn.addEventListener("click", () => {
            const id = btn.getAttribute("data-id");
            baixarLaudo(id);
        });
    });
}


// Seleciona o exame na lista examesCarregados, preenche o formulário de exame/laudo e exibe o formulário.
function carregarExameParaEdicao(idExame) {
    const exame = examesCarregados.find(e => String(e.idExame) === String(idExame));

    if (!exame) {
        alert("Não foi possível localizar os dados desse exame.");
        return;
    }

    idExameEmEdicao = exame.idExame;
    idPaciente.value = exame.idPaciente;
    idMedico.value = exame.idMedico;
    idTipoExame.value = exame.idTipoExame;
    dataExame.value = exame.dataExame || "";
    observacoes.value = exame.observacoes || "";
    statusLaudo.value = exame.status || "PENDENTE";
    idLaudo.value = exame.idLaudo && exame.idLaudo !== 0 ? exame.idLaudo : "";
    dataLaudo.value = exame.dataLaudo || "";

    // Limpa.
    if (arquivoInput) {
        arquivoInput.value = "";
    }
    if (nomeArquivo) {
        nomeArquivo.value = "Nenhum arquivo selecionado";
    }

    // Toggle.
    if (containerFormData) {
        containerFormData.style.display = "block";
    }
    if (btnToggle) {
        btnToggle.textContent = "Ocultar";
    }

    // Rola até o formulário.
    if (formExame) {
        formExame.scrollIntoView({ behavior: "smooth", block: "start" });
    }
}


// Gerenciamento de laudos (upload).
async function gravarLaudo(event) {
    event.preventDefault();

    if (!idExameEmEdicao) {
        alert("Selecione um exame na tabela (botão Editar) antes de gravar o laudo.");
        return;
    }

    if (!arquivoInput || arquivoInput.files.length === 0) {
        alert("Selecione um arquivo PDF para gravar o laudo.");
        return;
    }

    try {
        // Monta o objeto LaudoVO para enviar como JSON.
        const laudoVO = {
            idLaudo: parseInt(idLaudo.value || "0", 10),
            idExame: idExameEmEdicao,
            arquivo: null, // o Back preenche a partir do InputStream.
            dataLaudo: dataLaudo.value || null // se vazio, o BO coloca LocalDate.now().
        };

        const formData = new FormData();

        // arquivo PDF
        formData.append("file", arquivoInput.files[0]);

        // JSON do LaudoVO (mesmo nome do parâmetro no LaudoController: "laudoVO").
        const blobLaudo = new Blob([JSON.stringify(laudoVO)], {
            type: "application/json"
        });
        formData.append("laudoVO", blobLaudo);

        const response = await fetch(ENDPOINT_LAUDO_CADASTRAR, {
            method: "POST",
            body: formData
        });

        const laudoSalvo = await response.json(); // LaudoVO retornado.

        if (response.ok) {
            alert("Laudo gravado com sucesso para o exame " + laudoSalvo.idExame + ".");

            // Atualiza campos de tela com o retorno do Back.
            if (laudoSalvo.idLaudo !== undefined) {
                idLaudo.value = laudoSalvo.idLaudo;
            }
            if (laudoSalvo.dataLaudo) {
                dataLaudo.value = laudoSalvo.dataLaudo;
            }

            // Atualiza o status no formulário (visual).
            statusLaudo.value = "PRONTO";

            // limpa seleção de arquivo
            arquivoInput.value = "";
            nomeArquivo.value = "Nenhum arquivo selecionado";

            // Recarrega a lista da requisição, se estiver preenchida.
            if (inputRequisicao.value) {
                pesquisarRequisicao();
            }
        } else {
            alert("Erro ao gravar laudo.\n" + (laudoSalvo.mensagem || ""));
        }

    } catch (erro) {
        console.error("Erro ao gravar laudo:", erro);
        alert("Falha ao conectar com o servidor para gravar o laudo.");
    }
}


// Exclusão de exames.
function excluirExame(idExame) {
    if (!confirm(`Deseja realmente excluir o exame ID ${idExame}?`)) {
        return;
    }

    fetch(`${ENDPOINT_EXCLUIR_EXAME}/${idExame}`, {
        method: "DELETE"
    })
        .then(response => response.json().catch(() => ({})))
        .then(data => {
            if (data && data.mensagem) {
                alert(data.mensagem);
            } else {
                alert("Operação de exclusão concluída.");
            }

            if (inputRequisicao.value) {
                pesquisarRequisicao();
            } else {
                tbody.innerHTML = "";
            }
        })
        .catch(error => {
            console.error("Erro ao excluir exame:", error);
            alert("Não foi possível excluir o exame.");
        });
}


// Download do laudo :)
function baixarLaudo(idExame) {
    if (!idExame) {
        alert("Exame inválido para download do laudo.");
        return;
    }

    // Back já verifica se o exame está PRONTO e se existe laudo :D
    window.location.href = `${ENDPOINT_LAUDO_DOWNLOAD}/${idExame}`;
}


// Utilitários de tela (limpa a tela).
function limparTela() {
    inputRequisicao.value = "";
    tbody.innerHTML = "";
    limparFormulario();
    if (containerFormData) {
        containerFormData.style.display = "none";
    }
    if (btnToggle) {
        btnToggle.textContent = "Exibir";
    }
}


// Utilitários de tela (limpa o formulário).
function limparFormulario() {
    if (formExame) {
        formExame.reset();
    }
    idExameEmEdicao = null;
    statusLaudo.value = "PENDENTE";
    idLaudo.value = "";
    dataLaudo.value = "";
    if (arquivoInput) {
        arquivoInput.value = "";
    }
    if (nomeArquivo) {
        nomeArquivo.value = "Nenhum arquivo selecionado";
    }
}

// Utilitários de tela (mostra/oculta o bloco de cadastro de exame).
function toggleFormulario() {
    if (!containerFormData) return;

    const visivel = containerFormData.style.display === "block";

    containerFormData.style.display = visivel ? "none" : "block";

    if (btnToggle) {
        btnToggle.textContent = visivel ? "Exibir" : "Ocultar";
    }
}


// Converte "yyyy-MM-dd" -> "dd/MM/yyyy" para exibição em tabela.
function formatarDataBR(data) {
    if (!data) return "";
    if (typeof data === "string" && data.includes("-")) {
        const [ano, mes, dia] = data.split("-");
        return `${dia}/${mes}/${ano}`;
    }
    return data;
}
