package com.samtech.action;

import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.csair.domains.etdcs.EtAirport;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.samtech.hibernate3.EtAirportServiceInf;

public class GtGridAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EtAirportServiceInf etAirportService;

	@SuppressWarnings("unchecked")
	public Class getBeanClass() {
		// return getClass();
		return EtAirport.class;
	}

	@SuppressWarnings("unchecked")
	@JSON(name = "data")
	public List getPageData() {

		return null;
	}

	@JSON(name = "exception")
	public String getException() {
		return Action.ERROR;
	}

	@JSON(name = "success")
	public boolean isSuccess() {
		return true;
	}

	// @Action(value="/workbench/load", results={@Result(type="json")})
	public String load() {
		etAirportService.getAllEtAirports();
		// pageData=list;List<EtAirport> list ;
		return SUCCESS;
	}

	@SuppressWarnings("unused")
	private EtAirportServiceInf getEtAirportService() {
		return etAirportService;
	}

	public void setEtAirportService(EtAirportServiceInf etAirportService) {
		this.etAirportService = etAirportService;
	}

}
