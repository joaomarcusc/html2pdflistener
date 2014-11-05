package br.com.christ.jsf.html2pdf.listener;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
        switch(opType) {
            case CREATE:
                addCookie(new Cookie(name, value));
                break;
            case DELETE:
                Cookie cookie = new Cookie(name, "");
                cookie.setMaxAge(0);
                addCookie(cookie);
                break;
        }
        return true;
    }

    public void addCookie(Cookie cookie) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

        String refererHeader = request.getHeader("Referer");


        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();

        String refererURI = refererHeader.substring(refererHeader.indexOf(contextPath));
        if (!requestURI.equals(refererURI)) {
            String refererPath = refererURI.substring(0, refererURI.lastIndexOf("/"));

            cookie.setPath(refererPath);
        }
        response.addCookie(cookie);
    }




}
