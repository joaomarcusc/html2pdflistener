package br.com.christ.html2pdf.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import br.com.christ.html2pdf.exception.ConversionException;
import br.com.christ.html2pdf.factory.B64OrPreloadedReplacedElementFactory;
import br.com.christ.html2pdf.loader.ResourceLoader;
import com.lowagie.text.DocumentException;

public class Converter {

	private static class StyleNode {
		String source = null;
		String media = null;
	}

	private static void preloadStylesheets(ResourceLoader loader, Document document) throws IOException {
		NodeList nodes = document.getElementsByTagName("link");
		List<StyleNode> styleSheets = new ArrayList<StyleNode>();
		List<Node> itemsToRemove = new ArrayList<Node>();
		Node head = document.getElementsByTagName("head").item(0);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			Node hrefNode = node.getAttributes().getNamedItem("href");
			if (hrefNode != null) {

				StyleNode styleNode = new StyleNode();
                styleNode.source = loader.getStringFromReference(hrefNode.getNodeValue());
				if (node.getAttributes().getNamedItem("media") != null)
					styleNode.media = node.getAttributes().getNamedItem("media").getNodeValue();
				styleSheets.add(styleNode);
			}
			itemsToRemove.add(node);
            assert hrefNode != null;
            hrefNode.setNodeValue("");
		}
		for (Node node : itemsToRemove) {
			head.removeChild(node);
		}

		for (StyleNode styleNode : styleSheets) {
			Element styleElement = document.createElement("style");
			styleElement.setAttribute("type", "text/css");
			if (styleNode.media != null) {
				styleElement.setAttribute("media", styleNode.media);
			}
			styleElement.appendChild(document.createTextNode(styleNode.source));
			head.appendChild(styleElement);
		}
	}

    private boolean mustPreserveElem(Element elem) {
        String preserveVal = elem.getAttribute("data-pdf-preserve");
        String mediaVal = elem.getAttribute("media");
        if(mediaVal == null)
            mediaVal = "";
        return mediaVal.contains("pdf") ||
                "true".equalsIgnoreCase(preserveVal) ||
                "1".equalsIgnoreCase(preserveVal);
    }

    private String getAttr(Element elem, String attr) {
        return getAttr(elem, attr, null);
    }

    private String getAttr(Element elem, String attr, String defaultVal) {
        String attrVal = elem.getAttribute(attr);
        return attrVal != null ? attrVal : defaultVal;
    }

    private void removeStylesheets(Document document) {
        NodeList nodes = document.getElementsByTagName("link");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element)nodes.item(i);
            String rel = getAttr(elem, "rel");
            if ("stylesheet".equalsIgnoreCase(rel) &&
                    !mustPreserveElem(elem)) {
                elem.setAttribute("href", "");
            } else {
                fixPdfMedia(elem);
            }
        }
        nodes = document.getElementsByTagName("style");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element) nodes.item(i);
            if(!mustPreserveElem(elem))
                elem.setNodeValue("");
            else
                fixPdfMedia(elem);
        }
    }


    private void fixPdfMedia(Element elem) {
        String mediaVal = elem.getAttribute("media");
        if(mediaVal == null)
            mediaVal = "print";
        if(!mediaVal.contains("print"))
            mediaVal = mediaVal+",print";
        elem.setAttribute("media", mediaVal);
    }

	public byte[] convertHtmlToPDF(ConverterContext context) throws ConversionException {
		ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        List<? extends ConversionListener> listeners = context.getListeners();
        if(listeners == null)
            listeners = new ArrayList<ConversionListener>();
		Document xhtmlContent;
		Tidy tidy = new Tidy();
		try {
			tidy.setFixUri(true);
			tidy.setXHTML(true);
			tidy.setShowWarnings(false);
			tidy.setAsciiChars(true);
			tidy.setNumEntities(true);
            tidy.setOutputEncoding(context.getInputEncoding());
            tidy.setInputEncoding(context.getInputEncoding());
			xhtmlContent = tidy.parseDOM(new ByteArrayInputStream(context.getHtmlContent().getBytes()), pdfStream);
		} catch (Exception e) {
			throw new ConversionException(e);
		}
		try {
            ITextRenderer renderer = new ITextRenderer();
			B64OrPreloadedReplacedElementFactory replacementFactory = new B64OrPreloadedReplacedElementFactory();
			replacementFactory.setResourceLoader(context.getResourceLoader());
			renderer.getSharedContext().setReplacedElementFactory(replacementFactory);
            if (context.isRemoveStyles())
                removeStylesheets(xhtmlContent);
            if (context.isPreloadResources()) {
				preloadStylesheets(context.getResourceLoader(), xhtmlContent);
				replacementFactory.setPreloadAllImages(true);
			}
			renderer.setDocument(xhtmlContent, context.getUrl());
			for (ConversionListener listener : listeners) {
				listener.beforeConvert(context);
			}
			renderer.layout();
			pdfStream.reset();

			renderer.createPDF(pdfStream);

		} catch (IOException e) {
			throw new ConversionException(e);
		} catch (DocumentException e) {
            throw new ConversionException(e);
        }
        for (ConversionListener listener : listeners) {
			listener.afterConvert(context);
		}

        return pdfStream.toByteArray();
	}

}
