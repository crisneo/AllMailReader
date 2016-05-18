package com.allMailReader.views;

import javax.mail.Message;

import com.allMailReader.core.IMailProvider;

import android.os.AsyncTask;

public class ReplyMailAsync  extends AsyncTask<String, Void, String>  {

	ViewMailActivity parent;
	IMailProvider provider;
	Message message;
	String replyText;
	
	public ReplyMailAsync(ViewMailActivity parent, IMailProvider provider, Message message, String replyText){
		this.parent=parent;
		this.provider = provider;
		this.message=message;
		this.replyText=replyText;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		try{
			//parent.showSendingDialog("enviando", true);
			provider.response(message, replyText);
		}
		catch(Exception exc){
			String g = exc.getMessage();
		}
		return "";
	}
	
	@Override
	protected void onPostExecute(String result) {
		parent.showSendingDialog("", false);	
	}

}
