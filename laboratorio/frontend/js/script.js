const formulario = document.querySelector('[data-formulario]');

// Tratamento da Submissão
formulario.addEventListener('submit', (evento) => {
    evento.preventDefault();

    let usuario = {
        "login": document.getElementById("login").value,
        "senha": document.getElementById("senha").value,
    }

    realizarLogin(usuario);
});

// Realização do Login e Redirecionamento
async function realizarLogin(usuario) {

    let options = {
        method: "POST",
        headers: { "Content-type": "application/json" }, // o "@Consumes(MediaType.APPLICATION_JSON)" do BACK bate aqui.
        body: JSON.stringify({
            login: usuario.login,
            senha: usuario.senha
        })
    };

    const usuarioJson = await fetch('http://localhost:8080/laboratorio/rest/usuario/logar', options); // FETCH tem 2 parâmetros; o primeiro é obrigatório e representa a URL, o segundo é opcional.

    const usuarioLogado = await usuarioJson.json(); // pega o objeto JSON e transforma em objeto JS.

    if (usuarioLogado.idUsuario != 0) {
        sessionStorage.setItem('usuario', JSON.stringify(usuarioLogado));

        if (usuarioLogado.perfil == "PACIENTE") {
            window.location.href = './modules/paciente.html'; // redirecionamento pra uma próxima página.
        } else if (usuarioLogado.perfil == "MEDICO") {
            window.location.href = './modules/medico.html';
        } else {
            window.location.href = './modules/funcionario.html';
        }
    } else {
        alert("Login ou Senha incorretos.");
        formulario.reset();
    }

}
