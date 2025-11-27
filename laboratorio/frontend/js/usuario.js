// Verificação de sessão e controle de acesso.
const usuario = JSON.parse(sessionStorage.getItem('usuario'));

if (!usuario) {
    alert("Você precisa estar logado para acessar esta página.");
    window.location.href = "../index.html";
}


// Seleção de elementos (links que voltam pra tela principal).
const linkPrincipal = document.getElementById('linkPrincipal');
const principal = document.getElementById('principal');


// Ajuste dinâmico da navegação.
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
