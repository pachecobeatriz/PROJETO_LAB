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
import model.dto.ExameDTO;
import model.vo.ExameVO;

@Path("/exame")
public class ExameController {

	// POST com VO
	@POST
	@Path("/cadastrar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ExameVO cadastrar(ExameVO exameVO) {
		ExameBO exameBO = new ExameBO();
		return exameBO.cadastrar(exameVO);
	}

	@PUT
	@Path("/atualizar/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizar(ExameVO exameVO) {
		ExameBO exameBO = new ExameBO();
		boolean sucesso = exameBO.atualizar(exameVO);

		if (sucesso) {
			return Response.ok("Exame ID " + exameVO.getIdExame() + " atualizado com sucesso.").build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Falha ao atualizar o exame. Verifique o ID e os dados.").build();
		}
	}

	@DELETE
	@Path("/excluir/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response excluir(@PathParam("id") int idExame) {
		ExameBO exameBO = new ExameBO();
		String resultado = exameBO.excluir(idExame);

		switch (resultado) {
		case "SUCESSO":
			return Response.ok("Exame excluído com sucesso.").build();

		case "NAO_ENCONTRADO":
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Erro: Exame com ID " + idExame + " não foi encontrado.").build();

		case "LAUDO_EXISTENTE":
			return Response.status(Response.Status.BAD_REQUEST).entity(
					"Atenção! Exames com status diferente de PENDENTE (indicando laudos cadastrados) não podem ser excluídos.")
					.build();

		default:
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Erro interno ao tentar excluir o exame.").build();
		}
	}

	@GET
	@Path("/listarPorRequisicao/{numeroPedido}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarPorRequisicao(@PathParam("numeroPedido") int numeroPedido) {

		ExameBO exameBO = new ExameBO();
		List<ExameDTO> lista = exameBO.listarPorRequisicao(numeroPedido);

		if (lista == null) {
			lista = java.util.Collections.emptyList();
		}

		return Response.ok(lista).build();
	}

//	// POST com RESPONSE
//	@POST
//	@Path("/cadastrar")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response cadastrar(ExameVO exameVO) {
//		ExameBO exameBO = new ExameBO();
//		ExameVO novoExame = exameBO.cadastrar(exameVO);
//
//		if (novoExame != null && novoExame.getIdExame() > 0) {
//			return Response.status(Response.Status.CREATED).entity(novoExame).build();
//		} else {
//			return Response.status(Response.Status.BAD_REQUEST)
//					.entity("Falha ao cadastrar exame. Verifique os dados fornecidos.").build();
//		}
//	}

}
