package br.com.christ.jsf.html2pdf.listener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;

import br.com.christ.html2pdf.converter.ConversionListener;

@Alternative
@RequestScoped
public class PerRequestPDFConverterConfig implements PDFConverterConfig {
    private List<ConversionListener> listeners = new LinkedList<ConversionListener>();
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
    public List<ConversionListener> getListeners() {
        return listeners;
    }

    @Override
    public void addListener(ConversionListener listener) {
        listeners.add(listener);
    }

    @Override
    public void clearListeners() {
        listeners.clear();
    }

    @Override
    public void removeListener(ConversionListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setListeners(List<ConversionListener> listeners) {
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
