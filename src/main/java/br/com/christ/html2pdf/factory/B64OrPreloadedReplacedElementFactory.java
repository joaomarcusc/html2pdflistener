package br.com.christ.html2pdf.factory;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.Base64;
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

import javax.faces.context.FacesContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class B64OrPreloadedReplacedElementFactory implements ReplacedElementFactory {

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
				fsImage = null;
			} catch (IOException e1) {
				fsImage = null;
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
			InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(preloadSrc);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = stream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			fsImage = new ITextFSImage(Image.getInstance(buffer.toByteArray()));
		} else if (srcAttr.startsWith("data:image/")) {
			String b64encoded = srcAttr.substring(srcAttr.indexOf("base64,") + "base64,".length(), srcAttr.length());
			byte[] decodedBytes = Base64.decode(b64encoded);

			fsImage = new ITextFSImage(Image.getInstance(decodedBytes));
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

}