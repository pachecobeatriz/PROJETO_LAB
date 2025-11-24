package controller;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.bo.ExameBO;
import model.bo.MedicoBO;
import model.dto.RequisicaoExamesDTO;
import model.vo.MedicoVO;

@Path("/medico")
public class MedicoController {

	@POST
	@Path("/cadastrar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MedicoVO cadastrar(MedicoVO medicoVO) {
		MedicoBO medicoBO = new MedicoBO();
		return medicoBO.cadastrar(medicoVO);
	}

	@PUT // Verbo ideal para atualizar um recurso
	@Path("/atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizar(MedicoVO medicoVO) {
		MedicoBO medicoBO = new MedicoBO();
		boolean sucesso = medicoBO.atualizar(medicoVO);

		if (sucesso) {
			// Retorna Status 200 OK
			return Response.ok("Médico atualizado com sucesso.").build();
		} else {
			// Retorna Status 400 Bad Request (se falhar o update ou a transação)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Falha ao atualizar o médico. Verifique o ID e os dados.").build();
		}
	}

	// ~ NOVAS ADIÇÕES - Sandro ~

	// GET: listar requisições dos pacientes de um médico
	@GET
	@Path("/requisicao/{idMedico}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarRequisicoesPorMedico(@PathParam("idMedico") int idMedico) {

		ExameBO exameBO = new ExameBO();
		List<RequisicaoExamesDTO> lista = exameBO.listarRequisicoesPorMedico(idMedico);

		if (lista == null) {
			lista = java.util.Collections.emptyList();
		}

		return Response.ok(lista).build();
	}

	// Vai trazer o id do médico para corrigir CRM e Especialidade no permil Médico,
	// explicar para Beatriz!
	@GET
	@Path("/{idUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	public MedicoVO buscarPorId(@PathParam("idUsuario") int idUsuario) {
		MedicoBO medicoBO = new MedicoBO();
		return medicoBO.buscarPorId(idUsuario);
	}

}
