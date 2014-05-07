package br.com.christ.jsf.html2pdf.listener;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

public class ResponseCatcher implements HttpServletResponse {

    CharArrayWriter output;

    PrintWriter writer;

    HttpServletResponse response;

    public ResponseCatcher(HttpServletResponse response) {
        this.response = response;
        output = new CharArrayWriter();
        writer = new PrintWriter(output, true);
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setCharacterEncoding(String charset) {
        response.setCharacterEncoding(charset);
    }

    public void flushBuffer() throws IOException {
        writer.flush();
    }

    public boolean isCommitted() {
        return false;
    }

    public boolean containsHeader(String arg0) {
        return false;
    }

    public String encodeURL(String arg0) {
        return response.encodeURL(arg0);
    }

    public String encodeRedirectURL(String arg0) {
        return response.encodeRedirectURL(arg0);
    }

    public String encodeUrl(String arg0) {
        return response.encodeUrl(arg0);
    }

    public String encodeRedirectUrl(String arg0) {
        return response.encodeRedirectUrl(arg0);
    }

    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }

    public String getContentType() {
        return response.getContentType();
    }

    public int getBufferSize() {
        return response.getBufferSize();
    }

    public Locale getLocale() {
        return response.getLocale();
    }

    public void sendError(int arg0, String arg1) throws IOException {
        response.sendError(arg0, arg1);
    }

    public void sendError(int arg0) throws IOException {
        response.sendError(arg0);
    }

    public void sendRedirect(String arg0) throws IOException {
        response.sendRedirect(arg0);
    }

    public void addCookie(Cookie arg0) {
        response.addCookie(arg0);
    }

    public void setDateHeader(String arg0, long arg1) {
        response.setDateHeader(arg0, arg1);
    }

    public void addDateHeader(String arg0, long arg1) {
        response.addDateHeader(arg0, arg1);
    }

    public void setHeader(String arg0, String arg1) {
        response.setHeader(arg0, arg1);
    }

    public void addHeader(String arg0, String arg1) {
        response.addHeader(arg0, arg1);
    }

    public void setIntHeader(String arg0, int arg1) {
        response.setIntHeader(arg0, arg1);
    }

    public void addIntHeader(String arg0, int arg1) {
        response.addIntHeader(arg0, arg1);
    }

    public void setContentLength(int arg0) {
        response.setContentLength(arg0);
    }

    public void setContentType(String arg0) {
        response.setContentType(arg0);
    }

    /* null ops */
    public void setStatus(int arg0) {}
    public void setStatus(int arg0, String arg1) {}

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    public void setBufferSize(int arg0) {}
    public void resetBuffer() {}
    public void reset() {}
    public void setLocale(Locale arg0) {}


    public ServletOutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return output.toString();
    }
}