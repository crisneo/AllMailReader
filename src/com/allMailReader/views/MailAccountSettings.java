package com.allMailReader.views;

import com.allMailReader.core.EmailService;

public class MailAccountSettings {
	
	private String accountName;
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public EmailService getService() {
		return service;
	}
	public void setService(EmailService service) {
		this.service = service;
	}
	private String account;
	private String password;
	private EmailService service;

}
