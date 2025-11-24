package controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.bo.FuncionarioBO;
import model.vo.FuncionarioVO;

@Path("/funcionario")
public class FuncionarioController {

	@PUT // Verbo ideal para atualizar um recurso
	@Path("/atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizar(FuncionarioVO funcionarioVO) {
		FuncionarioBO funcionarioBO = new FuncionarioBO();
		boolean sucesso = funcionarioBO.atualizar(funcionarioVO);

		if (sucesso) {
			// Retorna Status 200 OK
			return Response.ok("Funcionário atualizado com sucesso.").build();
		} else {
			// Retorna Status 400 Bad Request
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Falha ao atualizar o funcionário. Verifique o ID e os dados.").build();
		}
	}

	// Se não houver método cadastrar, esta classe pode começar com o método acima.
}
