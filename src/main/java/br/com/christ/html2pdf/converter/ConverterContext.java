package br.com.christ.html2pdf.converter;

import javax.faces.bean.RequestScoped;

import br.com.christ.html2pdf.loader.FacesResourceLoader;
import br.com.christ.html2pdf.loader.ResourceLoader;

@RequestScoped
public class ConverterContext {
	private ResourceLoader resourceLoader;
	private boolean preloadResources;
	private String htmlContent;
	private String url;

	private String inputEncoding;

	public ConverterContext() {
        preloadResources = false;
        url = "";
        htmlContent = "";
        resourceLoader = new FacesResourceLoader();
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

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
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
}
