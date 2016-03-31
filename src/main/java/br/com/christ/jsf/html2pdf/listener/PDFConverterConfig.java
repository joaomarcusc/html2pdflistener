package br.com.christ.jsf.html2pdf.listener;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import br.com.christ.html2pdf.converter.ConversionListener;
import com.openhtmltopdf.extend.HttpStreamFactory;

public interface PDFConverterConfig extends Serializable {
	public HttpStreamFactory getHttpStreamFactory();

    public List<ConversionListener> getListeners();

	public void addListener(ConversionListener listener);

	public void clearListeners();

	public void removeListener(ConversionListener listener);

    public void setListeners(List<ConversionListener> listeners);

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
