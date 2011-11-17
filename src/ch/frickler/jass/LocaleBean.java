package ch.frickler.jass;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class LocaleBean {
	private String locale;
	
	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLocale() {
		if (locale == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			return context.getViewRoot().getLocale().toString();
		}
		return locale;
	}
}
