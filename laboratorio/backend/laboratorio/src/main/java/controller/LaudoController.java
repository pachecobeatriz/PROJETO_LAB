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

@Path("/laudo")
public class LaudoController {

    @POST
    @Path("/cadastrar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public LaudoVO gravarLaudoController(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData,
            @FormDataParam("laudoVO") InputStream laudoInputStream) throws Exception {

        LaudoBO laudoBO = new LaudoBO();
        // o BO fica responsável por:
        // - ler o JSON do laudo a partir de laudoInputStream
        // - tratar o InputStream do arquivo (PDF)
        // - salvar no banco e retornar o LaudoVO gravado
        return laudoBO.gravarLaudoBO(laudoInputStream, fileInputStream, fileMetaData);
    }
}
