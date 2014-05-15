package br.com.christ.html2pdf.converter;

import java.io.IOException;



public interface ConversionListener {
	boolean beforeConvert(ConverterContext context) throws IOException;
	boolean afterConvert(ConverterContext context);
    boolean afterResponseComplete(ConverterContext context);
}
