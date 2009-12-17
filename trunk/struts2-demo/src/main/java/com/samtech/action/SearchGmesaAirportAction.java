package com.samtech.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.jmesa.util.ItemUtils;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlRow;
import org.jmesa.view.html.component.HtmlTable;
import org.jmesa.view.html.event.MouseRowEvent;

import com.csair.domains.etdcs.EtAirport;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.samtech.hibernate3.EtAirportServiceInf;

public class SearchGmesaAirportAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String airportName;
	private EtAirportServiceInf etAirportService;
	private List<EtAirport> etairports;
	private HttpServletRequest request;
	private InputStream pgtableResult;

	public String etAirportList() {
		etairports = etAirportService.getAllEtAirports();
		/*
		 * if(etairports!=null && !etairports.isEmpty()){ EtAirport airport =
		 * etairports.get(0); airport.setAirportChBrief("广州");
		 * etAirportService.updateEtAirport(airport); }
		 */
		ActionContext context = ActionContext.getContext();
		Map<String, Object> session = context.getSession();
		session.put("key1", "value");// ${sessionScope.key1}
		return "etairports";
	}

	public List<EtAirport> getEtAirports() {
		return etairports;
	}

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

	public String paging() {
		String tblid = "etairportTbl";
		if (etairports == null) {
			etairports = etAirportService.getAllEtAirports();
		}
		TableFacade tableFacade = new TableFacadeImpl(tblid, request);
		tableFacade.setStateAttr("restore");
		tableFacade.setMaxRows(10);
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
		if (etairports == null) {
			etairports = etAirportService.getAllEtAirports();
		}
		String tblid = "etairportTbl";
		TableFacade tableFacade = new TableFacadeImpl(tblid, request);

		tableFacade.setStateAttr("restore");
		tableFacade.setEditable(false);

		tableFacade.setMaxRows(10);
		tableFacade.setTotalRows(etairports.size());
		tableFacade.setMaxRowsIncrements(10, 20, 50);
		// tableFacade.setItems(etairports);
		Limit limit = tableFacade.getLimit();

		RowSelect rowSelect = limit.getRowSelect();
		int maxRows = rowSelect.getMaxRows();

		FilterSet filterSet = limit.getFilterSet();
		Collection<Filter> filters = filterSet.getFilters();
		if (filters != null) {
			for (Iterator iter = filters.iterator(); iter.hasNext();) {
				Filter filter = (Filter) iter.next();
			}
		}
		tableFacade.setItems(etairports);
		// limit.get
		if (limit.isExported()) {
			// export
		}
		request.setAttribute("etairport_tbl", buildTable(tableFacade));
		return SUCCESS;
	}

	private String buildTable(TableFacade tableFacade) {
		tableFacade.setColumnProperties("airportCode", "airportChBrief",
				"weather");

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

		HtmlColumn id = row.getColumn("airportCode");
		id.setFilterable(false);
		id.setTitle("代码");
		id.setSortable(Boolean.FALSE);
		HtmlColumn firstName = row.getColumn("airportChBrief");
		firstName.setTitle("名字");

		HtmlColumn deleteAction = row.getColumn("weather");
		deleteAction.setTitle("操作");
		deleteAction.setSortable(Boolean.FALSE);
		// Using an anonymous class to implement a custom editor.
		// 用于演示在表格中增加超链接
		/*
		 * firstName.getCellRenderer().setCellEditor(new CellEditor() { public
		 * Object getValue(Object item, String property, int rowcount) { Object
		 * value = new BasicCellEditor().getValue(item, property, rowcount);
		 * HtmlBuilder html = new HtmlBuilder();
		 * html.a().href().quote().append("http://www.mobile-soft.cn")
		 * .quote().close(); html.append(value); html.aEnd(); return
		 * html.toString(); } });
		 */

		// Using an anonymous class to implement a custom editor.
		// 用于演示在表格中增加javascript操作,通过jquery来实现ajax式的删除操作
		deleteAction.getCellRenderer().setCellEditor(new CellEditor() {
			public Object getValue(Object item, String property, int rowcount) {
				Object value = new BasicCellEditor().getValue(item, property,
						rowcount);
				HtmlBuilder html = new HtmlBuilder();
				// 取得每一行的id号
				Object id = ItemUtils.getItemValue(item, "airportCode");
				String js = " onclick='javascript:del(\"tableId\"," + id + ")'"; //
				html.a().append(js).href().quote().append(
						request.getContextPath()
								+ "/setupModify.action?saveKey=" + id).quote()
						.close();
				html.img().src(request.getContextPath() + "/images/pencil.png")
						.border("none").end();
				html.aEnd();
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

}
