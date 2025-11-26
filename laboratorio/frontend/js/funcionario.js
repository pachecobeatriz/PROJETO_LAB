// Constante para os endpoints REST
const ENDPOINT_BASE = "http://localhost:8080/laboratorio/rest/exame/listarPorRequisicao";
//const ENDPOINT_DOWNLOAD_LAUDO = "http://localhost:8080/laboratorio/rest/laudo/download";
const ENDPOINT_EXCLUIR_EXAME = "http://localhost:8080/laboratorio/rest/exame/excluir";

// Formulário completo
const form = document.getElementById("formExame");
const idPaciente = document.getElementById("idPaciente");
const idMedico = document.getElementById("idMedico");
const idTipoExame = document.getElementById("idTipoExame");
const dataExame = document.getElementById("dataExame");
const observacoes = document.getElementById("observacoes");
const statusLaudo = document.getElementById("statusLaudo");
const idLaudo = document.getElementById("idLaudo");
const dataLaudo = document.getElementById("dataLaudo");
const arquivo = document.getElementById("arquivo");

const arquivoInput = document.getElementById("arquivo");
const nomeArquivo = document.getElementById("nome-arquivo");



btnToggle.addEventListener("click", toggleFormulario);

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


// ~~~~~ CADASTRO 1 ~~~~~  
/*
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

*/


// ~~~~~ CADASTRO 2 ~~~~~  
/*
arquivoInput.addEventListener("change", () => {
    nomeArquivo.textContent =
        arquivoInput.files.length > 0 ? arquivoInput.files[0].name : "Nenhum arquivo selecionado";
});

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
});
*/


// ~~~~~ CADASTRO 3 ~~~~~  

formExame.addEventListener("submit", gravarExame);

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
        statusLaudo: statusLaudo.value
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





/**
 * Realiza a requisição ao endpoint para listar exames por número de requisição e popula a tabela.
 * @param {number} numeroPedido O número da requisição digitado pelo usuário.
 */
