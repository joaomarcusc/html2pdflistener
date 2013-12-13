package br.com.christ.html2pdf.converter;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

public interface ConversionListener {
	boolean beforeConvert(ConverterContext context) throws IOException, DocumentException;
	boolean afterConvert(ConverterContext context);
}
