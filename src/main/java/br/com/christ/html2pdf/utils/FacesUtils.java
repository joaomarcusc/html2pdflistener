package br.com.christ.html2pdf.utils;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.util.logging.Logger;


public class FacesUtils {

    final static String resourcePrefix = "/javax.faces.resource";

    private Logger logger =java.util.logging.Logger.getLogger(FacesUtils.class.getName());

    public static byte[] getBytesFromReference(String reference) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
		String contextPath = externalContext.getRequestContextPath();
		if(reference.startsWith(contextPath)) {
			reference = reference.substring((contextPath.length()));
        }
		return FacesUtils.getBytesFromResource(reference);
	}


	public static String getStringFromReference(String reference) throws IOException {
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		if(reference.startsWith(contextPath))
			reference = reference.substring((contextPath.length()));
		return FacesUtils.getStringFromResource(reference);
	}

	public static byte[] getBytesFromResource(String resourcePath) throws IOException {
		InputStream stream = getStreamFromResource(resourcePath);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = stream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();
	}



    private static InputStream getStreamFromResource(String resourcePath) throws IOException {
        if (resourcePath.startsWith(resourcePrefix)) {
            return getStreamFromFacesResource(resourcePath);
        } else {
            return FacesContext.getCurrentInstance().getExternalContext()
                    .getResourceAsStream(resourcePath);
        }

    }
    private static InputStream getStreamFromFacesResource(String resourcePath) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        String resourceName = resourcePath;
        if(resourcePath.startsWith(resourcePrefix))
            resourceName = resourcePath.substring(resourcePrefix.length() + 1);
        String libraryName = null;
        if (resourcePath.contains("ln=")) {
            if (resourceName.contains("?"))
                resourceName = resourceName.substring(0, resourceName.indexOf("?"));
            if(resourceName.endsWith(".jsf"))
                resourceName = resourceName.substring(0, resourceName.lastIndexOf("."));
            int lnIndex = resourcePath.indexOf("ln=");
            libraryName = resourcePath.substring(lnIndex + 3);
            if (libraryName.contains("&")) {
                libraryName = libraryName.substring(0, libraryName.indexOf("&"));
            }
        }
        if (resourceName.endsWith(".jsf"))
            resourceName = resourceName.substring(0, resourceName.lastIndexOf("."));
        Resource resource;
        if (libraryName != null) {
            resource = resourceHandler.createResource(resourceName, libraryName);
        } else {
            resource = resourceHandler.createResource(resourceName);
        }
        if(resource == null) {
            Logger.getLogger(FacesUtils.class.getName()).warning("Could not fetch resource "+resourceName+ "!");
            return null;
        }
        return resource.getInputStream();
    }

	public static String getStringFromResource(String resourcePath) throws IOException {
        InputStream stream = getStreamFromResource(resourcePath);
        if(stream == null)
            return "";
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
