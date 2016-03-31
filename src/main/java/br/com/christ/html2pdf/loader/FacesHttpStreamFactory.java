package br.com.christ.html2pdf.loader;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import br.com.christ.html2pdf.utils.FacesUtils;
import com.openhtmltopdf.extend.HttpStream;
import com.openhtmltopdf.extend.HttpStreamFactory;
import com.openhtmltopdf.swing.NaiveUserAgent.DefaultHttpStream;

public class FacesHttpStreamFactory implements HttpStreamFactory {
	@Override
	public HttpStream getUrl(final String url) {
		String baseUrl = FacesUtils.getBaseFacesURL();

		try {
			return new FacesHttpStream(new ByteArrayInputStream(FacesUtils.getBytesFromReference(url)));
		} catch (IOException e) {
			return new DefaultHttpStream(new ByteArrayInputStream(new byte[0]));
		}
	}
}
