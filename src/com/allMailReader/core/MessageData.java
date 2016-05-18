package com.allMailReader.core;

import java.util.Date;

public class MessageData {
	
	private String subject;
	private String content;
	private Date date;
	private EmailService emailService;
	private int messageNumber;
	private String sender;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public int getMessageNumber() {
		return messageNumber;
	}
	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public EmailService getEmailService() {
		return emailService;
	}
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	

}

