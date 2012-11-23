package br.com.christ.jsf.html2pdf.listener;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
            Document xhtmlContent = null;
            Tidy tidy = new Tidy();
            try {
                tidy.setFixUri(true);
                tidy.setXHTML(true);
                tidy.setShowWarnings(false);
                tidy.setAsciiChars(true);
                tidy.setNumEntities(true);
                xhtmlContent = tidy.parseDOM(new ByteArrayInputStream(htmlContent.getBytes()),pdfStream);
            } catch(Exception e) {
                e.printStackTrace();
            }
            try {
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocument(xhtmlContent, ((HttpServletRequest) request).getRequestURL().toString());
                renderer.layout();
                pdfStream.reset();
                renderer.createPDF(pdfStream);

            } catch (com.itextpdf.text.DocumentException e) {
                throw new RuntimeException(e);
            }

            byte[] bytesPDF = pdfStream.toByteArray();
            request.setAttribute("ja_gerou_pdf", "1");
            if(actionPdf != null && !actionPdf.isEmpty()) {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition","attachment; filename="+nomeArquivo);
                response.getOutputStream().write(bytesPDF);
                facesContext.responseComplete();
            } else {
                MethodExpression methodExpression = application.getExpressionFactory().createMethodExpression(elContext, actionPdf,
                        String.class,
                        new Class[]{byte[].class});
                String outcome = methodExpression.invoke(elContext, new Object[] { bytesPDF }).toString();
                facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, "", outcome);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
