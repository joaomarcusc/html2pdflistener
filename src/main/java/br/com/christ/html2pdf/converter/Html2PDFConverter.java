package br.com.christ.html2pdf.converter;

import br.com.christ.html2pdf.exception.ConversionException;
import br.com.christ.html2pdf.loader.FacesResourceLoader;

public class Html2PDFConverter {

    public static byte[] convertHtmlToPDF(String htmlContent, String url, String encoding, boolean preloadResources) throws ConversionException {
	    ConverterContext context = new ConverterContext();
	    context.setHtmlContent(htmlContent);
        context.setUrl(url);
	    context.setPreloadResources(preloadResources);
	    context.setResourceLoader(new FacesResourceLoader());
        context.setInputEncoding(encoding);
	    Converter converter = new Converter();
	    converter.convertHtmlToPDF(context);
	    return converter.convertHtmlToPDF(context);
    }
}
