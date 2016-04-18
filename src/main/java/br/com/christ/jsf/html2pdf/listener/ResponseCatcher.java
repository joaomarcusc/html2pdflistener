package br.com.christ.jsf.html2pdf.listener;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Locale;

public class ResponseCatcher implements HttpServletResponse {
    private HttpServletResponse delegate;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    public ResponseCatcher(HttpServletResponse response) {
        this.delegate = response;
        this.stringWriter = new StringWriter();
        this.printWriter = new PrintWriter(this.stringWriter);
    }

    @Override public void addCookie(final Cookie cookie) {
        delegate.addCookie(cookie);
    }

    @Override public void addDateHeader(final String s, final long l) {
        delegate.addDateHeader(s, l);
    }

    @Override public void addHeader(final String s, final String s1) {
        delegate.addHeader(s, s1);
    }

    @Override public void addIntHeader(final String s, final int i) {
        delegate.addIntHeader(s, i);
    }

    @Override public boolean containsHeader(final String s) {
        return delegate.containsHeader(s);
    }

    @Override public String encodeRedirectURL(final String s) {
        return delegate.encodeRedirectURL(s);
    }

    @Override public String encodeRedirectUrl(final String s) {
        return delegate.encodeRedirectUrl(s);
    }

    @Override public String encodeURL(final String s) {
        return delegate.encodeURL(s);
    }

    @Override public String encodeUrl(final String s) {
        return delegate.encodeUrl(s);
    }

    @Override public String getHeader(final String s) {
        return delegate.getHeader(s);
    }

    @Override public Collection<String> getHeaderNames() {
        return delegate.getHeaderNames();
    }

    @Override public Collection<String> getHeaders(final String s) {
        return delegate.getHeaders(s);
    }

    @Override public int getStatus() {
        return delegate.getStatus();
    }

    @Override public void sendError(final int i) throws IOException {
        delegate.sendError(i);
    }

    @Override public void sendError(final int i, final String s) throws IOException {
        delegate.sendError(i, s);
    }

    @Override public void sendRedirect(final String s) throws IOException {
        delegate.sendRedirect(s);
    }

    @Override public void setDateHeader(final String s, final long l) {
        delegate.setDateHeader(s, l);
    }

    @Override public void setHeader(final String s, final String s1) {
        delegate.setHeader(s, s1);
    }

    @Override public void setIntHeader(final String s, final int i) {
        delegate.setIntHeader(s, i);
    }

    @Override public void setStatus(final int i) {
        delegate.setStatus(i);
    }

    @Override public void setStatus(final int i, final String s) {
        delegate.setStatus(i, s);
    }

    @Override public void flushBuffer() throws IOException {
        delegate.flushBuffer();
    }

    @Override public int getBufferSize() {
        return delegate.getBufferSize();
    }

    @Override public String getCharacterEncoding() {
        return delegate.getCharacterEncoding();
    }

    @Override public String getContentType() {
        return delegate.getContentType();
    }

    @Override public Locale getLocale() {
        return delegate.getLocale();
    }

    @Override public ServletOutputStream getOutputStream() throws IOException {
        return delegate.getOutputStream();
    }

    @Override public PrintWriter getWriter() throws IOException {
        return printWriter;
    }

    @Override public boolean isCommitted() {
        return delegate.isCommitted();
    }

    @Override public void reset() {
        delegate.reset();
    }

    @Override public void resetBuffer() {
        delegate.resetBuffer();
    }

    @Override public void setBufferSize(final int i) {
        delegate.setBufferSize(i);
    }

    @Override public void setCharacterEncoding(final String s) {
        delegate.setCharacterEncoding(s);
    }

    @Override public void setContentLength(final int i) {
        delegate.setContentLength(i);
    }

    @Override public void setContentType(final String s) {
        delegate.setContentType(s);
    }

    @Override public void setLocale(final Locale locale) {
        delegate.setLocale(locale);
    }

    public String getHtmlString() {
        return stringWriter.toString();
    }
}

