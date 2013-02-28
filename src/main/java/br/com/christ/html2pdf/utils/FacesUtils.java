package br.com.christ.html2pdf.utils;

import javax.faces.context.FacesContext;
import java.io.*;


public class FacesUtils {

	public static byte[] getBytesFromReference(String reference) throws IOException {
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		if(reference.startsWith(contextPath))
			reference = reference.substring((contextPath.length()));
		return FacesUtils.getBytesFromResource(reference);
	}


	public static String getStringFromReference(String reference) throws IOException {
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		if(reference.startsWith(contextPath))
			reference = reference.substring((contextPath.length()));
		return FacesUtils.getStringFromResource(reference);
	}

	public static byte[] getBytesFromResource(String resourcePath) throws IOException {
		InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(resourcePath);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = stream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();
	}

	public static String getStringFromResource(String resourcePath) throws IOException {
		InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(resourcePath);
		//read it with BufferedReader
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

		StringBuilder sb = new StringBuilder();
		String linha = bufferedReader.readLine();
		while(linha != null) {
			sb.append(linha).append("\n");
			linha = bufferedReader.readLine();
		}
		return sb.toString();
	}
}
