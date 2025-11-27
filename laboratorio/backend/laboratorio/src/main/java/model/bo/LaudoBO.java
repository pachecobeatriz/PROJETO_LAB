package model.bo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import model.dao.LaudoDAO;
import model.vo.LaudoVO;

import model.dao.ExameDAO;
import model.enums.StatusExame;

public class LaudoBO {

	public LaudoVO gravarLaudoBO(InputStream laudoInputStream, InputStream fileInputStream,
			FormDataContentDisposition fileMetaData) throws Exception {

		if (laudoInputStream == null) {
			throw new IllegalArgumentException("Dados do laudo não informados (campo laudoVO).");
		}

// Converte o JSON enviado no campo "laudoVO" para um objeto LaudoVO
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule()); // suporte a LocalDate

		LaudoVO laudoVO = mapper.readValue(laudoInputStream, LaudoVO.class);

// Converte o arquivo (PDF) em byte[]
		if (fileInputStream != null) {
			byte[] arquivoBytes = toByteArray(fileInputStream);
			laudoVO.setArquivo(arquivoBytes);
		}

// Se não vier data no JSON, usa a data atual
		if (laudoVO.getDataLaudo() == null) {
			laudoVO.setDataLaudo(LocalDate.now());
		}

		LaudoDAO laudoDAO = new LaudoDAO();
		boolean sucessoLaudo;

// Se tiver idLaudo > 0, atualiza; senão, cadastra
		if (laudoVO.getIdLaudo() > 0) {
			sucessoLaudo = laudoDAO.atualizar(laudoVO);
		} else {
			sucessoLaudo = laudoDAO.cadastrar(laudoVO);
		}

		if (!sucessoLaudo) {
			throw new RuntimeException("Não foi possível gravar os dados do laudo.");
		}

// Se Pronto - Mete bala
		if (laudoVO.getIdExame() <= 0) {
			throw new RuntimeException("idExame não informado no LaudoVO para atualizar o status.");
		}

		ExameDAO exameDAO = new ExameDAO();
		boolean sucessoStatus = exameDAO.atualizarStatusExame(laudoVO.getIdExame(), StatusExame.PRONTO);

		if (!sucessoStatus) {
			throw new RuntimeException("Laudo gravado, mas não foi possível atualizar o status do exame para PRONTO.");
		}

		return laudoVO;
	}

	// Utilitário para converter InputStream -> byte[]
	private byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] data = new byte[4096];
		int nRead;
		while ((nRead = input.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		return buffer.toByteArray();
	}

}
