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
import model.bo.PacienteBO;
import model.dto.RequisicaoExamesDTO;
import model.vo.PacienteVO;

@Path("/paciente")
public class PacienteController {

	@POST
	@Path("/cadastrar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PacienteVO cadastrar(PacienteVO pacienteVO) {
		PacienteBO pacienteBO = new PacienteBO();
		return pacienteBO.cadastrar(pacienteVO);
	}

	// PUT com VO
	@PUT
	@Path("/atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PacienteVO atualizar(PacienteVO pacienteVO) {
		PacienteBO pacienteBO = new PacienteBO();

		PacienteVO pacienteAtualizado = pacienteBO.atualizar(pacienteVO);

		return pacienteAtualizado;
	}

	@GET
	@Path("/{idUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	public PacienteVO buscarPorId(@PathParam("idUsuario") int idUsuario) {
		PacienteBO pacienteBO = new PacienteBO();
		return pacienteBO.buscarPorId(idUsuario);
	}

	@GET
	@Path("/requisicao/{idPaciente}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarRequisicoesPorPaciente(@PathParam("idPaciente") int idPaciente) {

		ExameBO exameBO = new ExameBO();
		List<RequisicaoExamesDTO> lista = exameBO.listarRequisicoesPorPaciente(idPaciente);

		if (lista == null) {
			return Response.ok().entity(List.of()).build();
		}

		return Response.ok(lista).build();
	}

//	// PUT com RESPONSE
//	@PUT
//	@Path("/atualizar")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response atualizar(PacienteVO pacienteVO) {
//		PacienteBO pacienteBO = new PacienteBO();
//		boolean sucesso = pacienteBO.atualizar(pacienteVO);
//		if (sucesso) {
//			return Response.ok("Paciente atualizado com sucesso.").build();
//		} else {
//			return Response.status(Response.Status.BAD_REQUEST).entity("Falha ao atualizar o paciente.").build();
//		}
//	}

}
