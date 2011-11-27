package ch.frickler.jass.helper;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * helper class for creating translated FacesMessages
 * 
 */
public class MessageHelper {

	private static final String UI_PROPERTY = "ch.frickler.jass.res.UI";

	public static FacesMessage getMessage(FacesContext ctx, String message) {
		ResourceBundle bundle = ResourceBundle.getBundle(UI_PROPERTY, ctx
				.getViewRoot().getLocale());

		return new FacesMessage(bundle.getString(message));
	}

}
