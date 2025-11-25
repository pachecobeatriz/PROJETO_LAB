package controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.bo.FuncionarioBO;
import model.vo.FuncionarioVO;

@Path("/funcionario")
public class FuncionarioController {

	// PUT com VO
	@PUT
	@Path("/atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FuncionarioVO atualizar(FuncionarioVO funcionarioVO) {
		FuncionarioBO funcionarioBO = new FuncionarioBO();

		FuncionarioVO funcionarioAtualizado = funcionarioBO.atualizar(funcionarioVO);

		return funcionarioAtualizado;
	}

//	// PUT com RESPONSE
//	@PUT
//	@Path("/atualizar")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response atualizar(FuncionarioVO funcionarioVO) {
//		FuncionarioBO funcionarioBO = new FuncionarioBO();
//		boolean sucesso = funcionarioBO.atualizar(funcionarioVO);
//
//		if (sucesso) {
//			return Response.ok("Funcionário atualizado com sucesso.").build();
//		} else {
//			return Response.status(Response.Status.BAD_REQUEST)
//					.entity("Falha ao atualizar o funcionário. Verifique o ID e os dados.").build();
//		}
//	}

}
