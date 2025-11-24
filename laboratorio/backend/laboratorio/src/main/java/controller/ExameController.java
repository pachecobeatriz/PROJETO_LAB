package controller;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.bo.ExameBO;
import model.dto.RequisicaoExamesDTO;
import model.vo.ExameVO;
import model.vo.PacienteVO;

@Path("/exame")
public class ExameController {

	@POST
	@Path("/cadastrar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cadastrar(ExameVO exameVO) {
		ExameBO exameBO = new ExameBO();
		ExameVO novoExame = exameBO.cadastrar(exameVO);

		if (novoExame != null && novoExame.getIdExame() > 0) {
			// Retorna Status 201 Created com o objeto recém-criado
			return Response.status(Response.Status.CREATED).entity(novoExame).build();
		} else {
			// Retorna Status 400 Bad Request
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Falha ao cadastrar exame. Verifique os dados fornecidos.").build();
		}
	}

	@PUT
	@Path("/atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizar(ExameVO exameVO) {
		ExameBO exameBO = new ExameBO();
		boolean sucesso = exameBO.atualizar(exameVO);

		if (sucesso) {
			// Retorna Status 200 OK
			return Response.ok("Exame ID " + exameVO.getIdExame() + " atualizado com sucesso.").build();
		} else {
			// Retorna Status 400 Bad Request
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Falha ao atualizar o exame. Verifique o ID e os dados.").build();
		}
	}

	@DELETE // Verbo HTTP para exclusão
	@Path("/excluir/{id}")
	@Produces(MediaType.APPLICATION_JSON) // Retorna mensagem em JSON
	public Response excluir(@PathParam("id") int idExame) {
		ExameBO exameBO = new ExameBO();
		String resultado = exameBO.excluir(idExame);

		switch (resultado) {
		case "SUCESSO":
			// 200 OK ou 204 No Content (melhor para exclusão, mas usaremos 200 com
			// mensagem)
			return Response.ok("Exame excluído com sucesso.").build();

		case "NAO_ENCONTRADO":
			// 404 Not Found
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Erro: Exame com ID " + idExame + " não foi encontrado.").build();

		case "LAUDO_EXISTENTE":
			// 400 Bad Request (Violação de Regra de Negócio)
			return Response.status(Response.Status.BAD_REQUEST).entity(
					"Atenção! Exames com status diferente de PENDENTE (indicando laudos cadastrados) não podem ser excluídos.")
					.build();

		default:
			// 500 Internal Server Error (para FALHA_DB)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Erro interno ao tentar excluir o exame.").build();
		}
	}

	@GET
	@Path("/requsicoes/paciente/{idPaciente}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarRequisicoesPorPaciente(@PathParam("idPaciente") int idPaciente) {
		ExameBO exameBO = new ExameBO();

		// Cria um PacienteVO apenas com o ID para passar ao BO
		PacienteVO pacienteVO = new PacienteVO();
		pacienteVO.setIdUsuario(idPaciente);

		List<RequisicaoExamesDTO> listaRequisicoes = exameBO.listarRequisicoesPorPaciente(pacienteVO);

		if (listaRequisicoes != null && !listaRequisicoes.isEmpty()) {
			// Retorna Status 200 OK com a lista
			return Response.ok(listaRequisicoes).build();
		} else {
			// Retorna 404 Not Found se não houver requisições
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Nenhuma requisição encontrada para o paciente ID " + idPaciente + ".").build();
		}
	}

}
