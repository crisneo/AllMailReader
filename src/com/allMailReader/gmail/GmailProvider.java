package com.allMailReader.gmail;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.allMailReader.core.EmailUtils;
import com.allMailReader.core.IMailProvider;
import com.allMailReader.core.MessageData;
import com.allMailReader.core.PagingInfo;

public class GmailProvider implements IMailProvider {

	Store store;
	Session session;
	Folder inbox;
	String user;
	String pass;

	@Override
	public boolean connect(Map credentials) {
		// TODO Auto-generated method stub
		try{
			if(store==null || !store.isConnected() || inbox==null){
				Properties props = System.getProperties();
				props.setProperty("mail.store.protocol", "imaps");
		
				session = Session.getDefaultInstance(props, null);
				store = session.getStore("imaps");
				user = credentials.get("mailAccount").toString();
				pass = credentials.get("password").toString();
				store.connect("imap.gmail.com", user, pass );
				inbox = store.getFolder("Inbox");
				return true;
			}
			return true;
			
		}
		catch(Exception exc){
			return false;
		}
	}
	
	
	
	public  ArrayList<MessageData> getMessageObjects(Message[] msgs){
		ArrayList<MessageData> messages=new ArrayList<MessageData>();
		try{
		for(Message msg : msgs){
			MessageData data =new MessageData();
			//data.setContent(EmailUtils.getMessageContent(msg));
			data.setSubject(msg.getSubject());
			//data.setDate(msg.getReceivedDate());
			data.setMessageNumber(msg.getMessageNumber());
			data.setSender(EmailUtils.getFrom(msg.getFrom()));
			messages.add(data);
		}
		}
		catch(Exception exc){
			
		}
		return messages;
	}

	@Override
	public List<MessageData> getInboxMessages() {
		//ArrayList<MessageData> messages=new ArrayList<MessageData>();
		List<MessageData> mailmessages=null;
		try {
		
			if(!inbox.isOpen()){
				inbox.open(Folder.READ_ONLY);
			}
			
			mailmessages=getMessageObjects(inbox.getMessages());
		} catch (Exception exc) {

		}

		return mailmessages;
	}

	@Override
	public List<MessageData> getInboxMessages(PagingInfo paging) {
		// TODO Auto-generated method stub
		Message[] mailmessages=null;
		Message[] invertedList=null;
		try{
    		
    		if (!inbox.isOpen()) {
				inbox.open(Folder.READ_ONLY); //the point of throwing the exception
	        }
			
    		mailmessages=inbox.getMessages(paging.getStartIndex(), paging.getEndIndex());
    		invertedList=new Message[mailmessages.length];
    		int count=0;
    		for(int i=mailmessages.length-1;i>=0;i--){
    			invertedList[count]=mailmessages[i];
    			count++;
    		}
    		//inbox.close(true);
			//store.close();
    	}
    	catch(Exception exc){
    		String msg = exc.getMessage();
    		//store.close();
    	}
    	//return getMessageObjects(mailmessages);
		return getMessageObjects(invertedList);
	}

	@Override
	public boolean sendMessage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getMessageCount() {
		try{
			return inbox.getMessageCount();
		}
		catch(Exception exc){
			return -1;
		}
	}

	@Override
	public MessageData getMessage(int messageNumber) {
		MessageData data=new MessageData();
		try{
			if(!inbox.isOpen()){
				inbox.open(Folder.READ_ONLY);
			}
			Message message = inbox.getMessage(messageNumber);
			data.setContent(message.getContent().toString());
			
		}
		catch(Exception exc){
			
		}
		return data;
	}



	@Override
	public Message getNativeMessage(int messageNumber) {
		Message data=null;
		try{
			if(!inbox.isOpen()){
				inbox.open(Folder.READ_ONLY);
			}
			data = inbox.getMessage(messageNumber);
			
		}
		catch(Exception exc){
			
		}
		return data;
	}



	@Override
	public boolean disconnect() {
		try{
			inbox.close(true);
			store.close();
			return true;
		}
		catch(Exception exc){
			return false;
		}
		
	}

	@Override
	public String getMailVendorName() {
		return "gmail";
	}



	@Override
	public void response(Message message, String textResponse) {
		try{
			
			String to = InternetAddress.toString(message
			         .getRecipients(Message.RecipientType.TO));
			Message replyMessage = new MimeMessage(session);
            replyMessage = (MimeMessage) message.reply(false);
            replyMessage.setFrom(new InternetAddress(to));
            replyMessage.setText(textResponse);
            replyMessage.setReplyTo(message.getReplyTo());

            // Send the message by authenticating the SMTP server
            // Create a Transport instance and call the sendMessage
            Transport t = session.getTransport("smtp");
            try {
 	     //connect to the smpt server using transport instance
	     //change the user and password accordingly	
	           
            	
            	t.connect("imap.gmail.com",user, pass);
	           t.sendMessage(replyMessage,
	                  replyMessage.getAllRecipients());
	                 
            	//session.getTransport("smtp").sendMessage(replyMessage, replyMessage.getAllRecipients());
            } finally {
               //t.close();
            }
          
		}
		catch(Exception exc){
			String str = exc.getMessage();
		}
		
	}

}
