package br.com.christ.html2pdf.converter;

import java.util.Arrays;
import java.util.List;

import br.com.christ.html2pdf.exception.ConversionException;
import br.com.christ.html2pdf.loader.FacesResourceLoader;
import br.com.christ.html2pdf.loader.ResourceLoader;

public class Html2PDFConverter {

    public static byte[] convertHtmlToPDF(String htmlContent, String url, String encoding, boolean preloadResources, ResourceLoader resourceLoader) throws ConversionException {
        return convertHtmlToPDF(htmlContent, url,encoding, false, resourceLoader, Arrays.asList(new ConversionListener[]{}));
    }
    public static byte[] convertHtmlToPDF(String htmlContent, String url, String encoding, boolean preloadResources, ResourceLoader resourceLoader, List<? extends ConversionListener> listeners) throws ConversionException {
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
}
