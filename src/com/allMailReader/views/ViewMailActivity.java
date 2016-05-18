package com.allMailReader.views;

import java.util.Date;

import javax.mail.Message;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allMailReader.controllers.MainController;
import com.allMailReader.core.IMailProvider;




@SuppressLint("NewApi")
public class ViewMailActivity extends ActionBarActivity  {

	public IMailProvider provider;
	//MessageData message;
	public Message message;
	int messageNumber;
	ProgressDialog progressDialog;
	ProgressDialog sendingDialog;
	 
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_mail);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.login_background));
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		
		((ImageButton)findViewById(R.id.btnResponse)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				//ViewMailActivity.this.finish();
				replyMail();
				
			}
			
		});

		MainController controller=(MainController)getApplicationContext();
		provider = controller.getMailProvider();
		messageNumber  = Integer.parseInt(getIntent().getExtras().get("mailNumber").toString());

		ViewMailInfoAsync task = new ViewMailInfoAsync(this);
		showProgressDialog(true);
		task.execute("");
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_mail, menu);
		return true;
	}
	
	public void showMessageInfo(String content, String subject, String from, Date date){
		//String htmlMail = EmailUtils.getMessageContent(content);
		String mime = "text/html";
		String encoding = "utf-8";
		((TextView)findViewById(R.id.textFrom)).setText(getResources().getString(R.string.textFrom)+from);
		((TextView)findViewById(R.id.textSubject)).setText(getResources().getString(R.string.textSubject)+subject);
		((TextView)findViewById(R.id.textMailDate)).setText(getResources().getString(R.string.textDate)+date);
		((WebView)findViewById(R.id.webview)).loadDataWithBaseURL(null, content, mime, encoding, null);
		showProgressDialog(false);
	}
	

	
	public void showProgressDialog(boolean show){
		if(show == true){
			progressDialog = ProgressDialog.show(this, getResources().getString(R.string.textLoadingDialogTitle)
					, getResources().getString(R.string.textLoading));
		}
		else{
			if(progressDialog != null){
				progressDialog.dismiss();
				progressDialog.hide();
			}
			
			
		}
		
	}
	
	public void showSendingDialog(String dialogText, boolean showt){
		if(showt == true){
			sendingDialog = ProgressDialog.show(this, "enviando..."
					, getResources().getString(R.string.textLoading));
		}
		else{
			if(sendingDialog != null){
				sendingDialog.dismiss();
				sendingDialog.hide();
			}
			
			
		}
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:{
	        Intent homeIntent = new Intent(this, MainActivity.class);
	        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(homeIntent);
	      }
	    break;
	    case R.id.btnLogout2:
	     /* Toast.makeText(this, "Menu item 2 selected", Toast.LENGTH_SHORT)
	          .show();*/
	    	((MainController)getApplicationContext()).logout(ViewMailActivity.this);
	      break;

	    default:
	      break;
	    }

	    return true;
	  } 
	
	public void setCurrentMessage(Message msg){
		message = msg;
	}
	
	
	
	public void replyMail(){
		// Set an EditText view to get user input 
		final EditText input = new EditText(ViewMailActivity.this);
		input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		input.setLayoutParams(new android.view.ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, 
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		//input.setHeight(pixels)
		
		//final String nText = nuevoTexto;

		new AlertDialog.Builder(ViewMailActivity.this)
		    .setTitle(getResources().getString(R.string.texteplyMail))
		    .setMessage("")
		    .setView(input)
		    
		    .setPositiveButton(getResources().getString(R.string.btnReplyText), new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int whichButton) {
		             String textResponse =  input.getText().toString(); 
		            
		             //writeStringAsFile(nText, filename+".txt", true);
		            
		             //Toast.makeText(getApplicationContext(), "archivo guardado", Toast.LENGTH_SHORT);
		             //provider.response(message, textResponse);
		             new ReplyMailAsync(ViewMailActivity.this, provider, message, textResponse).execute("");
		             showSendingDialog(getResources().getString(R.string.textSendingMail), true);
		         }
		    })
		    .setNegativeButton(getResources().getString(R.string.btncancelText), new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int whichButton) {
		               
		         }
		    }).show();
		
	}
	

}
