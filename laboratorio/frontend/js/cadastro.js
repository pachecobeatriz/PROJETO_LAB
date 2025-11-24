const formulario = document.querySelector('[data-formulario]');

const perfilSelecionado = document.getElementById('perfil');
const grupoNascimento = document.getElementById('grupoNascimento');
const grupoEspecialidade = document.getElementById('grupoEspecialidade');
const grupoCRM = document.getElementById('grupoCRM');

perfilSelecionado.value = "";


// EventListener
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
console.log("passou por perfilSelecionado.addEventListener") // teste


// EventListener
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
console.log("passou por formulario.addEventListener") // teste


// FUNÇÃO de CADASTRO
async function realizarCadastro(usuario) {
    let url;
    let corpo;
    console.log("entrou em async function realizarCadastro") // teste

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
        }); console.log("passou por if do paciente") // teste
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
        }); console.log("passou por if do medico") // teste
    }

    let options = {
        method: 'POST',
        headers: { 'Content-type': 'application/json' },
        body: corpo
    }
    console.log("passou por let options") // teste

    const usuarioJson = await fetch(url, options);
    console.log("passou por usuarioJson") // teste
    const usuarioCadastrado = await usuarioJson.json();
    console.log("passou por usuarioCadastrado") // teste

    console.log("Enviando para o backend:", corpo); // teste
    console.log("RESPOSTA DO BACK:", usuarioCadastrado); // teste

    if (usuarioCadastrado.idUsuario != 0) {
        console.log("entra no IF") // teste
        alert("Usuário cadastrado com sucesso!");
        formulario.reset();
        window.location.href = '../index.html';
    } else {
        alert("Problema ao cadastrar o Usuário.");
    }
}
