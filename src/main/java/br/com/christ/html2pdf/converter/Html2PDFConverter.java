package br.com.christ.html2pdf.converter;

import java.util.Arrays;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.com.christ.html2pdf.exception.ConversionException;
import br.com.christ.html2pdf.loader.FacesResourceLoader;
import br.com.christ.html2pdf.loader.ResourceLoader;
import br.com.christ.html2pdf.renderer.FaceletRenderer;
import br.com.christ.jsf.html2pdf.listener.PDFConverterConfig;

public class Html2PDFConverter {

	public static byte[] convertHtmlToPDF(String htmlContent, String url, String encoding, boolean preloadResources, ResourceLoader resourceLoader) throws ConversionException {
		return convertHtmlToPDF(htmlContent, url, encoding, false, resourceLoader, Arrays.asList(new ConversionListener[]{}));
	}

	public static byte[] convertHtmlToPDF(String htmlContent, String url, String encoding, boolean preloadResources, ResourceLoader resourceLoader, List<ConversionListener> listeners) throws ConversionException {
		ConverterContext context = new ConverterContext();
		context.setListeners(listeners);
		context.setHtmlContent(htmlContent);
		context.setUrl(url);
		context.setPreloadResources(preloadResources);
		context.setResourceLoader(resourceLoader);
		context.setInputEncoding(encoding);
		Converter converter = new Converter();
		return converter.convertHtmlToPDF(context);
	}

	public static byte[] convertHtmlToPDF(ConverterContext context) throws ConversionException {
		Converter converter = new Converter();
		return converter.convertHtmlToPDF(context);
	}

	/**
	 * Renderizar a view especificada como PDF. As informações de configuração são passadas
	 * via atributo de request pdfConfig.
	 *
	 * @param config Configuração da geração.
	 * @param viewId Caminho para a view.
	 * @return bytes do PDF.
	 */
	public static byte[] renderFaceletAsPDF(PDFConverterConfig config, String viewId) throws ConversionException {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.setAttribute("pdfConfig", config);
		FaceletRenderer renderer = new FaceletRenderer(FacesContext.getCurrentInstance());
		String content = renderer.renderView(viewId);
		ConverterContext converterContext = new ConverterContext(config);
		converterContext.setUrl("");
		converterContext.setHtmlContent(content);
		byte[] bytesPdf;
		bytesPdf = Html2PDFConverter.convertHtmlToPDF(converterContext);
		return bytesPdf;
	}

}
