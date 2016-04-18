package br.com.christ.jsf.html2pdf.listener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.christ.cdi.CDIUtil;
import br.com.christ.html2pdf.converter.ConversionListener;
import br.com.christ.html2pdf.converter.Converter;
import br.com.christ.html2pdf.converter.Html2PDFConverter;
import br.com.christ.html2pdf.converter.PDFConverterContext;
import br.com.christ.html2pdf.loader.FacesResourceLoader;

@SuppressWarnings("serial")
@ApplicationScoped
public class Html2PDFPhaseListener implements PhaseListener {

	@Inject
	PDFConverterConfig config;

	public void afterPhase(PhaseEvent event) {
	}

	public void beforePhase(PhaseEvent event) {
		PDFConverterConfig config = CDIUtil.getBean(PDFConverterConfig.class);
		HttpServletRequest request = ((HttpServletRequest) event.getFacesContext().getExternalContext().getRequest());
		HttpServletResponse response = ((HttpServletResponse) event.getFacesContext().getExternalContext().getResponse());
		if (config != null && config.isEnablePdf()) {
			gerarPDF(event);
		}
	}

	private void gerarPDF(PhaseEvent event) {
		PDFConverterConfig config = CDIUtil.getBean(PDFConverterConfig.class);
		HttpServletRequest request = ((HttpServletRequest) event.getFacesContext().getExternalContext().getRequest());
		HttpServletResponse response = ((HttpServletResponse) event.getFacesContext().getExternalContext().getResponse());
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		Object oldResponse = externalContext.getResponse();
		String nomeArquivo = "arquivo.pdf";


		try {
			Application application = facesContext.getApplication();
			ViewHandler viewHandler = application.getViewHandler();
			ELContext elContext = facesContext.getELContext();
			String actionPdf = null;
			if (config.getPdfAction() != null)
				actionPdf = config.getPdfAction();
			if (config.getFileName() != null)
				nomeArquivo = config.getFileName();
			String encoding = ((HttpServletRequest) facesContext.getExternalContext().getRequest()).getCharacterEncoding();
			if (config.getEncoding() != null)
				encoding = config.getEncoding();

			ResponseCatcher catcher = new ResponseCatcher(response);
			externalContext.setResponse(catcher);
			viewHandler.renderView(facesContext, facesContext.getViewRoot());
			String htmlContent = catcher.toString();
			externalContext.setResponse(oldResponse);
			URL url = new URL(request.getRequestURL().toString());
			URL newUrl = new URL(url.getProtocol(),
					url.getHost(),
					url.getPort(),
					facesContext.getExternalContext().getRequestContextPath() + facesContext.getViewRoot().getViewId());
			// Force preloading if the server is HTTPS
			boolean preloadResources = config.isPreloadResources()
					|| url.getProtocol().toLowerCase().equals("https");

			PDFConverterContext context = new PDFConverterContext();
			context.setListeners(config.getListeners());
			context.setHtmlContent(htmlContent);
			context.setUrl(newUrl.toString());
			context.setPreloadResources(preloadResources);
			context.setInputEncoding(encoding);
			context.setRemoveStyles(config.isRemoveStyles());
			Converter converter = new Converter();
			converter.convertHtmlToPDF(context);
			byte[] bytesPDF = Html2PDFConverter.convertHtmlToPDF(context);
			config.setEnablePdf(false);

			List<ConversionListener> listeners = config.getListeners();

			if (actionPdf != null && !actionPdf.isEmpty()) {
				if (listeners != null) {
					for (ConversionListener listener : listeners) {
						listener.afterResponseComplete(context);
					}
				}
				MethodExpression methodExpression = application.getExpressionFactory().createMethodExpression(elContext, actionPdf,
						String.class,
						new Class[]{byte[].class});
				String outcome = methodExpression.invoke(elContext, new Object[]{bytesPDF}).toString();
				facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
			} else {
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=" + nomeArquivo);
				response.getOutputStream().write(bytesPDF);
				facesContext.responseComplete();
				if (listeners != null) {
					for (ConversionListener listener : listeners) {
						listener.afterResponseComplete(context);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			throw new RuntimeException("Error converting the HTML content: " + stringWriter.toString());
		}
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
}
