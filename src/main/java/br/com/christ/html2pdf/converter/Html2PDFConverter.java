package br.com.christ.html2pdf.converter;

import br.com.christ.html2pdf.exception.ConversionException;
import br.com.christ.html2pdf.factory.B64OrPreloadedReplacedElementFactory;
import br.com.christ.html2pdf.utils.FacesUtils;
import org.ajax4jsf.org.w3c.tidy.Tidy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Html2PDFConverter {

	private static class StyleNode {
		String source = null;
		String media = null;
	}

	private static void preloadStylesheets(Document document) throws IOException {
		NodeList nodes = document.getElementsByTagName("link");
		List<StyleNode> styleSheets = new ArrayList<StyleNode>();
		List<Node> itemsToRemove = new ArrayList<Node>();
		Node head = document.getElementsByTagName("head").item(0);
		for(int i=0;i<nodes.getLength();i++) {
			Node node = nodes.item(i);
			Node hrefNode = node.getAttributes().getNamedItem("href");
			if(hrefNode != null) {

				StyleNode styleNode = new StyleNode();
				String styleSource = FacesUtils.getStringFromReference(hrefNode.getNodeValue());
				styleNode.source = styleSource;
				if(node.getAttributes().getNamedItem("media") != null)
					styleNode.media = node.getAttributes().getNamedItem("media").getNodeValue();
				styleSheets.add(styleNode);
			}
			itemsToRemove.add(node);
			hrefNode.setNodeValue("");
		}
		for (Node node : itemsToRemove) {
			head.removeChild(node);
		}

		for(StyleNode styleNode : styleSheets) {
			Element styleElement = document.createElement("style");
			styleElement.setAttribute("type", "text/css");
			if(styleNode.media != null) {
				styleElement.setAttribute("media", styleNode.media);
			}
			styleElement.appendChild(document.createTextNode(styleNode.source));
			head.appendChild(styleElement);
		}
	}

	private static String getStringFromDoc(org.w3c.dom.Document doc)    {
		try
		{
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			writer.flush();
			return writer.toString();
		}
		catch(TransformerException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

    public static byte[] convertHtmlToPDF(String htmlContent, String url, boolean preloadResources) throws ConversionException {
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        Document xhtmlContent = null;
        Tidy tidy = new Tidy();
        try {
            tidy.setFixUri(true);
            tidy.setXHTML(true);
            tidy.setShowWarnings(false);
            tidy.setAsciiChars(true);
            tidy.setNumEntities(true);
            xhtmlContent = tidy.parseDOM(new ByteArrayInputStream(htmlContent.getBytes()),pdfStream);
        } catch(Exception e) {
            throw new ConversionException(e);
        }
        try {

	        ITextRenderer renderer = new ITextRenderer();
	        B64OrPreloadedReplacedElementFactory replacementFactory = new B64OrPreloadedReplacedElementFactory();
	        renderer.getSharedContext().setReplacedElementFactory(replacementFactory);
	        if(preloadResources) {
		        preloadStylesheets(xhtmlContent);
		        htmlContent = getStringFromDoc(xhtmlContent);
		        replacementFactory.setPreloadAllImages(true);
	        }
	        renderer.setDocument(xhtmlContent, url);
	        renderer.layout();
            pdfStream.reset();

            renderer.createPDF(pdfStream);

        } catch (com.itextpdf.text.DocumentException e) {
            throw new ConversionException(e);
        } catch (IOException e) {
            throw new ConversionException(e);
        }

        byte[] bytesPDF = pdfStream.toByteArray();
        return bytesPDF;
    }
}
