package br.com.christ.html2pdf.loader;

import java.io.IOException;

public interface ResourceLoader {
	public String getStringFromReference(String reference) throws IOException;
	public byte[] getBytesFromReference(String reference) throws IOException;
	public byte[] getBytesFromResource(String reference) throws IOException;
}
