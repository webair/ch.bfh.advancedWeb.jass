// continues polling when the last request was successfull
function processPoll(t, sourceId, toRender) {
	return function(data) {
		if (data.status == 'success') {
			window.setTimeout("reloadUI("+t+",'" + sourceId + "', '" + toRender + "')", t);
		}
	};
}

// starts the polling using the given interval and renders the "toRender" jsf components
function reloadUI(interval, sourceId, toRender) {
	jsf.ajax.request(sourceId, null, {
		render : toRender,
		onevent: processPoll(interval, sourceId, toRender)
	});
}

// does polling using a hiddenform.
// this way we cann call methods in a bean
function formPoll(timeout, button) {
	document.getElementById(button).click();
	window.setTimeout("formPoll("+timeout+", '"+button+"')", timeout);
}