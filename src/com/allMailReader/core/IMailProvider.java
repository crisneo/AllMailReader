package com.allMailReader.core;

import java.util.List;
import java.util.Map;

import javax.mail.Message;

public interface IMailProvider {
	
	boolean connect(Map credentials);
	List<MessageData> getInboxMessages();
	List<MessageData> getInboxMessages(PagingInfo paging);
	//List<Message> getInboxNativeMessages();
	//List<Message> getInboxNativeMessages(PagingInfo paging);
	boolean sendMessage();
	int getMessageCount();
	MessageData getMessage(int messageNumber);
	Message getNativeMessage(int messageNumber);
	boolean disconnect();
	String getMailVendorName();
	void response(Message message, String textResponse);


}
