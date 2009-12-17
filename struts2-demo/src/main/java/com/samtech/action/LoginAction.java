package com.samtech.action;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.csair.domains.etdcs.SvcUserAccount;
import com.samtech.exception.LoginException;

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

	public String doLoginAction() {
		try {
			user = logined(this.getUsername(), this.getPassword());
			HttpSession session = this.getServletRequest().getSession();
			if (session == null)
				session = this.getServletRequest().getSession(true);
			session.setAttribute(LOGIN_KEY, Boolean.TRUE);
			session.setAttribute(USER_INFO_KEY, user);
		} catch (LoginException e) {
			this.addActionError(e.getMessage());
			return INPUT;
		}

		return SUCCESS;
	}

	public String doLogoutAction() {

		HttpSession session = this.getServletRequest().getSession();
		logined = false;
		user = null;
		if (session != null) {
			session.removeAttribute(LOGIN_KEY);
			session.removeAttribute(USER_INFO_KEY);
			session.invalidate();
		}

		return SUCCESS;
	}

	private SvcUserAccount logined(String u, String p) throws LoginException {

		return null;
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
	}

}
