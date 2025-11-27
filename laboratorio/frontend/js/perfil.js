// Inicialização e seleção de elementos.
const formulario = document.querySelector('[data-formulario]');
const grupoNascimento = document.getElementById('grupoNascimento');
const grupoEspecialidade = document.getElementById('grupoEspecialidade');
const grupoCRM = document.getElementById('grupoCRM');
const grupoMatricula = document.getElementById('grupoMatricula');
const grupoCargo = document.getElementById('grupoCargo');


// Preenchimento inicial dos campos comuns.
if (document.getElementById('nome')) {
    document.getElementById('nome').value = usuario.nome || "";
    document.getElementById('cpf').value = usuario.cpf || "";
    document.getElementById('email').value = usuario.email || "";
    document.getElementById('perfil').value = usuario.perfil || "";
    document.getElementById('login').value = usuario.login || "";
    document.getElementById('senha').value = usuario.senha || "";
}


// Ajuste e carregamento de campos específicos.
function ajustarCamposPorPerfil() {
    grupoNascimento.classList.add('oculto');
    grupoEspecialidade.classList.add('oculto');
    grupoCRM.classList.add('oculto');
    grupoMatricula.classList.add('oculto');
    grupoCargo.classList.add('oculto');

    if (usuario.perfil === 'PACIENTE') {
        grupoNascimento.classList.remove('oculto');
        const campoData = document.getElementById('datanascimento');

        if (usuario.dataNascimento) {
            // se já veio do Back no login
            campoData.value = formatarDataInput(usuario.dataNascimento);
        } else {
            // se não veio no login, daí busca no Back
            carregarDadosPaciente();
        }

    } else if (usuario.perfil === 'MEDICO') {
        grupoEspecialidade.classList.remove('oculto');
        grupoCRM.classList.remove('oculto');

        if (usuario.especialidade || usuario.crm) {
            // se já veio do Back no login
            document.getElementById('especialidade').value = usuario.especialidade || "";
            document.getElementById('crm').value = usuario.crm || "";
        } else {
            // se não veio no login, daí busca no Back
            carregarDadosMedico();
        }

    } else if (usuario.perfil === 'FUNCIONARIO') {

        if (usuario.matricula || usuario.cargo) {
            // se já veio do Back no login
            document.getElementById('matricula').value = usuario.matricula || "";
            document.getElementById('cargo').value = usuario.cargo || "";
        } else {
            // se não veio no login, daí busca no Back
            carregarDadosFuncionario();
        }
    }

}
ajustarCamposPorPerfil();


// Atualização de perfil (submissão do formulário).
if (formulario) {
    formulario.addEventListener('submit', async (evento) => {
        evento.preventDefault();

        let usuarioAtualizado = {
            idUsuario: usuario.idUsuario,
            nome: document.getElementById('nome').value,
            cpf: document.getElementById('cpf').value,
            email: document.getElementById('email').value,
            perfil: document.getElementById('perfil').value,
            login: document.getElementById('login').value,
            senha: document.getElementById('senha').value
        };

        if (usuario.perfil === 'PACIENTE') {
            usuarioAtualizado.dataNascimento = document.getElementById('datanascimento').value;
        } else if (usuario.perfil === 'MEDICO') {
            usuarioAtualizado.especialidade = document.getElementById('especialidade').value;
            usuarioAtualizado.crm = document.getElementById('crm').value;
        } else if (usuario.perfil === 'FUNCIONARIO') {
            usuarioAtualizado.matricula = document.getElementById('matricula').value;
            usuarioAtualizado.cargo = document.getElementById('cargo').value;
        }

        await atualizarUsuario(usuarioAtualizado);
    });
}

// Determina a URL de PUT correta.
async function atualizarUsuario(usuarioAtualizado) {
    let url;

    if (usuarioAtualizado.perfil === 'PACIENTE') {
        url = 'http://localhost:8080/laboratorio/rest/paciente/atualizar';
    } else if (usuarioAtualizado.perfil === 'MEDICO') {
        url = 'http://localhost:8080/laboratorio/rest/medico/atualizar';
    } else {
        url = 'http://localhost:8080/laboratorio/rest/funcionario/atualizar';
    }

    const options = {
        method: 'PUT',
        headers: { 'Content-type': 'application/json' },
        body: JSON.stringify(usuarioAtualizado)
    };

    try {
        const resposta = await fetch(url, options);
        const resultado = await resposta.json();

        if (resultado.idUsuario) {
            alert("Dados atualizados com sucesso!");
            sessionStorage.setItem('usuario', JSON.stringify(resultado));
        } else {
            alert("Erro ao atualizar os dados.");
        }
    } catch (erro) {
        console.error("Erro ao atualizar:", erro);
        //alert("Erro de conexão ao atualizar os dados.");
    }
}

