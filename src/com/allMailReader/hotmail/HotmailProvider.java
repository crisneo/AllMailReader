package com.allMailReader.hotmail;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.allMailReader.core.EmailUtils;
import com.allMailReader.core.IMailProvider;
import com.allMailReader.core.MessageData;
import com.allMailReader.core.PagingInfo;

public class HotmailProvider implements IMailProvider {

	Store store;
	Session session;
	Folder inbox;
	String user;
	String pass;
	
	@Override
	public boolean connect(Map credentials) {
		// TODO Auto-generated method stub
		try{
			if(store==null || !store.isConnected() || inbox == null){
				Properties pop3Props = new Properties();
				pop3Props.setProperty("mail.pop3s.port", "995");
				
				session = Session.getInstance(pop3Props, null);
				
				store = session.getStore("pop3s");
				user = credentials.get("mailAccount").toString();
				pass = credentials.get("password").toString();
				store.connect("pop3.live.com", 995,user ,pass );
				inbox = store.getFolder("Inbox");
			
				return true;
			}
			return true;
			
		}
		catch(Exception exc){
			return false;
		}
	}

	@Override
	public List<MessageData> getInboxMessages() {
		
		Message[] mailmessages=null;
		try{
    		
    		if (!inbox.isOpen()) {
				inbox.open(Folder.READ_ONLY); //the point of throwing the exception
	        }
			
    		mailmessages=inbox.getMessages();
    		//inbox.close(true);
			//store.close();
    	}
    	catch(Exception exc){
    		String msg = exc.getMessage();
    		//store.close();
    	}
    	return getMessageObjects(mailmessages);
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
	
	/*public void fetch(){
		Message[] mailmessages=null;
		FetchProfile fp = new FetchProfile();
		fp.add(FetchProfile.Item.ENVELOPE);
		fp.add("Date");
		inbox.fetch(mailmessages, fp);
	}*/

	@Override
	public List<MessageData> getInboxMessages(PagingInfo paging) {
		// TODO Auto-generated method stub
		//return null;
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
			if (!inbox.isOpen()) {
				inbox.open(Folder.READ_ONLY); //the point of throwing the exception
	        }
			
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
			String excmsg = exc.getMessage();
		}
		return data;
	}

	@Override
	public Message getNativeMessage(int messageNumber) {
		Message message=null;
		try{
			if(!inbox.isOpen()){
				inbox.open(Folder.READ_ONLY);
			}
			message = inbox.getMessage(messageNumber);
			
			
		}
		catch(Exception exc){
			String excmsg = exc.getMessage();
		}
		return message;
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
		return "hotmail";
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
           
            try {
 	     //connect to the smpt server using transport instance
	     //change the user and password accordingly	
	           
            	
            	//t.connect("pop3.live.com",user, pass);
            	if(!session.getProperties().contains("mail.smtp.starttls.enable")){
            		session.getProperties().put("mail.smtp.starttls.enable", "true");
                	session.getProperties().put("mail.smtp.starttls.required", "true");
            	}
            	
            	
            	 
            	Transport t = session.getTransport("smtp");
            	t.connect("smtp.live.com", user, pass);
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
