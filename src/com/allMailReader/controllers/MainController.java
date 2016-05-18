package com.allMailReader.controllers;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.allMailReader.core.IMailProvider;
import com.allMailReader.core.MessageData;
import com.allMailReader.views.About;
import com.allMailReader.views.LoginView;
import com.allMailReader.views.MailAccountSettings;
import com.allMailReader.views.MainActivity;
import com.allMailReader.views.ViewMailActivity;

/*
 * Controller class to manage intent transitions and datasource providers for all
 * activities 
 * 
 * clase controladora para manejar el flujo de los intents entre paginas
 * y tambien el acceso a datos comun(IMailprovider) para todas las actividades
 */
public class MainController extends Application {
	
	private IMailProvider mailProvider;
	public MessageData currentMail;
	MailAccountSettings accountSettings;
	
	public MailAccountSettings getAccountSettings() {
		return accountSettings;
	}

	public void setAccountSettings(MailAccountSettings accountSettings) {
		this.accountSettings = accountSettings;
	}
	
	/*public MainController(IMailProvider provider){
		this.mailProvider = provider;
	}*/
	public void showMail(MessageData message, Activity activity, int mailNumber){
		Intent intent=new Intent(activity,ViewMailActivity.class);
		intent.putExtra("mailNumber", mailNumber);
		//intent.putExtra("calling-activity", ActivityConstants.ACTIVITY_1);
		activity.startActivity(intent);
	}
	
	public void showMainWindow(Activity activity){
		Intent intentm=new Intent(activity,MainActivity.class);
		activity.startActivity(intentm);
	}
	
	public void showAbout(Activity activity){
		Intent intent=new Intent(activity,About.class);
		activity.startActivity(intent);
	}

	public IMailProvider getMailProvider() {
		return mailProvider;
	}

	public void setMailProvider(IMailProvider mailProvider) {
		this.mailProvider = mailProvider;
	}
	
	public void logout(Activity activity){
		this.mailProvider.disconnect();
		Intent intentl=new Intent(activity,LoginView.class);
		activity.startActivity(intentl);
	}

}
