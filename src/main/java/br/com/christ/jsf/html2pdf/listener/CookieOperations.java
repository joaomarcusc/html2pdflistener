package br.com.christ.jsf.html2pdf.listener;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import br.com.christ.html2pdf.converter.ConversionListener;
import br.com.christ.html2pdf.converter.ConverterContext;

public class CookieOperations implements ConversionListener {
    private static enum OpType {
        CREATE, DELETE
    };

    private OpType opType;
    private String name;
    private String value;

    private CookieOperations() {
    }

    public static CookieOperations doAdd(String name, String value) {
        CookieOperations op = new CookieOperations();
        op.name = name;
        op.value = value;
        op.opType = OpType.CREATE;
        return op;
    }

    public static CookieOperations doDelete(String name) {
        CookieOperations op = new CookieOperations();
        op.name = name;
        op.opType = OpType.DELETE;
        return op;
    }

    @Override
    public boolean beforeConvert(ConverterContext context) throws IOException {
        return false;
    }

    @Override
    public boolean afterConvert(ConverterContext context) {
        return false;
    }

    @Override
    public boolean afterResponseComplete(ConverterContext context) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        switch(opType) {
            case CREATE:
                response.addCookie(new Cookie(name, value));
                break;
            case DELETE:
                Cookie cookie = new Cookie(name, "");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                break;
        }
        return true;
    }



}