function listarExamesPorRequisicao(numeroPedido) {
    const url = `${ENDPOINT_BASE}/${numeroPedido}`;
    const tbody = document.querySelector('table tbody');

    // Limpa o corpo da tabela antes de iniciar uma nova busca
    tbody.innerHTML = '';

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro na requisição: ${response.status} ${response.statusText}`);
            }
            return response.json();
        })
        .then(listaExames => {
            if (listaExames.length === 0) {
                const tr = document.createElement('tr');
                tr.innerHTML = `<td colspan="10">Nenhum exame encontrado para a requisição **${numeroPedido}**.</td>`;
                tbody.appendChild(tr);
                return;
            }

            listaExames.forEach(exame => {
                //const tbody = document.getElementById("tbody");
                const tr = document.createElement('tr');
                const statusFormatado = exame.status ? exame.status.replace('_', ' ') : '';
                const dataLaudo = exame.dataLaudo ? exame.dataLaudo : '';

                // --- LÓGICA DINÂMICA PARA OS BOTÕES DE AÇÃO ---
                let botoesAcoes = `
                    <button style="padding: 5px 15px;" class="botao-tabela-editar" data-id="${exame.idExame}">Editar</button>
                `;

                if (exame.status === 'PRONTO') {
                    // Botão Laudo: Apenas se o status for PRONTO
                    botoesAcoes += `
                        <button style="padding: 5px 15px;" class="botao-tabela-laudo" data-id="${exame.idExame}" data-idlaudo="${exame.idLaudo}">Laudo</button>
                    `;
                } else if (exame.status === 'PENDENTE') {
                    // Botão Excluir: Apenas se o status for PENDENTE
                    botoesAcoes += `
                        <button style="padding: 5px 15px;" class="botao-tabela-excluir" data-id="${exame.idExame}">Excluir</button>
                    `;
                }

                tr.innerHTML = `
                    <td>${exame.idExame}</td>
                    <td>${exame.paciente || ''}</td>
                    <td>${exame.medico || ''}</td>
                    <td>${exame.nomeExame || ''}</td>
                    <td>${exame.observacoes || ''}</td>
                    <td>${exame.dataExame || ''}</td>
                    <td>${statusFormatado}</td>
                    <td>${exame.idLaudo > 0 ? exame.idLaudo : ''}</td>
                    <td>${dataLaudo}</td>
                    <td>${botoesAcoes}</td>
                `;

                tbody.appendChild(tr);
            });

            // Adiciona os event listeners após a tabela ser populada
            adicionarListenersAcoes();

        })
        .catch(error => {
            console.error("Erro ao listar exames:", error);
            /* const tr = document.createElement('tr');
            tr.innerHTML = `<td colspan="10" style="color: red;">Erro ao carregar dados. Verifique o console.</td>`;
            tbody.appendChild(tr); */
        });
}

// ... (Mantenha o Listener para o botão 'Pesquisar' e 'Limpar' aqui)
// ...
// Listener para o botão "Pesquisar"
document.addEventListener('DOMContentLoaded', () => {
    const btnPesquisar = document.getElementById('btnPesquisar');
    const inputRequisicao = document.getElementById('idRequisicao');

    if (btnPesquisar && inputRequisicao) {
        btnPesquisar.addEventListener('click', (event) => {
            event.preventDefault(); // Evita o comportamento padrão do botão, se estiver em um form

            const numeroPedido = parseInt(inputRequisicao.value.trim());

            if (isNaN(numeroPedido) || numeroPedido <= 0) {
                alert("Por favor, digite um número de requisição válido.");
                // Limpa a tabela, se houver algo
                document.querySelector('table tbody').innerHTML = '';
                return;
            }

            listarExamesPorRequisicao(numeroPedido);
        });
    } else {
        console.error("Elementos 'btnPesquisar' ou 'idRequisicao' não encontrados.");
    }

    // Opcional: Adicionar funcionalidade ao botão Limpar (se desejar)
    const btnLimpar = document.getElementById('btnLimpar');
    if (btnLimpar) {
        btnLimpar.addEventListener('click', () => {
            inputRequisicao.value = '';
            document.querySelector('table tbody').innerHTML = '';
        });
    }
});

function adicionarListenersAcoes() {
    // 1. Botão Editar
    document.querySelectorAll('.botao-tabela-editar').forEach(button => {
        button.addEventListener('click', (e) => {
            const idExame = e.target.getAttribute('data-id');
            carregarExameParaEdicao(idExame);
        });
    });

    // 2. Botão Laudo (PRONTO)
    document.querySelectorAll('.botao-tabela-laudo').forEach(button => {
        button.addEventListener('click', (e) => {
            const idExame = e.target.getAttribute('data-id');
            fazerDownloadLaudo(idExame);
        });
    });

    // 3. Botão Excluir (PENDENTE)
    document.querySelectorAll('.botao-tabela-excluir').forEach(button => {
        button.addEventListener('click', (e) => {
            const idExame = e.target.getAttribute('data-id');
            if (confirm(`Tem certeza que deseja excluir o Exame ID ${idExame}?`)) {
                excluirExame(idExame);
            }
        });
    });
}





/**
 * Carrega os dados de um exame da tabela para o formulário de cadastro/edição.
 * @param {string} idExame O ID do exame a ser carregado.
 */
function carregarExameParaEdicao(idExame) {

    document.getElementById('idPaciente').value = idExame.idPaciente || "";
    document.getElementById('idMedico').value = idExame.idMedico || "";
    document.getElementById('idTipoExame').value = idExame.idTipoExame || "";
    document.getElementById('dataExame').value = idExame.dataExame || "";
    document.getElementById('observacoes').value = idExame.observacoes || "hello";
    document.getElementById('statusLaudo').value = idExame.statusLaudo || "";
    document.getElementById('dataLaudo').value = idExame.dataLaudo || "";
    document.getElementById('arquivo').value = idExame.arquivo || "";

    btnToggle.textContent = "Ocultar";
    formExame.parentElement.style.display = "block";
}





/**
 * Envia uma requisição DELETE para excluir o exame.
 * @param {string} idExame O ID do exame a ser excluído.
 */
function excluirExame(idExame) {
    const urlDelete = `${ENDPOINT_EXCLUIR_EXAME}/${idExame}`;

    fetch(urlDelete, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
            // Adicione aqui outros headers necessários, como Tokens de Autorização
        }
    })
        .then(response => {
            if (response.ok) {
                alert(`Exame ID ${idExame} excluído com sucesso.`);

                // Recarrega a lista para remover o item excluído da tabela
                const numeroPedido = document.getElementById('idRequisicao').value.trim();
                if (numeroPedido) {
                    listarExamesPorRequisicao(parseInt(numeroPedido));
                } else {
                    // Se a requisição não estiver preenchida, apenas limpa a tabela
                    document.querySelector('table tbody').innerHTML = '';
                }
            } else {
                // Tenta ler o corpo da resposta para uma mensagem de erro mais detalhada
                return response.text().then(text => {
                    throw new Error(`Falha ao excluir exame: ${response.status} - ${text || 'Erro desconhecido.'}`);
                });
            }
        })
        .catch(error => {
            console.error("Erro ao excluir exame:", error);
            alert(`Não foi possível excluir o exame: ${error.message}`);
        });
}
