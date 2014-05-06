package br.com.christ.html2pdf.converter;

import org.ajax4jsf.org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import br.com.christ.html2pdf.loader.ResourceLoader;

public class ConverterContext {
	private ResourceLoader resourceLoader;
	private ITextRenderer renderer;
	private Tidy tidy;
	private boolean preloadResources;
	private String htmlContent;
	private String tidiedHtmlContent;
	private String url;

    private String inputEncoding;

	public ConverterContext() {
		this.renderer = new ITextRenderer();
		this.tidy = new Tidy();
	}

	public ITextRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(ITextRenderer renderer) {
		this.renderer = renderer;
	}

	public Tidy getTidy() {
		return tidy;
	}

	public void setTidy(Tidy tidy) {
		this.tidy = tidy;
	}

	public boolean isPreloadResources() {
		return preloadResources;
	}

	public void setPreloadResources(boolean preloadResources) {
		this.preloadResources = preloadResources;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTidiedHtmlContent() {
		return tidiedHtmlContent;
	}

	public void setTidiedHtmlContent(String tidiedHtmlContent) {
		this.tidiedHtmlContent = tidiedHtmlContent;
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
}
