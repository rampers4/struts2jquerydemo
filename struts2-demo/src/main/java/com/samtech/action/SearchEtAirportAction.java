package com.samtech.action;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;

import com.csair.domains.etdcs.EtAirport;
import com.opensymphony.xwork2.ActionContext;
import com.samtech.hibernate3.EtAirportServiceInf;

public class SearchEtAirportAction extends BaseDaoAction<EtAirport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String id;
	private String airportName;
	EtAirportServiceInf etAirportService;
	List<EtAirport> etairports;
	private String key;
	private boolean isNew=false;

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
		return SUCCESS;
	}

	public List<EtAirport> getEtAirports() {
		return etairports;
	}

	public String saveOrUpdate(){
		String s;
		if(StringUtils.isNotBlank(this.getSaveKey()) && isNew==false){
			try{
			s= this.doUpdateAction();
			}catch(Exception ex){
				s=INPUT;
				
			}
			if(s.equals(SUCCESS))
				addActionMessage(this.getText("save.successful"));
		}else{
			if(this.getTarget().getOpId()==null)this.getTarget().setOpId("");
			if(this.getTarget().getOpDate()==null)getTarget().setOpDate(new Date());
			try{
				s= this.doInsertAction();
			}catch(Exception ex){
				s=INPUT;
				this.addActionMessage(ex.getMessage());
			}
			if(s.equals(SUCCESS)){
				
				this.addActionMessage(this.getText("save.successful"));
				this.setSaveKey(this.getTarget().getAirportCode());
			}else{
				this.setSaveKey(null);
			}
		}
		return s;
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

	@Override
	public String buildKey(Serializable id) {
		return id.toString();
	}

	@Override
	public Serializable getObjectId() {
		return key;
	}

	@Override
	public String getSaveKey() {
		return key;
	}

	@Override
	public String insertResultName() {
		return SUCCESS;
	}

	@Override
	public String queryResultName() {
		return SUCCESS;
	}

	@Override
	public void setSaveKey(String id) {
		key=id;
		if(StringUtils.isNotBlank(key) && this.baseService!=null){
			EtAirport t1 = this.findById(EtAirport.class, key);
			EtAirport target2 = this.getTarget();
			if(t1!=null){
			System.out.println("db cname="+t1.getAirportChBrief()+";page cname="+(target2!=null?target2.getAirportChBrief():""));
			this.setTarget(t1);
			}else {
				if(target2!=null)
				target2.setAirportCode(key);
				isNew=true;
			}	
		}
	}
	
	public String setupModify(){
		String key=this.getSaveKey();
		if(StringUtils.isNotBlank(key) && this.baseService!=null){
			EtAirport t1 = this.findById(EtAirport.class, key);
			this.setTarget(t1);
			//EtAirport target2 = getTarget();
			
			this.setTarget(t1);
		}
		return SUCCESS;
	}
	
	public String doDelete(){
		String s=SUCCESS;
		String key=this.getSaveKey();
		if(StringUtils.isNotBlank(key) && this.baseService!=null){
			EtAirport t1 = this.findById(EtAirport.class, key);
			if(t1!=null){
				try{
					this.setTarget(null);
				this.baseService.deleteObject(t1);
				}catch(Exception ex){
					this.addActionMessage(ex.getMessage());
					s=INPUT;
				}
			}else{
				s=INPUT;
				this.addActionMessage("not found data!");
			}
			this.setTarget(null);
		}
		etAirportList();
		return s;
	}
	
	@Override
	public String updateResultName() {
		return SUCCESS;
	}

	@Override
	protected boolean validation() {
		//EtAirport t = this.getTarget();
		boolean b=true;
		/*if(t.getAirportCode()==null || t.getAirportCode().trim().length()!=3){
			b=false;
			this.addFieldError("saveKey", "请输入3位字母");
		}*/
		/*if(StringUtils.isBlank(t.getAirportChBrief())){
			b=false;
			this.addFieldError("airportChBrief", "中文名不能为空");
		}*/
		Map<String, List<String>> fieldErrors = this.getFieldErrors();
		Collection<String> actionErrors = this.getActionErrors();
		if(fieldErrors!=null && !fieldErrors.isEmpty()){
			b=false;
		}
		if(actionErrors!=null && !actionErrors.isEmpty())
			b=false;
		
		return b;
	}

	public void prepare() throws Exception {
		/*key=this.getSaveKey();
		if(StringUtils.isNotBlank(key) && this.baseService!=null){
			EtAirport t1 = this.findById(EtAirport.class, key);
			EtAirport target2 = this.getTarget();
			System.out.println("db cname="+t1.getAirportChBrief()+";page cname="+target2!=null?target2.getAirportChBrief():"");
			this.setTarget(t1);
		}*/
		key=null;
		isNew=false;
		if(this.getTarget()==null)
			this.setTarget(new EtAirport());
		
	}
	
	public boolean isUpdateble(){
		String saveKey = this.getSaveKey();
		return org.apache.commons.lang.StringUtils.isNotBlank(saveKey);
	}
}
