package br.com.christ.html2pdf.converter;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.com.christ.html2pdf.exception.ConversionException;
import br.com.christ.html2pdf.loader.FacesHttpStreamFactory;
import br.com.christ.html2pdf.renderer.FaceletRenderer;
import br.com.christ.html2pdf.utils.FacesUtils;
import br.com.christ.jsf.html2pdf.listener.PDFConverterConfig;

public class FaceletsConverter {
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
		PDFConverterContext converterContext = new PDFConverterContext(config);
		converterContext.setUrl(FacesUtils.getBaseFacesURL());
		converterContext.setHtmlContent(content);
		converterContext.setHttpStreamFactory(new FacesHttpStreamFactory());
		byte[] bytesPdf;
		bytesPdf = Html2PDFConverter.convertHtmlToPDF(converterContext);
		return bytesPdf;
	}


}
