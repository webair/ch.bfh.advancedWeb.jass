function reloadTable () {
	var source = window.document.getElementById("OverviewForm:GameLink");
	jsf.ajax.request(source, event, {value:'show',render:"OverviewForm:games"});
	window.setTimeout("reloadTable()", 2000);
}

window.setTimeout("reloadTable()", 2000);