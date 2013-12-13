package br.com.christ.html2pdf.loader;

import java.io.IOException;

import br.com.christ.html2pdf.utils.FacesUtils;

public class FacesResourceLoader implements ResourceLoader {
	public String getStringFromReference(String reference) throws IOException {
		return FacesUtils.getStringFromReference(reference);
	}

	public byte[] getBytesFromReference(String reference) throws IOException {
		return FacesUtils.getBytesFromReference(reference);
	}

	public byte[] getBytesFromResource(String reference) throws IOException {
		return FacesUtils.getBytesFromResource(reference);
	}
}
