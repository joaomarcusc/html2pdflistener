package br.com.christ.jsf.html2pdf.listener;

import br.com.christ.html2pdf.converter.Html2PDFConverter;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

@SuppressWarnings("serial")
public class Html2PDFPhaseListener implements PhaseListener {

    public void afterPhase(PhaseEvent event) {
    }

    public void beforePhase(PhaseEvent event) {
        HttpServletRequest request = ((HttpServletRequest)event.getFacesContext().getExternalContext().getRequest());
        HttpServletResponse response = ((HttpServletResponse)event.getFacesContext().getExternalContext().getResponse());
        if("1".equals(request.getAttribute("gerar_pdf")) && request.getAttribute("ja_gerou_pdf") == null) {
            gerarPDF(event);
        }
    }

    private void gerarPDF(PhaseEvent event) {
        HttpServletRequest request = ((HttpServletRequest)event.getFacesContext().getExternalContext().getRequest());
        HttpServletResponse response = ((HttpServletResponse)event.getFacesContext().getExternalContext().getResponse());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Object oldResponse = externalContext.getResponse();
        String nomeArquivo = "arquivo.pdf";
        try {
            Application application = facesContext.getApplication();
            ViewHandler viewHandler = application.getViewHandler();
            ELContext elContext = facesContext.getELContext();
            String actionPdf = null;
            if(request.getAttribute("action_pdf") != null)
                actionPdf = request.getAttribute("action_pdf").toString();
            if(request.getAttribute("nome_arquivo_pdf") != null)
                nomeArquivo = request.getAttribute("nome_arquivo_pdf").toString();
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
            byte[] bytesPDF = Html2PDFConverter.convertHtmlToPDF(htmlContent, newUrl.toString());
            request.setAttribute("ja_gerou_pdf", "1");
            if(actionPdf != null && !actionPdf.isEmpty()) {
                MethodExpression methodExpression = application.getExpressionFactory().createMethodExpression(elContext, actionPdf,
                        String.class,
                        new Class[]{byte[].class});
                String outcome = methodExpression.invoke(elContext, new Object[] { bytesPDF }).toString();
                facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, "", outcome);
            } else {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition","attachment; filename="+nomeArquivo);
                response.getOutputStream().write(bytesPDF);
                facesContext.responseComplete();
            }
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            throw new RuntimeException("Error converting the HTML content: "+stringWriter.toString());
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
