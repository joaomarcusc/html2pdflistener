package br.com.christ.jsf.html2pdf.listener;

import java.io.Serializable;

public interface PDFConverterConfig extends Serializable {
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

	public void onPdfFinish();
}
