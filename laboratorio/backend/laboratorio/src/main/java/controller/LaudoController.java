package controller;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.bo.LaudoBO;
import model.vo.LaudoVO;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import model.dao.ExameDAO;
import model.dao.LaudoDAO;
import model.enums.StatusExame;

@Path("/laudo")
public class LaudoController {

	@POST
	@Path("/cadastrar")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public LaudoVO gravarLaudoController(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData,
			@FormDataParam("laudoVO") InputStream laudoInputStream) throws Exception {

		LaudoBO laudoBO = new LaudoBO();
		// o BO fica responsável por:
		// - ler o JSON do laudo a partir de laudoInputStream
		// - tratar o InputStream do arquivo (PDF)
		// - salvar no banco e retornar o LaudoVO gravado
		return laudoBO.gravarLaudoBO(laudoInputStream, fileInputStream, fileMetaData);
	}

	// Ter ou não ter laudo, eis a questão
	@GET
	@Path("/download/{idExame}")
	@Produces("application/pdf")
	public Response downloadLaudo(@PathParam("idExame") int idExame) {

		// 1) Verifica se exame está PRONTO
		ExameDAO exameDAO = new ExameDAO();
		StatusExame status = exameDAO.consultarStatus(idExame);

		if (status == null || status != StatusExame.PRONTO) {
			return Response.status(Response.Status.FORBIDDEN).entity("Exame não está com status PRONTO ou não existe.")
					.type(MediaType.TEXT_PLAIN).build();
		}

		// 2) Busca laudo
		LaudoDAO laudoDAO = new LaudoDAO();
		LaudoVO laudo = laudoDAO.buscarPorIdExame(idExame);

		if (laudo == null || laudo.getArquivo() == null || laudo.getArquivo().length == 0) {
			return Response.status(Response.Status.NOT_FOUND).entity("Laudo não encontrado para este exame.")
					.type(MediaType.TEXT_PLAIN).build();
		}

		// 3) Retorna o PDF
		String nomeArquivo = "laudo_exame_" + idExame + ".pdf";

		return Response.ok(laudo.getArquivo()).type("application/pdf")
				.header("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"").build();
	}
}