// Compõe o ajustarCamposPorPerfil (Paciente).
async function carregarDadosPaciente() {
    try {
        const resposta = await fetch(`http://localhost:8080/laboratorio/rest/paciente/${usuario.idUsuario}`);

        if (!resposta.ok) {
            console.error("Erro ao buscar dados do paciente:", resposta.status);
            return;
        }

        const paciente = await resposta.json();

        if (paciente) {
            // Atualiza o objeto usuario em memória e no sessionStorage
            usuario.dataNascimento = paciente.dataNascimento;
            sessionStorage.setItem('usuario', JSON.stringify(usuario));

            const campoData = document.getElementById('datanascimento');
            if (campoData) {
                campoData.value = formatarDataInput(paciente.dataNascimento);
            }
        }
    } catch (erro) {
        console.error("Erro de conexão ao buscar dados do paciente:", erro);
    }
}

// Compõe o ajustarCamposPorPerfil (Medico).
async function carregarDadosMedico() {
    try {
        const resposta = await fetch(`http://localhost:8080/laboratorio/rest/medico/${usuario.idUsuario}`);

        if (!resposta.ok) {
            console.error("Erro ao buscar dados do médico:", resposta.status);
            return;
        }

        const medico = await resposta.json();

        if (medico) {
            // atualiza o objeto em memória
            usuario.especialidade = medico.especialidade;
            usuario.crm = medico.crm;

            // salva atualizado na sessão
            sessionStorage.setItem('usuario', JSON.stringify(usuario));

            document.getElementById('especialidade').value = medico.especialidade || "";
            document.getElementById('crm').value = medico.crm || "";
        }
    } catch (erro) {
        console.error("Erro de conexão ao buscar dados do médico:", erro);
    }
}

// Compõe o ajustarCamposPorPerfil (Funcionario).
async function carregarDadosFuncionario() {
    try {
        const resposta = await fetch(`http://localhost:8080/laboratorio/rest/funcionario/${usuario.idUsuario}`);

        if (!resposta.ok) {
            console.error("Erro ao buscar dados do funcionário:", resposta.status);
            return;
        }

        const funcionario = await resposta.json();

        if (funcionario) {
            // atualiza o objeto em memória
            usuario.matricula = funcionario.matricula;
            usuario.cargo = funcionario.cargo;

            // salva atualizado na sessão
            sessionStorage.setItem('usuario', JSON.stringify(usuario));

            document.getElementById('matricula').value = funcionario.matricula || "";
            document.getElementById('cargo').value = funcionario.cargo || "";
        }
    } catch (erro) {
        console.error("Erro de conexão ao buscar dados do funcionário:", erro);
    }
}

// Formatador de datas.
function formatarDataInput(data) {
    if (!data) return "";
    // Se vier como string "yyyy-MM-dd" -> já serve pro input date
    if (typeof data === "string") {
        if (/^\d{4}-\d{2}-\d{2}$/.test(data)) {
            return data;
        }
        // Se vier "dd/MM/yyyy"
        if (/^\d{2}\/\d{2}\/\d{4}$/.test(data)) {
            const [dia, mes, ano] = data.split("/");
            return `${ano}-${mes}-${dia}`;
        }
        return data; // fallback
    }
    // Se vier como array [ano, mes, dia] (caso Jackson invente moda)
    if (Array.isArray(data)) {
        const [ano, mes, dia] = data;
        return `${ano}-${String(mes).padStart(2, "0")}-${String(dia).padStart(2, "0")}`;
    }
    // Se vier objeto {year, monthValue, dayOfMonth}
    if (typeof data === "object") {
        const ano = data.year ?? data.ano;
        const mes = data.monthValue ?? data.month ?? data.mes;
        const dia = data.dayOfMonth ?? data.day ?? data.dia;

        if (ano && mes && dia) {
            return `${ano}-${String(mes).padStart(2, "0")}-${String(dia).padStart(2, "0")}`;
        }
    }
    return "";
}
