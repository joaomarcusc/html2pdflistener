package br.com.christ.html2pdf.converter;

import br.com.christ.html2pdf.utils.FacesUtils;
import com.openhtmltopdf.pdfboxout.PdfBoxOutputDevice;
import com.openhtmltopdf.pdfboxout.PdfBoxUserAgent;
import com.openhtmltopdf.swing.NaiveUserAgent;

import java.io.IOException;

public class FacesUserAgentCallback extends PdfBoxUserAgent {
    public FacesUserAgentCallback(PdfBoxOutputDevice outputDevice) {
        super(outputDevice);
    }

    @Override
	public byte[] getBinaryResource(String uri) {
		try {
			return FacesUtils.getBytesFromReference(uri);
		} catch (IOException e) {
			return super.getBinaryResource(uri);
		}
	}

}
