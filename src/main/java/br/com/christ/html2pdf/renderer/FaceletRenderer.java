package br.com.christ.html2pdf.renderer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.view.ViewDeclarationLanguage;
import javax.servlet.http.HttpServletResponse;

import br.com.christ.jsf.html2pdf.listener.ResponseCatcher;

public class FaceletRenderer {
	private FacesContext context;

	public FaceletRenderer(FacesContext context) {
		this.context = context;
	}


	public static class WrappedStringFacesContext extends FacesContextWrapper {
		private final StringWriter stringWriter;
		private final ResponseWriter responseWriter;
		private FacesContext facesContext;

		public WrappedStringFacesContext(FacesContext facesContext) {
			this.facesContext = facesContext;
			this.stringWriter = new StringWriter();
			this.responseWriter = createResponseWriter();
		}

		private ResponseWriter createResponseWriter() {
			ExternalContext extContext = getWrapped().getExternalContext();
			Map<String, Object> requestMap = extContext.getRequestMap();
			String contentType = (String) requestMap.get("facelets.ContentType");
			String encoding = (String) requestMap.get("facelets.Encoding");
			RenderKit renderKit = getWrapped().getRenderKit();
			return renderKit.createResponseWriter(stringWriter, contentType, encoding);
		}

		@Override public FacesContext getWrapped() {
			return this.facesContext;
		}

		@Override public ResponseWriter getResponseWriter() {
			return responseWriter;
		}

		public String getHtmlString() {
			return stringWriter.toString();
		}
	}

	private ResponseWriter createResponseWriter(FacesContext context, StringWriter stringWriter) {
		ExternalContext extContext = context.getExternalContext();
		Map<String, Object> requestMap = extContext.getRequestMap();
		String contentType = (String) requestMap.get("facelets.ContentType");
		String encoding = (String) requestMap.get("facelets.Encoding");

		RenderKit renderKit = context.getRenderKit();
		return renderKit.createResponseWriter(stringWriter, contentType, encoding);
	}

	/**
	 * Render the Facelets template specified.
	 *
	 * @param template path to the Facelets template
	 * @return rendered content of the Facelets template
	 */
	public String renderView(String template) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getResponseWriter();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse servletResponse = (HttpServletResponse)externalContext.getResponse();
		ResponseCatcher catcher = new ResponseCatcher(servletResponse);
		StringWriter stringWriter = new StringWriter();
		ResponseWriter originalWriter = facesContext.getResponseWriter();
		facesContext.setResponseWriter(createResponseWriter(facesContext, stringWriter));
		Map<Object, Object> oldAttributeMap = new HashMap<>();
		oldAttributeMap.putAll(facesContext.getAttributes());
//		externalContext.setResponse(catcher);
		try {
			// create a UIViewRoot instance using the template specified
			ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
			UIViewRoot view = viewHandler.createView(facesContext, template);
			// the fun part -- do the actual rendering here
			ViewDeclarationLanguage vdl = viewHandler
					.getViewDeclarationLanguage(facesContext, template);
			vdl.buildView(facesContext, view);
			renderChildren(facesContext, view);
			return stringWriter.toString();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		} finally {
			if (originalWriter != null) {
				facesContext.setResponseWriter(originalWriter);
			}
			// Remover os atributos do FacesContext. Ver documentação do método.
			clearContextAttributes(facesContext, oldAttributeMap);
		}
	}


	/**
	 * The outputStylesheet renderer saves properties inside FacesContext's getAttributes
	 * to avoid the same tag to be rendered twice.
	 * This causes problems with FaceletRenderer, because, if you want to call it more than once
	 * inside the same request, the same CSS will eventually need to be included.
	 *
	 * Thus, we need to remove all the new FacesContext attributes after the rendering.
	 *
	 * @param oldAttributeMap Old map of FacesContext attributes.
	 */
	private void clearContextAttributes(FacesContext facesContext, final Map<Object, Object> oldAttributeMap) {
		Set<Object> currKeys = new HashSet<>();
		currKeys.addAll(facesContext.getAttributes().keySet());
		Set<Object> oldKeys = oldAttributeMap.keySet();
		// Remover as chaves já existentes
		currKeys.removeAll(oldKeys);
		// As que ficaram são as novas, que precisam ser removidas.
		for (Object currKey : currKeys) {
			facesContext.getAttributes().remove(currKey);
		}
	}

	/**
	 * Create ResponseWriter. Taken from FaceletViewDeclarationLanguage.java of
	 * MyFaces.
	 */

	/**
	 * Render a UIComponent. Taken from JSF.java of Seam 2.2.
	 */
	private void renderChildren(FacesContext context, UIComponent component)
			throws IOException {
		List<UIComponent> children = component.getChildren();
		for (int i = 0, size = component.getChildCount(); i < size; i++) {
			UIComponent child = (UIComponent) children.get(i);
			renderChild(context, child);
		}
	}

	/**
	 * Render the child and all its children components.
	 */
	private void renderChild(FacesContext context, UIComponent child)
			throws IOException {
		if (child.isRendered()) {
			child.encodeAll(context);
		}
	}
}
