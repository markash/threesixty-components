window.io_threesixty_ui_components_chart_AbstractHighChart = function () {

	this.onStateChange = function () {
		// read state
		var domId = this.getState().domId;
		var hcjs = this.getState().hcjs;

		var connector = this;

		// evaluate highcharts JS which needs to define var "options"
		eval(hcjs);

		// set chart context
		if (typeof chartType === 'undefined' || chartType == 'HighChart') {
			$('#' + domId).highcharts(options);
		} else {
			$('#' + domId).highcharts(chartType, options);
		}
	};
};