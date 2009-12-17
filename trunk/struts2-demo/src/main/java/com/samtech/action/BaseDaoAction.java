package com.samtech.action;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.samtech.hibernate3.BaseServiceInf;

@SuppressWarnings("serial")
public abstract class BaseDaoAction<T> extends ActionSupport implements Preparable, ServletRequestAware,ServletResponseAware{
	/**
	 * 
	 */
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	protected BaseServiceInf baseService;
	private String message;
	
	private T target;
	
	public T getTarget() {
		return target;
	}
	public void setTarget(T target) {
		this.target = target;
	}
	public abstract String getSaveKey();
	public abstract void setSaveKey(String id);
	public abstract Serializable getObjectId();
	public abstract String buildKey(Serializable id);
	
	public String doQueryAction(){
		
		return queryResultName();
	}
	
	abstract protected boolean validation();
	
	public String doUpdateAction(){
		if(validation()){
			this.baseService.updateObject(target);
		}else{
			return INPUT;
		}
		return updateResultName();
	}
	
	public String doInsertAction(){
		if(validation()){
			
			this.baseService.saveObject(target);
		}else{
			return INPUT;
		}
		return insertResultName();
	}
	
	public abstract String queryResultName();
	
	public abstract String updateResultName();
	public abstract String insertResultName();
	
	
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}
	
	protected HttpServletResponse getServletResponse() {
		return this.response;
	}
	
	public void setServletRequest(HttpServletRequest request){
		this.request=request;
	}
	
	protected HttpServletRequest getServletRequest(){
		return request;
	}
	
	protected <T> T findById(Class<T> c,Serializable id){
		
		return (T) this.baseService.getObject(c, id);
	}
	
	public void setBaseService(BaseServiceInf baseService){
		this.baseService=baseService;
	}

}
