package com.samtech.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeImpl;
import org.jmesa.limit.Filter;
import org.jmesa.limit.FilterSet;
import org.jmesa.limit.Limit;
import org.jmesa.limit.Order;
import org.jmesa.limit.RowSelect;
import org.jmesa.limit.Sort;
import org.jmesa.limit.SortSet;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlRow;
import org.jmesa.view.html.component.HtmlTable;
import org.jmesa.view.html.event.MouseRowEvent;

import com.csair.domains.etdcs.TargetCustomerTicket;
import com.samtech.hibernate3.EtAirportServiceInf;
import com.samtech.hibernate3.PagingAndSorting;

public class TravelersKeywordsAction extends BaseDaoAction<TargetCustomerTicket>
		implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String airportName;
	private EtAirportServiceInf etAirportService;
	private List<TargetCustomerTicket> etairports;
	private HttpServletRequest request;
	private InputStream pgtableResult;

	private void setPgInputStream(InputStream in) {
		pgtableResult = in;
	}

	public InputStream getPgInputStream() {
		/*
		 * ByteArrayInputStream inputStream = new
		 * ByteArrayInputStream("dd".getBytes()); return inputStream;
		 */
		return pgtableResult;
	}

	public String doSearch() {
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String paging() {
		String tblid = "traveler_tbl";

		TableFacade tableFacade = new TableFacadeImpl(tblid, request);
		String keywords = request.getParameter(tblid + "_keywords");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		if (keywords != null && keywords.trim().length()>0) {
			
				
				
				HttpSession session = request.getSession();
				if (session != null) {
					session.setAttribute(tblid + "_keywords", keywords);
					
				}
			

		} else {
			HttpSession session = request.getSession();
			if (session != null) {
				keywords = (String) session.getAttribute(tblid + "_keywords");
				
			}
		}
		tableFacade.setStateAttr("restore");
		tableFacade.setMaxRows(10);
		Map<String, Object> param = new HashMap(2);
		PagingAndSorting pg = new PagingAndSorting();
		etairports=Collections.EMPTY_LIST;
		String sql = "select o from " + TargetCustomerTicket.class.getName()
				+ " as o ";
		if (keywords != null && keywords.trim().length()>0) {
			Map<String, Float> properties=new HashMap<String,Float>(3);
			properties.put("depAirport", 1f);
			properties.put("psgName", 0.8f);
			properties.put("certificateId", 0.6f);
			properties.put("certificateType", 0.5f);
			etairports = this.baseService.searchByKeywords(TargetCustomerTicket.class, properties, keywords);
		}
		tableFacade.setTotalRows(etairports.size());
		tableFacade.setMaxRowsIncrements(10, 20, 50);
		Limit limit = tableFacade.getLimit();
		RowSelect rowSelect = limit.getRowSelect();
		SortSet sortSet = limit.getSortSet();
		if (sortSet != null) {
			Collection<Sort> sorts = sortSet.getSorts();
			if (sorts != null) {
				for (Iterator iterator = sorts.iterator(); iterator.hasNext();) {
					Sort sort = (Sort) iterator.next();
					Order order = sort.getOrder();
					String s = "";
					if (order.equals(Order.ASC))
						s = "asc";
					if (order.equals(Order.DESC))
						s = "desc";
					int position = sort.getPosition();
					String property = sort.getProperty();
					System.out.println("{property:'" + property + "',position:"
							+ position + ",order:'" + s + "'}");
				}
			}
		}
		if (rowSelect != null) {
			int rowStart = rowSelect.getRowStart();
			int rowEnd = rowSelect.getRowEnd();
			int page = rowSelect.getPage();
			System.out.println("rowstart=" + rowStart + ";rowend=" + rowEnd
					+ ";page=" + page);
			ArrayList rs = new ArrayList(10);
			for (int i = rowStart; i < rowEnd && i < etairports.size(); i++) {
				rs.add(etairports.get(i));
			}
			tableFacade.setItems(rs);
		}
		final String buildTable = buildTable(tableFacade);
		ByteArrayInputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(buildTable.getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setPgInputStream(inputStream);
		return "pgresult";
	}

	@Override
	public String execute() throws Exception {

		String tblid = "etairportTbl";
		TableFacade tableFacade = new TableFacadeImpl(tblid, request);

		tableFacade.setStateAttr("restore");
		tableFacade.setEditable(false);

		tableFacade.setMaxRows(10);
		// tableFacade.setTotalRows(etairports.size());
		tableFacade.setMaxRowsIncrements(10, 20, 50);
		Limit limit = tableFacade.getLimit();

		RowSelect rowSelect = limit.getRowSelect();

		FilterSet filterSet = limit.getFilterSet();
		Collection<Filter> filters = filterSet.getFilters();

		// tableFacade.setItems(etairports);

		request.setAttribute("traveler_tbl", "");// buildTable(tableFacade)
		return SUCCESS;
	}

	private String buildTable(TableFacade tableFacade) {
		tableFacade.setColumnProperties("psgName", "OFltNo", "fltDate",
				"depAirport", "arrAirport");

		HtmlTable table = (HtmlTable) tableFacade.getTable();
		table.getTableRenderer().setWidth("350px");
		HtmlRow row = table.getRow();
		row.setFilterable(Boolean.FALSE);
		MouseRowEvent onmouseout = new MouseRowEvent();
		onmouseout.setStyleClass("odd");
		MouseRowEvent onmouseover = new MouseRowEvent();
		onmouseout.setStyleClass("even");
		row.setOnmouseout(onmouseout);
		// table.setCaption("测试用户信息列表");

		HtmlColumn id = row.getColumn("psgName");
		id.setFilterable(false);
		id.setTitle("旅客");
		id.setSortable(Boolean.FALSE);
		HtmlColumn firstName = row.getColumn("OFltNo");
		firstName.setTitle("航班");

		HtmlColumn deleteAction = row.getColumn("arrAirport");
		deleteAction.setTitle("到达");
		deleteAction.setSortable(Boolean.FALSE);

		deleteAction.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowcount) {
				Object value = new BasicCellEditor().getValue(item, property,
						rowcount);
				HtmlBuilder html = new HtmlBuilder();

				/*
				 * Object id = ItemUtils.getItemValue(item, "airportCode");
				 * 
				 * html.a().href().quote().append(request.getContextPath()+
				 * "/setupModify.action?saveKey="+id).quote().close();
				 * html.img()
				 * .src(request.getContextPath()+"/images/pencil.png").
				 * border("none").end(); html.aEnd();
				 */
				if (value != null
						&& value.toString().toUpperCase().equals("CAN")) {
					html.append(value.toString()).append("(gz)");
				} else
					html.append(value);
				return html.toString();
			}
		});
		HtmlColumn arrAction = row.getColumn("depAirport");
		arrAction.setTitle("起飞");
		arrAction.setSortable(Boolean.FALSE);

		arrAction.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowcount) {
				Object value = new BasicCellEditor().getValue(item, property,
						rowcount);
				HtmlBuilder html = new HtmlBuilder();

				/*
				 * Object id = ItemUtils.getItemValue(item, "airportCode");
				 * 
				 * html.a().href().quote().append(request.getContextPath()+
				 * "/setupModify.action?saveKey="+id).quote().close();
				 * html.img()
				 * .src(request.getContextPath()+"/images/pencil.png").
				 * border("none").end(); html.aEnd();
				 */
				if (value != null
						&& value.toString().toUpperCase().equals("CAN")) {
					html.append(value.toString()).append("(gz)");
				} else
					html.append(value);
				return html.toString();
			}
		});
		HtmlColumn fltdateAction = row.getColumn("fltDate");
		fltdateAction.setTitle("起飞日期");
		fltdateAction.setSortable(Boolean.FALSE);

		fltdateAction.getCellRenderer().setCellEditor(new CellEditor() {
			private Format sm = new SimpleDateFormat("dd/MMM", Locale.ENGLISH);

			public Object getValue(Object item, String property, int rowcount) {
				Object value = new BasicCellEditor().getValue(item, property,
						rowcount);
				HtmlBuilder html = new HtmlBuilder();
				html.append(sm.format(value));
				return html.toString();
			}
		});
		return tableFacade.render();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public EtAirportServiceInf getEtAirportService() {
		return etAirportService;
	}

	public void setEtAirportService(EtAirportServiceInf etAirportService) {
		this.etAirportService = etAirportService;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String buildKey(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable getObjectId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSaveKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insertResultName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryResultName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSaveKey(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public String updateResultName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean validation() {
		// TODO Auto-generated method stub
		return false;
	}

	public void prepare() throws Exception {
		// TODO Auto-generated method stub

	}

}
