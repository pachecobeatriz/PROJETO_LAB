const usuario = JSON.parse(sessionStorage.getItem('usuario'));

// Pra conferir se tem usuário logado | Pode tirar pra ñ ter que conectar o Back.
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
