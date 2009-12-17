package com.samtech.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;

import com.csair.domains.etdcs.EtAirport;
import com.opensymphony.xwork2.ActionSupport;
import com.samtech.hibernate3.EtAirportServiceInf;

public class JqueryGridAction extends ActionSupport implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Log    log   = LogFactory.getLog(JqueryGridAction.class);

	private EtAirportServiceInf etAirportService;
	
	private List<EtAirport> gridModel;
	private List<EtAirport> myCustomers;
	private Integer rows = 0;
	private Integer page = 0;
	private Integer total = 0;
	private Integer record = 0;
	private String sord;
	private String sidx;
	private String searchField;
	private String searchString;
	private String searchOper;
	private boolean loadonce = false;
	private Map<String, Object> session;

	//Actions( { @Action(value = "/jsontable", results = { @Result(name = "success", type = "json") }) })
	@SuppressWarnings("unchecked")
	public String execute() {

		log.info("build json table");
		log.debug("Page :" + getPage());
		log.debug("Rows :" + getRows());
		log.debug("Sorting Order :" + getSord());
		log.debug("Index Row :" + getSidx());
		log.debug("Search :" + searchField + " " + searchOper + " "
				+ searchString);

		Object list = session.get("mylist");
		if (list != null) {
			myCustomers = (List<EtAirport>) list;
		} else {
			log.debug("Build new List");
			myCustomers = etAirportService.getAllEtAirports();//CustomerDAO.buildList();
		}

		if (getSord() != null && getSord().equalsIgnoreCase("asc")) {
			//Collections.sort(myCustomers);
		}
		if (getSord() != null && getSord().equalsIgnoreCase("desc")) {
			/*Collections.sort(myCustomers);
			Collections.reverse(myCustomers);*/
		}
		
		setRecord(myCustomers!=null?myCustomers.size():0);

		int to = (getRows() * getPage());
		int from = to - getRows();

		if (to > getRecord())
			to = getRecord();

		if (loadonce) {
			setGridModel(myCustomers);
		} else {
			if (searchString != null && searchOper != null) {
				int id = Integer.parseInt(searchString);
				if (searchOper.equalsIgnoreCase("eq")) {
					log.debug("search id equals " + id);
					List<EtAirport> cList = new ArrayList<EtAirport>();
					//cList.add(CustomerDAO.findById(myCustomers, id));
					setGridModel(cList);
				} else if (searchOper.equalsIgnoreCase("ne")) {
					log.debug("search id not " + id);
					//setGridModel(CustomerDAO.findNotById(myCustomers, id, from,to));
				} else if (searchOper.equalsIgnoreCase("lt")) {
					log.debug("search id lesser then " + id);
					//setGridModel(CustomerDAO.findLesserAsId(myCustomers, id, from, to));
				} else if (searchOper.equalsIgnoreCase("gt")) {
					log.debug("search id greater then " + id);
					//setGridModel(CustomerDAO.findGreaterAsId(myCustomers, id,from, to));
				}
			} else {
				if(myCustomers!=null){
					List<EtAirport> list2 = myCustomers.subList(from, to);
					setGridModel(list2);
				}
			}
		}

		setTotal((int) Math.ceil((double) getRecord() / (double) getRows()));

		session.put("mylist", myCustomers);

		return SUCCESS;
	}

	public String getJSON() {
		return execute();
	}

	/**
	 * @return how many rows we want to have into the grid
	 */
	public Integer getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            how many rows we want to have into the grid
	 */
	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * @return current page of the query
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * @param page
	 *            current page of the query
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * @return total pages for the query
	 */
	public Integer getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            total pages for the query
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}

	/**
	 * @return total number of records for the query. e.g. select count(*) from
	 *         table
	 */
	public Integer getRecord() {
		return record;
	}

	/**
	 * @param record
	 *            total number of records for the query. e.g. select count(*)
	 *            from table
	 */
	public void setRecord(Integer record) {

		this.record = record;

		if (this.record > 0 && this.rows > 0) {
			this.total = (int) Math.ceil((double) this.record
					/ (double) this.rows);
		} else {
			this.total = 0;
		}
	}

	/**
	 * @return an collection that contains the actual data
	 */
	public List<EtAirport> getGridModel() {
		if(gridModel!=null && !gridModel.isEmpty()){
			for (EtAirport airport : gridModel) {
				airport.setWeather(airport.getAirportCode());
			}
		}
		return gridModel;
	}

	/**
	 * @param gridModel
	 *            an collection that contains the actual data
	 */
	public void setGridModel(List<EtAirport> gridModel) {
		this.gridModel = gridModel;
	}

	/**
	 * @return sorting order
	 */
	public String getSord() {
		return sord;
	}

	/**
	 * @param sord
	 *            sorting order
	 */
	public void setSord(String sord) {
		this.sord = sord;
	}

	/**
	 * @return get index row - i.e. user click to sort.
	 */
	public String getSidx() {
		return sidx;
	}

	/**
	 * @param sidx
	 *            get index row - i.e. user click to sort.
	 */
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
	}

	public void setLoadonce(boolean loadonce) {
		this.loadonce = loadonce;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	/*private EtAirportServiceInf getEtAirportService() {
		return etAirportService;
	}*/

	public void setEtAirportService(EtAirportServiceInf etAirportService) {
		this.etAirportService = etAirportService;
	}
}
