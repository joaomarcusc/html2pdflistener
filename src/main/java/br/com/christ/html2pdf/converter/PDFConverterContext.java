package br.com.christ.html2pdf.converter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.RequestScoped;

import br.com.christ.html2pdf.loader.FacesResourceLoader;
import br.com.christ.html2pdf.loader.ResourceLoader;
import br.com.christ.jsf.html2pdf.listener.PDFConverterConfig;
import com.openhtmltopdf.extend.HttpStreamFactory;

@RequestScoped
public class PDFConverterContext {
	private HttpStreamFactory streamFactory;
    private List<ConversionListener> listeners;
	private ResourceLoader resourceLoader;
	private boolean preloadResources;
	private String htmlContent;
	private String urlPrefix;
	private String url;
    private boolean removeStyles;

	private String inputEncoding;

	public PDFConverterContext() {
        setListeners(new ArrayList<ConversionListener>());
        preloadResources = false;
        url = "";
        htmlContent = "";
        resourceLoader = new FacesResourceLoader();
	}

	public PDFConverterContext(PDFConverterConfig config) {
		this.setListeners(config.getListeners());
		this.setPreloadResources(config.isPreloadResources());
		this.setInputEncoding(config.getEncoding());
		this.setRemoveStyles(config.isRemoveStyles());
		this.setHttpStreamFactory(config.getHttpStreamFactory());
	}

	public boolean isPreloadResources() {
		return preloadResources;
	}

	public void setPreloadResources(boolean preloadResources) {
		this.preloadResources = preloadResources;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInputEncoding() {
		return inputEncoding;
	}

	public void setInputEncoding(String inputEncoding) {
		this.inputEncoding = inputEncoding;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

    public List<? extends ConversionListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<ConversionListener> listeners) {
        this.listeners = listeners;
    }

    public boolean isRemoveStyles() {
        return removeStyles;
    }

    public void setRemoveStyles(boolean removeStyles) {
        this.removeStyles = removeStyles;
    }

	public HttpStreamFactory getHttpStreamFactory() {
		return streamFactory;
	}

	public void setHttpStreamFactory(final HttpStreamFactory streamFactory) {
		this.streamFactory = streamFactory;
	}
}
