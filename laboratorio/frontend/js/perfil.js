const usuario = JSON.parse(sessionStorage.getItem('usuario'));
//const teste = JSON.parse(sessionStorage.getItem(usuario.login));

// Se não existir usuário logado, volta pro login
if (!usuario) {
    alert("Você precisa estar logado para acessar esta página.");
    window.location.href = "../index.html";
}

// Pros links que voltam pra tela Principal
const linkPrincipal = document.getElementById('linkPrincipal');
const principal = document.getElementById('principal');

if (usuario.perfil === 'PACIENTE') {
    if (linkPrincipal) linkPrincipal.href = "../modules/paciente.html";
    if (principal) principal.href = "../modules/paciente.html";
} else if (usuario.perfil === 'MEDICO') {
    if (linkPrincipal) linkPrincipal.href = "../modules/medico.html";
    if (principal) principal.href = "../modules/medico.html";
} else {
    if (linkPrincipal) linkPrincipal.href = "../modules/funcionario.html";
    if (principal) principal.href = "../modules/funcionario.html";
}


// Pro form de Perfil...

const formulario = document.querySelector('[data-formulario]');
const grupoNascimento = document.getElementById('grupoNascimento');
const grupoEspecialidade = document.getElementById('grupoEspecialidade');
const grupoCRM = document.getElementById('grupoCRM');

// Põe as infos nos campos básicos
if (document.getElementById('nome')) {
    document.getElementById('nome').value = usuario.nome || "";
    document.getElementById('cpf').value = usuario.cpf || "";
    document.getElementById('email').value = usuario.email || "";
    document.getElementById('perfil').value = usuario.perfil || "";
    document.getElementById('login').value = usuario.login || "";
    document.getElementById('senha').value = usuario.senha || "";
}

// FUNÇÃO
function ajustarCamposPorPerfil() {
    grupoNascimento.classList.add('oculto');
    grupoEspecialidade.classList.add('oculto');
    grupoCRM.classList.add('oculto');

    if (usuario.perfil === 'PACIENTE') {
        grupoNascimento.classList.remove('oculto');
        document.getElementById('datanascimento').value = usuario.datanascimento || "dd";
    } else if (usuario.perfil === 'MEDICO') {
        grupoEspecialidade.classList.remove('oculto');
        grupoCRM.classList.remove('oculto');
        document.getElementById('especialidade').value = usuario.especialidade || "ee";
        document.getElementById('crm').value = usuario.crm || "cc";
    }
}
ajustarCamposPorPerfil();


// Pra atualização do user...

// Envio do formulário para atualizar dados
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
        }

        await atualizarUsuario(usuarioAtualizado);
    });
}

// FUNÇÃO
async function atualizarUsuario(usuarioAtualizado) {
    let url;

    if (usuarioAtualizado.perfil === 'PACIENTE') {
        url = 'http://localhost:8080/laboratorio/rest/paciente/atualizar';
    } else if (usuarioAtualizado.perfil === 'MEDICO') {
        url = 'http://localhost:8080/laboratorio/rest/medico/atualizar';
    } else {
        url = 'http://localhost:8080/laboratorio/rest/usuario/atualizar';
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
        alert("Erro de conexão ao atualizar os dados.");
    }
}
