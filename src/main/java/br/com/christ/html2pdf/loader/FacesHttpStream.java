package br.com.christ.html2pdf.loader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.openhtmltopdf.extend.HttpStream;

public class FacesHttpStream implements HttpStream {

	private final InputStream inputStream;
	private final Reader reader;

	public FacesHttpStream(InputStream inputStream) {
		this.inputStream = inputStream;
		this.reader = new InputStreamReader(this.inputStream);
	}

	@Override
	public InputStream getStream() {
		return inputStream;
	}

	@Override
	public Reader getReader() {
		return reader;
	}
}
