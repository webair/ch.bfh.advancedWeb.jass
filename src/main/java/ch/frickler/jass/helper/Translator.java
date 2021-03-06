package ch.frickler.jass.helper;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * helper class for creating translated FacesMessages
 * 
 */
public class Translator {

	private static final String UI_PROPERTY = "ch.frickler.jass.res.Lang";

	public static FacesMessage getMessage(FacesContext ctx, String message) {
		ResourceBundle bundle = ResourceBundle.getBundle(UI_PROPERTY, ctx
				.getViewRoot().getLocale());

		return new FacesMessage(bundle.getString(message));
	}
	
	public static String getString(FacesContext ctx, String message) {
		ResourceBundle bundle = ResourceBundle.getBundle(UI_PROPERTY, ctx
				.getViewRoot().getLocale());

		return bundle.getString(message);
	}
	

}
