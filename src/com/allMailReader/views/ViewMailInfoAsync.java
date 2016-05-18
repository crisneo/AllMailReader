package com.allMailReader.views;

import java.util.Date;

import javax.mail.Message;

import com.allMailReader.core.EmailUtils;
import com.allMailReader.core.IMailProvider;
import com.allMailReader.core.MessageData;

import android.os.AsyncTask;

public class ViewMailInfoAsync extends AsyncTask<String, Void, String>  {

	public ViewMailActivity parent;
	String message = "";
	String subject;
	String from;
	Date date;
	Message messageObj;
	
	public ViewMailInfoAsync(ViewMailActivity parent){
		this.parent=parent;
	}
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try{
			//IMailProvider  mailprovider= parent.provider;
			messageObj = parent.provider.getNativeMessage(parent.messageNumber);
			message = EmailUtils.getMessageContent(messageObj);
			subject = messageObj.getSubject();
			from = EmailUtils.getFrom(messageObj.getFrom());
			date = messageObj.getSentDate();
		}
		catch(Exception exc){
			String mf = exc.getMessage();
		}
		
		return "";
	}
	
	@Override
	protected void onPostExecute(String result) {
		parent.showMessageInfo(message, subject, from, date);
		parent.setCurrentMessage(messageObj);		
	}

}
