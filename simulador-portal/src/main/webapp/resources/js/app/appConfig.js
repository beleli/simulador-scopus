app.value("appConfig", {
	contextName : document.location.pathname.split("/")[1],
	baseProject : "/simulador-portal"
});

app.config([ 'growlProvider', function(growlProvider) {
	growlProvider.globalDisableCountDown(true);
	growlProvider.globalTimeToLive({
		success : 6000,
		error : 6000,
		warning : 6000,
		info : 6000
	});
} ]);

app.config(function($mdDateLocaleProvider) {

	$mdDateLocaleProvider.formatDate = function(date) {
		if (date != undefined) {
			return moment(date).format('DD/MM/YYYY');	
		} else  {
			return null;
		}
	};

	$mdDateLocaleProvider.parseDate = function(dateString) {
	    var m = moment(dateString, 'DD/MM/YYYY', true);
	    return m.isValid() ? m.toDate() : new Date(NaN);
	};
   
   
});