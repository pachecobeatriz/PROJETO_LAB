// Seleção e inicialização de elementos
const formulario = document.querySelector('[data-formulario]');

const perfilSelecionado = document.getElementById('perfil');
const grupoNascimento = document.getElementById('grupoNascimento');
const grupoEspecialidade = document.getElementById('grupoEspecialidade');
const grupoCRM = document.getElementById('grupoCRM');

perfilSelecionado.value = "";


// Controle de visibilidade dos campos
perfilSelecionado.addEventListener('change', () => {
    grupoNascimento.classList.add('oculto');
    grupoEspecialidade.classList.add('oculto');
    grupoCRM.classList.add('oculto');

    if (perfilSelecionado.value === 'PACIENTE') {
        grupoNascimento.classList.remove('oculto');
    } else if (perfilSelecionado.value === 'MEDICO') {
        grupoEspecialidade.classList.remove('oculto');
        grupoCRM.classList.remove('oculto');
    }
});


// Tratamento da submissão e coleta de dados
formulario.addEventListener('submit', (evento) => {
    evento.preventDefault();

    let usuario = {
        'nome': document.getElementById('nome').value,
        'cpf': document.getElementById('cpf').value,
        'email': document.getElementById('email').value,
        'perfil': document.getElementById('perfil').value,
        'login': document.getElementById('login').value,
        'senha': document.getElementById('senha').value
    }
    if (document.getElementById('perfil').value === 'PACIENTE') {
        usuario.dataNascimento = document.getElementById('datanascimento').value;
    } else {
        usuario.especialidade = document.getElementById('especialidade').value;
        usuario.crm = document.getElementById('crm').value;
    }

    realizarCadastro(usuario);
});


// Envio e conclusão do cadastro
async function realizarCadastro(usuario) {
    let url;
    let corpo;

    if (usuario.perfil === 'PACIENTE') {
        url = 'http://localhost:8080/laboratorio/rest/paciente/cadastrar';
        corpo = JSON.stringify({
            nome: usuario.nome,
            cpf: usuario.cpf,
            email: usuario.email,
            perfil: usuario.perfil,
            dataNascimento: usuario.dataNascimento,
            login: usuario.login,
            senha: usuario.senha
        });
    } else if (usuario.perfil === 'MEDICO') {
        url = 'http://localhost:8080/laboratorio/rest/medico/cadastrar';
        corpo = JSON.stringify({
            nome: usuario.nome,
            cpf: usuario.cpf,
            email: usuario.email,
            perfil: usuario.perfil,
            especialidade: usuario.especialidade,
            crm: usuario.crm,
            login: usuario.login,
            senha: usuario.senha
        });
    }

    let options = {
        method: 'POST',
        headers: { 'Content-type': 'application/json' },
        body: corpo
    }

    const usuarioJson = await fetch(url, options);
    const usuarioCadastrado = await usuarioJson.json();

    console.log("Enviando para o backend:", corpo); // teste
    console.log("RESPOSTA DO BACK:", usuarioCadastrado); // teste

    if (usuarioCadastrado.idUsuario != 0) {
        alert("Usuário cadastrado com sucesso!");
        formulario.reset();
        window.location.href = '../index.html';
    } else {
        alert("Problema ao cadastrar o Usuário.");
    }
}
