package ch.frickler.jass.err;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * this exception handler factory is used to instantiate our own exception handler class
 * @author kaeserst
 *
 */
public class MyExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;

	public MyExceptionHandlerFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		ExceptionHandler result = new MyExceptionHandler(
				parent.getExceptionHandler());
		return result;
	}
}