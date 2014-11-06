package br.com.christ.html2pdf.converter;

public class SavedState {
	private String viewId;
	private Object viewState;

	public SavedState(String viewId, Object viewState) {
		this.viewId = viewId;
		this.viewState = viewState;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public Object getViewState() {
		return viewState;
	}

	public void setViewState(Object viewState) {
		this.viewState = viewState;
	}
}
