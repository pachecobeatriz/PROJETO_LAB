package controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.bo.PacienteBO;
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
	
	// NOVO
	@PUT // PUT é o verbo HTTP mais adequado para 'atualizar' um recurso existente
	@Path("/atualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizar(PacienteVO pacienteVO) {
	    PacienteBO pacienteBO = new PacienteBO();
	    boolean sucesso = pacienteBO.atualizar(pacienteVO);
	    
	    if (sucesso) {
	        // Retorna Status 200 OK
	        return Response.ok("Paciente atualizado com sucesso.").build();
	    } else {
	        // Retorna Status 400 Bad Request
	        return Response.status(Response.Status.BAD_REQUEST).entity("Falha ao atualizar o paciente.").build();
	    }
	}

}
