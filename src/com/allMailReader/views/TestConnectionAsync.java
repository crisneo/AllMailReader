package com.allMailReader.views;

import java.util.Map;

import android.os.AsyncTask;

import com.allMailReader.controllers.MainController;
import com.allMailReader.core.IMailProvider;

public class TestConnectionAsync extends AsyncTask<String, Void, String>  {

	protected IMailProvider provider;
	protected Map credentials;
	protected LoginView parent;
	private boolean successConnection;
	
	public TestConnectionAsync(LoginView parent, Map credentials, IMailProvider provider){
		//this.provider=provider;
		this.provider=provider;
		this.credentials=credentials;
		this.parent=parent;
	}
	@Override
	protected String doInBackground(String... arg0) {
		try{
			if(provider!=null){
				successConnection = provider.connect(credentials);
			}
		}
		catch(Exception exc){
			successConnection=false;
		}
		
		return "";
	}
	@Override
	protected void onPostExecute(String result) {
		parent.authenticate(successConnection);
	}

}
