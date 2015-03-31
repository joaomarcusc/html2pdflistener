package br.com.christ.html2pdf.converter;

import java.io.IOException;

import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.CSSResource;
import org.xhtmlrenderer.resource.ImageResource;
import org.xhtmlrenderer.resource.XMLResource;

import br.com.christ.html2pdf.utils.FacesUtils;

public class FacesUserAgentCallback extends ITextUserAgent {
	@Override
	public byte[] getBinaryResource(String uri) {
		try {
			return FacesUtils.getBytesFromReference(uri);
		} catch (IOException e) {
			return super.getBinaryResource(uri);
		}
	}

	public FacesUserAgentCallback(ITextOutputDevice outputDevice) {
		super(outputDevice);
	}
}
