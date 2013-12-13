package br.com.christ.html2pdf.factory;

import java.io.IOException;

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
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.Base64;


public class B64OrPreloadedReplacedElementFactory implements ReplacedElementFactory {

	private ResourceLoader resourceLoader;

	public B64OrPreloadedReplacedElementFactory() {
		super();
		setResourceLoader(new FacesResourceLoader());
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
		String nodeName = e.getNodeName();
		if (nodeName.equals("img")) {
			String srcAttribute = e.getAttribute("src");
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
				return new ITextImageElement(fsImage);
			}
		}

		return null;
	}

	protected FSImage buildImage(String srcAttr, UserAgentCallback uac) throws IOException, BadElementException {
		FSImage fsImage;
		if(srcAttr.startsWith("preload:")) {
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