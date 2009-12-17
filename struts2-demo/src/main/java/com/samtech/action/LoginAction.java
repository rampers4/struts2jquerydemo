package com.samtech.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.csair.domains.etdcs.SvcUserAccount;
import com.opensymphony.xwork2.ActionContext;
import com.samtech.exception.LoginException;
import com.samtech.hibernate3.UserAccountServiceInf;

public class LoginAction extends BaseDaoAction<SvcUserAccount> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2414729349608270422L;
	final String LOGIN_KEY = "user_logined";
	final String USER_INFO_KEY = "user_info";
	private SvcUserAccount user = null;
	private boolean logined = false;
	private String password, username;
	private UserAccountServiceInf userAccountService;

	public String doLoginAction() {
		try {
			Map<String, List<String>> fieldErrors = this.getFieldErrors();
			if(fieldErrors!=null && !fieldErrors.isEmpty()){
				return SUCCESS;
			}
			user = logined(this.getUsername(), this.getPassword());
			Map<String, Object> smap = ActionContext.getContext().getSession();
			HttpSession session = this.getServletRequest().getSession();
			if (session == null)
				session = this.getServletRequest().getSession(true);
			session.setAttribute(LOGIN_KEY, Boolean.TRUE);
			session.setAttribute(USER_INFO_KEY, user);
			if(smap!=null){
				smap.put(LOGIN_KEY, Boolean.TRUE);
				smap.put(USER_INFO_KEY, user);
			}
		} catch (LoginException e) {
			this.addActionError(e.getMessage());
			return SUCCESS;
		}

		return SUCCESS;
	}

	public String doLogoutAction() {

		HttpSession session = this.getServletRequest().getSession();
		logined = false;
		user = null;
		Map<String, Object> smap = ActionContext.getContext().getSession();
		
		
		if(smap!=null){
			smap.remove(LOGIN_KEY);
			smap.remove(USER_INFO_KEY);
		}
		if (session != null) {
			session.removeAttribute(LOGIN_KEY);
			session.removeAttribute(USER_INFO_KEY);
			session.invalidate();
		}

		return SUCCESS;
	}

	private SvcUserAccount logined(String u, String p) throws LoginException {
		try {
			return this.userAccountService.login(u, p);
		} catch (javax.security.auth.login.LoginException e) {
			throw new LoginException(e.getMessage());
		}
		
	}

	@Override
	public String buildKey(Serializable id) {

		return null;
	}

	@Override
	public Serializable getObjectId() {

		return null;
	}

	@Override
	public String getSaveKey() {

		return null;
	}

	@Override
	public String insertResultName() {

		return null;
	}

	@Override
	public String queryResultName() {

		return null;
	}

	@Override
	public void setSaveKey(String id) {

	}

	@Override
	public String updateResultName() {

		return null;
	}

	@Override
	protected boolean validation() {

		return false;
	}

	public SvcUserAccount getSvcUser() {
		return user;
	}

	public boolean isLogined() {
		return logined;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void prepare() throws Exception {
		HttpServletRequest request = getServletRequest();
		HttpSession session = request.getSession();
		logined = false;
		user = null;
		if (session != null) {

			Object o = session.getAttribute(LOGIN_KEY);
			if (o != null && ((Boolean) o).booleanValue()) {
				logined = true;

				user = (SvcUserAccount) session.getAttribute(USER_INFO_KEY);
			}
		}
		Map<String, Object> smap = ActionContext.getContext().getSession();
		if(smap!=null){
			Object o=smap.get(LOGIN_KEY);
			if (o != null && ((Boolean) o).booleanValue()) {
				logined = true;
				user = (SvcUserAccount)smap.get(USER_INFO_KEY);
			}

		}
	}
	
	public void setUserAccountService( UserAccountServiceInf service){
		this.userAccountService=service;
	}

}
