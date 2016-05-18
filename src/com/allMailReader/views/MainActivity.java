package com.allMailReader.views;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.allMailReader.controllers.MainController;
import com.allMailReader.core.IMailProvider;
import com.allMailReader.core.MessageData;
import com.allMailReader.core.PagingInfo;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity {

	ReadMailAsync readmailTask;
	private MailAccountSettings currentSettings;
	private IMailProvider currentMailProvider;
	private ListView listView;
	private View footerView;
	private List<MessageData> messages;

	int datasourceSize=200;
	private boolean loading=false;
	public PagingInfo currentPaging;
	public List<PagingInfo> pages;
	public int currentPageIndex=0;
	int PAGE_SIZE=20;
	public TextView messageText;
	public MainController controller;
	boolean isFirstLoading=true;
	ListViewAdapter adapter;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		controller = ((MainController)getApplicationContext());
		pages=new ArrayList<PagingInfo>();
		
		//currentMailProvider=MailProviderFactory.getInstance().getMailProvider(2);
		currentMailProvider = ((MainController)getApplicationContext()).getMailProvider();
		messageText=(TextView)findViewById(R.id.txtMessage);
		//datasourceSize=currentMailProvider.getMessageCount();
		currentSettings=new MailAccountSettings();
		//currentSettings.setAccountName("outlook");
		//currentSettings.setAccount("luciovero933@gmail.com");
		//currentSettings.setPassword("gladiador1");
		//currentSettings.setAccount("isidrov12@outlook.es");
		//currentSettings.setPassword("pichu321_");
		//currentSettings.setAccount("diegobleichner@outlook.es");
		//currentSettings.setPassword("crisneo24.");
		currentSettings=((MainController)getApplicationContext()).getAccountSettings();
		
		footerView=getLayoutInflater().inflate(R.layout.listview_footer, null); 
		footerView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, ListView.LayoutParams.WRAP_CONTENT));
		//listView = (ListView)findViewById(R.id.listView);
	
		getListView().addFooterView(footerView, null, false); 
		listView=getListView();
		messages=new ArrayList<MessageData>();
		adapter=new ListViewAdapter(this,messages,currentMailProvider.getMailVendorName());
		listView.setAdapter(adapter);
		readmailTask=new ReadMailAsync(this);
		listView.setOnScrollListener(new OnScrollListener(){
			@Override
	        public void onScrollStateChanged(AbsListView arg0, int arg1)
	        {
	            // nothing here             
	        }
	         
	        @Override
	        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	        {
	          
	        	if(requireMoreItems(firstVisibleItem, visibleItemCount, totalItemCount)){
	        		loading=true;
	        		messageText.setText(getResources().getString(R.string.textLoading));
	        		listView.addFooterView(footerView, null, false);
	        		//readmailTask.execute("");
	        		new ReadMailAsync(readmailTask.parent).execute("");
	        	}
	        }
		});
		
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		        final MessageData item = (MessageData) parent.getItemAtPosition(position);
		        try{
		        	
		        	controller.showMail(null, MainActivity.this , item.getMessageNumber());
		        }
		        catch(Exception exc){
		        	String msg = exc.getMessage();
		        }
		       

		      }

		    });
		((ImageButton)findViewById(R.id.btnRefresh)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				refresh();
				
			}
			
		});
		messageText.setText("loading mails...");
		//readmailTask.execute("");
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.login_background));
		}
		
		
		
	}
	
	public ListView getListView(){
		if(listView==null){
			listView = (ListView)findViewById(R.id.list);
		}
		return listView;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void refresh(){
		try{
			datasourceSize=200;//the initial size for loading
			//getListView().setAdapter(null);
			//getListView().no
			pages.clear();
			currentPageIndex=0;
			getListView().addFooterView(footerView, null, false);
			loading=true;
			messageText.setText(getResources().getString(R.string.textLoading));
			listView.addFooterView(footerView, null, false);
			//readmailTask.execute("");
			messages=new ArrayList<MessageData>();
			adapter=new ListViewAdapter(this,messages,currentMailProvider.getMailVendorName());
			getListView().setAdapter(adapter);
			new ReadMailAsync(this).execute("");
		}
		catch(Exception exc){
			String mesg = exc.getMessage();
		}
		
	}
	
	public void nextPagingInfo(){
		
		currentPageIndex++;
		if(pages.size()>=currentPageIndex){
			currentPaging=pages.get(currentPageIndex);	
		}
		
	}
	
	public void calculateRanges(int totalItems, int pageSize){
		
		
		if(pages.size()==0){
			if(totalItems<=PAGE_SIZE){
				PAGE_SIZE=totalItems;
				PagingInfo info=new PagingInfo();
				info.setStartIndex(1);
				info.setEndIndex(totalItems);
				currentPaging=info;
				return;
				
			}
			for(int i=totalItems; i>=1; i-=pageSize){
				PagingInfo info=new PagingInfo();
				
				int end=i-pageSize<1+1?1:i-pageSize+1;
				info.setStartIndex(end);
				info.setEndIndex(i);
				pages.add(info);
			}
			currentPaging=pages.get(currentPageIndex);
		}
		
	}
	
	public void updateResults(){
			showMessage("el numero de emails es:"+readmailTask.messagesCount);
	}
	
	public IMailProvider getCurrentMailProvider(){
		return currentMailProvider;
	}
	
	public void showMessage(String text){
	    AlertDialog ad = new AlertDialog.Builder(this).create();  
	    ad.setCancelable(false); // This blocks the 'BACK' button  
	    ad.setMessage(text);  
	    ad.setButton("OK", new DialogInterface.OnClickListener() {  
	        @Override  
	        public void onClick(DialogInterface dialog, int which) {  
	            dialog.dismiss();                      
	        }  
	    });  
	    ad.show();  
	}
	
	public MailAccountSettings getCurrentMailSettings(){
		return currentSettings;
	}
	
	public void appendMailMessages(List<MessageData> messages){
		try{

			
			HeaderViewListAdapter wasThisReallySoHard=(HeaderViewListAdapter)listView.getAdapter();
			ListViewAdapter nadapter=(ListViewAdapter)wasThisReallySoHard.getWrappedAdapter();
			nadapter.addAll(messages);
			nadapter.notifyDataSetChanged();
			//((ListViewAdapter)((HeaderViewListAdapter)listView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
			listView.removeFooterView(footerView);
			loading=false;
			messageText.setText(getResources().getString(R.string.textShowingMails)+getListView().getCount());
			nextPagingInfo();
		}
		catch(Exception exc){
			showMessage(exc.getMessage());
		}
	}
	
	
	public boolean requireMoreItems(int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		boolean lastItem = firstVisibleItem + visibleItemCount == totalItemCount && listView.getChildAt(visibleItemCount -1) != null && listView.getChildAt(visibleItemCount-1).getBottom() <= listView.getHeight();
		boolean moreRows = listView.getAdapter().getCount() < datasourceSize;
		return moreRows && lastItem && !loading;
		
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.btnSettings:
	      /*Toast.makeText(this, "Menu Item 1 selected", Toast.LENGTH_SHORT)
	          .show();*/
	      break;
	    case R.id.btnLogout:
	     /* Toast.makeText(this, "Menu item 2 selected", Toast.LENGTH_SHORT)
	          .show();*/
	    	controller.logout(MainActivity.this);
	      break;

	    default:
	      break;
	    }

	    return true;
	  } 

}
