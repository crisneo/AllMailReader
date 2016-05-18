package com.allMailReader.views;





import java.util.HashMap;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allMailReader.controllers.MainController;
import com.allMailReader.core.IMailProvider;
import com.allMailReader.core.MailProviderFactory;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class LoginView extends ActionBarActivity implements AnimationListener {
	
	EditText txtMailAccount;
	EditText txtPassword;
	MainController controller;
	// Animation
	Animation animFadein;
	Button btnLogin;
	TextView txtMessage;
	String acct;
	String pass;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login_view);
		controller = (MainController)getApplicationContext();
		txtMailAccount = (EditText)findViewById(R.id.txtMailAccount);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
		txtMessage = (TextView)findViewById(R.id.txtMessage);
		animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_in);
		
		// set animation listener
		animFadein.setAnimationListener(this);
		ImageView logo = (ImageView)findViewById(R.id.logo);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		
		logo.startAnimation(animFadein);
		btnLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				acct=txtMailAccount.getText().toString();
				pass = txtPassword.getText().toString();
				if(isValidData(acct, pass)){
					controller.setMailProvider(getMailProviderByAccount(parseMail(txtMailAccount.getText().toString())));
					HashMap credentials=new HashMap();
					credentials.put("mailAccount", acct);
					credentials.put("password", pass);
					txtMessage.setText("Authenticating...");
					btnLogin.setEnabled(false);
					new TestConnectionAsync(LoginView.this, credentials, controller.getMailProvider()).execute("");
				}
				else{
					Toast.makeText(LoginView.this, getResources().getString(R.string.invalidAcountMsg), Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
		
		txtMailAccount.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				txtMailAccount.setText("");
				
			}
			
		});
		
		((TextView)findViewById(R.id.textAbout)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				controller.showAbout(LoginView.this);
			}
			
		});
	}
	
	public boolean isValidData(String account, String password){
		if(!account.isEmpty() && !password.isEmpty() && isValidEmailAddress(account)){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	public static boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.view_mail, menu);
		return true;
	}
	
	public String parseMail(String mailAddress){
		
		return mailAddress.substring( mailAddress.indexOf("@"), mailAddress.lastIndexOf("."));
	}
	
	public IMailProvider getMailProviderByAccount(String mailVendor){
		mailVendor=mailVendor.startsWith("@")?mailVendor.replace("@", ""):mailVendor;
		IMailProvider provider=null;
		if(mailVendor.equals("gmail") || mailVendor.equals("GMAIL")){
			provider = MailProviderFactory.getInstance().getMailProvider(1);
		}
		else if (mailVendor.equals("hotmail") || mailVendor.equals("HOTMAIL") ||
				mailVendor.equals("outlook") || mailVendor.equals("OUTLOOK")){
			provider = MailProviderFactory.getInstance().getMailProvider(2);
		}
		else{
			//todo: another provider
		}
		return provider;
	}

	@Override
	public void onAnimationEnd(Animation arg0) {

		btnLogin.setEnabled(true);
		
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void authenticate(boolean isAuthenticated){
		btnLogin.setEnabled(true);
		if(isAuthenticated){
			MailAccountSettings accountSettings=new MailAccountSettings();
			accountSettings.setAccountName(parseMail(acct));
			accountSettings.setAccount(acct);
			accountSettings.setPassword(pass);
			
			controller.setAccountSettings(accountSettings);
			controller.showMainWindow(LoginView.this);
			//controller.showMainWindow(this);
		}else{
			
			txtMessage.setText(getResources().getString(R.string.invalidAcountMsg));
		}
	}
	
	
}
