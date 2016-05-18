package com.allMailReader.views;

import java.util.HashMap;
import java.util.List;

import javax.mail.Message;

import com.allMailReader.core.IMailProvider;
import com.allMailReader.core.MailProviderFactory;
import com.allMailReader.core.MessageData;

import android.os.AsyncTask;

public class ReadMailAsync extends AsyncTask<String, Void, String> {

	private List<MessageData> mailmessages;
	MainActivity parent;
	public int messagesCount;
	public IMailProvider provider;
	
	public ReadMailAsync(MainActivity parent){
		this.parent=parent;
	}
	public List<MessageData> getMails(){
		return mailmessages;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		//return null;
		//IMailProvider provider=MailProviderFactory.getInstance().getMailProvider(1);
		provider=parent.getCurrentMailProvider();
		HashMap credentials=new HashMap();
		credentials.put("mailAccount", parent.getCurrentMailSettings().getAccount());
		credentials.put("password", parent.getCurrentMailSettings().getPassword());
		/*if(parent.messageText!=null){
			parent.messageText.setText("loading");
		}*/
		
		if(provider.connect(credentials)){
			messagesCount = provider.getMessageCount();
			//mailmessages = provider.getInboxMessages();
			this.parent.datasourceSize=messagesCount;
			parent.calculateRanges(messagesCount, parent.PAGE_SIZE);
			mailmessages=provider.getInboxMessages(this.parent.currentPaging);
			
			
		}
		return "";
		
		
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
		//parent.updateResults();
		//parent.ShowMailMessages(mailmessages);
		parent.appendMailMessages(mailmessages);
	}

}
