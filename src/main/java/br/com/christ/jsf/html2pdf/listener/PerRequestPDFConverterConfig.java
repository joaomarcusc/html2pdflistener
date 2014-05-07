package br.com.christ.jsf.html2pdf.listener;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;

import br.com.christ.jsf.html2pdf.listener.PDFConverterConfig;

@Default
@RequestScoped
public class PerRequestPDFConverterConfig implements PDFConverterConfig {
    private boolean preloadResources = false;
    private String fileName = null;
    private boolean enablePdf = false;
    private String pdfAction = null;
    private String encoding = null;

    public boolean isPreloadResources() {
        return preloadResources;
    }

    public void setPreloadResources(boolean preloadResources) {
        this.preloadResources = preloadResources;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isEnablePdf() {
        return enablePdf;
    }

    public void setEnablePdf(boolean enablePdf) {
        this.enablePdf = enablePdf;
    }

    public String getPdfAction() {
        return pdfAction;
    }

    public void setPdfAction(String pdfAction) {
        this.pdfAction = pdfAction;
    }

    public String getEncoding() {
        return encoding;
    }

    public void onPdfFinish() {
        setEnablePdf(false);
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

}
