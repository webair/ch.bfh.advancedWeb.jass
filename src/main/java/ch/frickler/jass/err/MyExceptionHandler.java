package ch.frickler.jass.err;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * we use this exception handler for displaying the errors in the console
 * 
 * @author kaeserst
 * 
 */
public class MyExceptionHandler extends ExceptionHandlerWrapper {

	private ExceptionHandler wrapped;

	public MyExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	/**
	 * this method handles the exception and prints the stacktrace to the
	 * console
	 */
	@Override
	public void handle() throws FacesException {
		Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents()
				.iterator();
		while (i.hasNext()) {
			ExceptionQueuedEvent event = (ExceptionQueuedEvent) i.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event
					.getSource();

			Throwable t = context.getException();
			t.printStackTrace();
			i.remove();
		}
		getWrapped().handle();
	}
}