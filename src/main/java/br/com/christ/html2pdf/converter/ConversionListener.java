package br.com.christ.html2pdf.converter;

import java.io.IOException;



public interface ConversionListener {
	boolean beforeConvert(PDFConverterContext context) throws IOException;
	boolean afterConvert(PDFConverterContext context);
    boolean afterResponseComplete(PDFConverterContext context);
}
