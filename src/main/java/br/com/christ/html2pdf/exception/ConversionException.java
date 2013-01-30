package br.com.christ.html2pdf.exception;

public class ConversionException extends Exception {

    private Exception actualException;

    public ConversionException(Exception cause) {
        this.setActualException(cause);
    }

    public Exception getActualException() {
        return actualException;
    }

    public void setActualException(Exception actualException) {
        this.actualException = actualException;
    }
}
