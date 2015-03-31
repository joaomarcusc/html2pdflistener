package br.com.christ.html2pdf.font;

import java.io.IOException;

import org.xhtmlrenderer.css.value.FontSpecification;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.render.FSFont;

import br.com.christ.html2pdf.loader.FacesResourceLoader;
import com.lowagie.text.DocumentException;

public class ITextFacesFontResolver extends ITextFontResolver {
	FacesResourceLoader resourceLoader = new FacesResourceLoader();

	@Override
	public FSFont resolveFont(SharedContext renderingContext, FontSpecification spec) {
		return super.resolveFont(renderingContext, spec);
	}

	@Override
	public void addFont(String path, boolean embedded) throws DocumentException, IOException {
		super.addFont(path, embedded);
	}

	@Override
	public void addFont(String path, String encoding, boolean embedded) throws DocumentException, IOException {
		super.addFont(path, encoding, embedded);
	}

	@Override
	public void addFont(String path, String encoding, boolean embedded, String pathToPFB) throws DocumentException, IOException {
		super.addFont(path, encoding, embedded, pathToPFB);
	}

	@Override
	public void addFont(String path, String fontFamilyNameOverride, String encoding, boolean embedded, String pathToPFB) throws DocumentException, IOException {
		super.addFont(path, fontFamilyNameOverride, encoding, embedded, pathToPFB);
	}

	public ITextFacesFontResolver(SharedContext sharedContext) {
		super(sharedContext);
	}
}
