package br.com.christ.jsf.html2pdf.listener;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;

import br.com.christ.html2pdf.converter.ConversionListener;
import br.com.christ.jsf.html2pdf.listener.PDFConverterConfig;

@Default
@RequestScoped
public class PerRequestPDFConverterConfig implements PDFConverterConfig {
    private List<? extends ConversionListener> listeners;
    private boolean preloadResources = false;
    private String fileName = null;
    private boolean enablePdf = false;
    private String pdfAction = null;
    private String encoding = null;
    private boolean removeStyles = false;
    private boolean removeImages = false;

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

    @Override
    public List<? extends ConversionListener> getListeners() {
        return listeners;
    }

    @Override
    public void setListeners(List<? extends ConversionListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void setListeners(ConversionListener... listeners) {
        setListeners(Arrays.asList(listeners));
    }

    public boolean isRemoveStyles() {
        return removeStyles;
    }

    @Override
    public void setRemoveStyles(boolean removeStyles) {
        this.removeStyles = removeStyles;
    }
}
