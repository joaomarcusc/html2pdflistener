package br.com.christ.jsf.html2pdf.listener;

import java.io.Serializable;
import java.util.List;

import br.com.christ.html2pdf.converter.ConversionListener;

public interface PDFConverterConfig extends Serializable {
    public List<? extends ConversionListener> getListeners();

    public void setListeners(List<? extends ConversionListener> listeners);

    public void setListeners(ConversionListener... listeners);

	public void setPreloadResources(boolean preload);

	public boolean isPreloadResources();

	public void setFileName(String fileName);

	public String getFileName();

	public void setEnablePdf(boolean enablePdf);

	public boolean isEnablePdf();

	public void setPdfAction(String pdfAction);

	public String getPdfAction();

	public void setEncoding(String encoding);

	public String getEncoding();

    public boolean isRemoveStyles();

    public void setRemoveStyles(boolean removeAllStyles);
}
