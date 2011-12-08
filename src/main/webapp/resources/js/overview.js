function reloadTable () {
	var source = window.document.getElementById("OverviewForm:GameLink");
	jsf.ajax.request(source, event, {value:'show',render:"OverviewForm:games"});
	window.setTimeout("reloadTable()", 10000);
}

window.setTimeout("reloadTable()", 10000);