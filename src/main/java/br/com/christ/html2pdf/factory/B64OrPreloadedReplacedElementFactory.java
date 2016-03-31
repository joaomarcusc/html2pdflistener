package br.com.christ.html2pdf.factory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;

import org.w3c.dom.Element;

import br.com.christ.html2pdf.loader.FacesResourceLoader;
import br.com.christ.html2pdf.loader.ResourceLoader;
import com.openhtmltopdf.extend.FSImage;
import com.openhtmltopdf.extend.ReplacedElement;
import com.openhtmltopdf.extend.ReplacedElementFactory;
import com.openhtmltopdf.extend.UserAgentCallback;
import com.openhtmltopdf.layout.LayoutContext;
import com.openhtmltopdf.pdfboxout.PdfBoxImage;
import com.openhtmltopdf.pdfboxout.PdfBoxImageElement;
import com.openhtmltopdf.render.BlockBox;
import com.openhtmltopdf.simple.extend.FormSubmissionListener;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

public class B64OrPreloadedReplacedElementFactory implements ReplacedElementFactory {

	private static final String EMPTY_IMAGE_CONTENT = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAAAXNSR0IArs4c6QAAAAlwSFlzAAAL\n"+
			"EwAACxMBAJqcGAAAAAd0SU1FB98EBg0LFXmmUzUAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRo\n"+
			"IEdJTVBXgQ4XAAAADElEQVQI12P4//8/AAX+Av7czFnnAAAAAElFTkSuQmCC\n";


	private java.awt.Image emptyImage;
	private FSImage pdfBoxEmptyImage;

	private LinkedHashMap<String, ReplacedElement> elementCache;

	private ResourceLoader resourceLoader;

	public B64OrPreloadedReplacedElementFactory() {
		super();
		setResourceLoader(new FacesResourceLoader());
		elementCache = new LinkedHashMap<String, ReplacedElement>();

		try {
			byte[] emptyImageBytes = Base64.decodeBase64(EMPTY_IMAGE_CONTENT);
			emptyImage = ImageIO.read(new ByteArrayInputStream(emptyImageBytes));
			pdfBoxEmptyImage = new PdfBoxImage(emptyImageBytes, "");
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
				} catch (IOException e1) {
					fsImage = null;
					e1.printStackTrace();
				}
				if (fsImage != null) {
					if (cssWidth != -1 || cssHeight != -1) {
						fsImage.scale(cssWidth, cssHeight);
					}
					imgElement = new PdfBoxImageElement(fsImage);
				}
			}
			elementCache.put(srcAttribute, imgElement);
		}

		return imgElement;
	}

	protected FSImage buildImage(String srcAttr, UserAgentCallback uac) throws IOException {
		FSImage fsImage;
		try {
			if (srcAttr.startsWith("preload:")) {
				String preloadSrc = srcAttr.substring("preload:".length());
				fsImage = new PdfBoxImage(getResourceLoader().getBytesFromReference(preloadSrc), srcAttr);
			} else if (srcAttr.startsWith("data:image/")) {
				String b64encoded = srcAttr.substring(srcAttr.indexOf("base64,") + "base64,".length(), srcAttr.length());
				byte[] decodedBytes = new BASE64Decoder().decodeBuffer(b64encoded);

				fsImage = new PdfBoxImage(decodedBytes, srcAttr);
			} else if (preloadAllImages) {
				fsImage = new PdfBoxImage(getResourceLoader().getBytesFromReference(srcAttr), srcAttr);
			} else {
				fsImage = uac.getImageResource(srcAttr).getImage();
			}
		} catch(Exception exc) {
			fsImage = pdfBoxEmptyImage;
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