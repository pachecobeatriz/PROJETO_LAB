function preencherFormPraEdicao() {
    document.getElementById('idPaciente').value = exame.idPaciente || "";
    document.getElementById('idMedico').value = exame.idMedico || "";
    document.getElementById('idTipoExame').value = exame.idTipoExame || "";
    document.getElementById('dataExame').value = exame.dataExame || "";
    document.getElementById('observacoes').value = exame.observacoes || "";
    document.getElementById('statusLaudo').value = exame.statusLaudo || "";
    document.getElementById('dataLaudo').value = exame.dataLaudo || "";
    document.getElementById('arquivo').value = exame.arquivo || "";
}






async function carregarDadosExame() {
    try {
        const resposta = await fetch(`http://localhost:8080/laboratorio/rest/exame/${idExame}`);

        if (!resposta.ok) {
            console.error("Erro ao buscar dados do exame:", resposta.status);
            return;
        }
    } catch (erro) {
        console.error("Erro de conexão ao buscar dados do exame:", erro);
    }
}





fetch(`http://localhost:8080/laboratorio/rest/exame/listar/${idExame}`)
        .then(resp => resp.json())
        .then(exame => {
            idPaciente.value = exame.idPaciente;
            idMedico.value = exame.idMedico;
            idTipoExame.value = exame.idTipoExame;
            dataExame.value = exame.dataExame;
            observacoes.value = exame.observacoes;
            statusLaudo.value = exame.statusLaudo;
            idLaudo.value = exame.idLaudo;
            dataLaudo.value = exame.dataLaudo;
        });