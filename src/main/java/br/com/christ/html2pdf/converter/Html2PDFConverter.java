package br.com.christ.html2pdf.converter;

import java.util.Arrays;
import java.util.List;

import br.com.christ.html2pdf.exception.ConversionException;
import br.com.christ.html2pdf.loader.FacesHttpStreamFactory;
import br.com.christ.html2pdf.loader.ResourceLoader;

public class Html2PDFConverter {

	public static byte[] convertHtmlToPDF(String htmlContent, String url, String encoding, boolean preloadResources) throws ConversionException {
		return convertHtmlToPDF(htmlContent, url, encoding, false, Arrays.asList(new ConversionListener[]{}));
	}

	public static byte[] convertHtmlToPDF(String htmlContent, String url, String encoding, boolean preloadResources, List<ConversionListener> listeners) throws ConversionException {
		PDFConverterContext context = new PDFConverterContext();
		context.setListeners(listeners);
		context.setHtmlContent(htmlContent);
		context.setUrl(url);
		context.setPreloadResources(preloadResources);
		context.setInputEncoding(encoding);
		Converter converter = new Converter();
		return converter.convertHtmlToPDF(context);
	}

	public static byte[] convertHtmlToPDF(PDFConverterContext context) throws ConversionException {
		Converter converter = new Converter();
		return converter.convertHtmlToPDF(context);
	}

}
