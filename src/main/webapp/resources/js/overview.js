INTERVAL = 10000;

function processPoll(t) {
	return function(data) {
		if (data.status == 'success') {
			window.setTimeout("reloadTable()", t);
		}
	};
}

function reloadTable() {
	var source = window.document.getElementById("OverviewForm:GameLink");
	jsf.ajax.request(source, null, {
		value : 'show',
		render : "OverviewForm:games",
		onevent: processPoll(INTERVAL)
	});
}

window.setTimeout("reloadTable()", INTERVAL);