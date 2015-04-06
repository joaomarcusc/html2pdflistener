package br.com.christ.html2pdf.factory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import br.com.christ.html2pdf.loader.FacesResourceLoader;
import br.com.christ.html2pdf.loader.ResourceLoader;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import com.lowagie.text.Jpeg;
import com.lowagie.text.pdf.codec.Base64;
import sun.misc.BASE64Decoder;


public class B64OrPreloadedReplacedElementFactory implements ReplacedElementFactory {

	private static final String EMPTY_IMAGE_CONTENT = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAAAXNSR0IArs4c6QAAAAlwSFlzAAAL\n"+
			"EwAACxMBAJqcGAAAAAd0SU1FB98EBg0LFXmmUzUAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRo\n"+
			"IEdJTVBXgQ4XAAAADElEQVQI12P4//8/AAX+Av7czFnnAAAAAElFTkSuQmCC\n";

	private Image emptyImage;

	private LinkedHashMap<String, ReplacedElement> elementCache;

	private ResourceLoader resourceLoader;

	public B64OrPreloadedReplacedElementFactory() {
		super();
		setResourceLoader(new FacesResourceLoader());
		elementCache = new LinkedHashMap<String, ReplacedElement>();
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			emptyImage = Image.getInstance(decoder.decodeBuffer(EMPTY_IMAGE_CONTENT));
		} catch (Exception e) {
			throw new RuntimeException("Erro durante a geração do PDF");
		}
	}

	public B64OrPreloadedReplacedElementFactory(ResourceLoader resourceLoader) {
		this.setResourceLoader(resourceLoader);
	}

	private boolean preloadAllImages = false;

	public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
		Element e = box.getElement();
		if (e == null) {
			return null;
		}
		ReplacedElement imgElement = null;
		String nodeName = e.getNodeName();
		if (nodeName.equals("img")) {
			String srcAttribute = e.getAttribute("src");
			if (elementCache.containsKey(srcAttribute)) {
				imgElement = elementCache.get(srcAttribute);
			} else {
				FSImage fsImage;
				try {
					fsImage = buildImage(srcAttribute, uac);
				} catch (BadElementException e1) {
					e1.printStackTrace();
					fsImage = null;
				} catch (IOException e1) {
					fsImage = null;
					e1.printStackTrace();
				}
				if (fsImage != null) {
					if (cssWidth != -1 || cssHeight != -1) {
						fsImage.scale(cssWidth, cssHeight);
					}
					imgElement = new ITextImageElement(fsImage);
				}
			}
			elementCache.put(srcAttribute, imgElement);
		}

		return imgElement;
	}

	protected FSImage buildImage(String srcAttr, UserAgentCallback uac) throws BadElementException, IOException {
		FSImage fsImage;
		try {
			if (srcAttr.startsWith("preload:")) {
				String preloadSrc = srcAttr.substring("preload:".length());
				fsImage = new ITextFSImage(Image.getInstance(getResourceLoader().getBytesFromReference(preloadSrc)));
			} else if (srcAttr.startsWith("data:image/")) {
				String b64encoded = srcAttr.substring(srcAttr.indexOf("base64,") + "base64,".length(), srcAttr.length());
				byte[] decodedBytes = Base64.decode(b64encoded);

				fsImage = new ITextFSImage(Image.getInstance(decodedBytes));
			} else if (preloadAllImages) {
				fsImage = new ITextFSImage(Image.getInstance(getResourceLoader().getBytesFromReference(srcAttr)));
			} else {
				fsImage = uac.getImageResource(srcAttr).getImage();
			}
		} catch(Exception exc) {
			fsImage = new ITextFSImage(emptyImage);
		}
		return fsImage;
	}

	public void remove(Element e) {
	}

	public void setFormSubmissionListener(FormSubmissionListener listener) {

	}

	public void reset() {
	}

	public boolean isPreloadAllImages() {
		return preloadAllImages;
	}

	public void setPreloadAllImages(boolean preloadAllImages) {
		this.preloadAllImages = preloadAllImages;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}