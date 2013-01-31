import br.com.christ.html2pdf.converter.Html2PDFConverter;
import br.com.christ.html2pdf.exception.ConversionException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;
import java.util.logging.Logger;

public class TestGeracaoPDF {

    private static Logger logger = Logger.getLogger(TestGeracaoPDF.class.getCanonicalName());

    private static URLClassLoader resourceLoader= null;

    /**
     * Initialize class loader.
     */
    static{
        ClassLoader currentThreadClassLoader
                = Thread.currentThread().getContextClassLoader();

        try {
            System.out.println(new File(".").toURI().toURL().toString());
            resourceLoader
                    = new URLClassLoader(new URL[]{new File(".").toURI().toURL()},
                    currentThreadClassLoader);
        } catch (MalformedURLException e) {
            logger.warning(e.getLocalizedMessage());
        }
    }

    public static void main(String[] args) throws IOException, ConversionException {
        Scanner scanner = new Scanner(new File("/var/www/teste2/confirmaAderirRPC.jsf.html"));
        StringWriter stringWriter = new StringWriter();
        while(scanner.hasNextLine()) {
            stringWriter.write(scanner.nextLine());
        }
        scanner.close();
        byte[] bytesPDF = Html2PDFConverter.convertHtmlToPDF(stringWriter.toString(),"http://localhost/teste2/confimaAderirRPC.jsf.html");
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/saida.pdf");
        fileOutputStream.write(bytesPDF);
    }
}
