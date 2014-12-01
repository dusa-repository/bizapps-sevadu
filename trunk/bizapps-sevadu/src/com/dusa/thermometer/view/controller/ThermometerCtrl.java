package com.dusa.thermometer.view.controller;

import java.util.Date;
import java.util.HashMap;

import modelo.maestros.MaestroAliado;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Html;

import com.dusa.thermometer.Test;
import com.dusa.thermometer.service.bf.ProcessDataBf;
import com.dusa.thermometer.service.bf.ThermometerBf;
import com.dusa.thermometer.service.to.ThermometerTo;

public class ThermometerCtrl extends SelectorComposer<Component> {

	private static final long serialVersionUID = -7117413922267074045L;

	@Wire
	private Html htmlContainer;

	private ThermometerBf thermometerBf = new ThermometerBf();
	private ProcessDataBf processDataBf = new ProcessDataBf();
	MaestroAliado aliado = new MaestroAliado();
	Date fechaDesde = null;
	Date fechaHasta = null;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("termometro");
		if (map != null) {
			if (map.get("id") != null) {
				aliado = (MaestroAliado) map.get("aliado");
				fechaDesde = (Date) map.get("desde");
				fechaHasta = (Date) map.get("hasta");
			}
			map.clear();
			map = null;
		}

		// Get data from Business Facade -> Data access object.
		ThermometerTo thermometer = new ThermometerTo();
		thermometer.setData(Test.getTestData(aliado, fechaDesde, fechaHasta));
		thermometer.setLabels(Test.getTestHeaders());

		// Call business facade for totaling of data.
		processDataBf.calculateTotals(thermometer.getData());
		// Call business facade for HTML generation.
		String htmlDom = thermometerBf.generateThermometer(thermometer);
		htmlContainer.setContent(htmlDom);
	}
}
