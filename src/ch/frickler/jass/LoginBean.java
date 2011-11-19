package ch.frickler.jass;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class LoginBean {

	private String locale;

	public String login() {
		// TODO check user/pw, login and redirect to the overview page
		return "overview?faces-redirect=true";
	}

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