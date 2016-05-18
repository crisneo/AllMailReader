package com.allMailReader.views;

import java.util.List;

import javax.mail.Message;

import com.allMailReader.core.MessageData;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class ListViewAdapter extends ArrayAdapter<MessageData> {

	Context context=null;
	List<MessageData> messages;
	String mailVendorName;
	
	public ListViewAdapter(Context context, List<MessageData> messages, String mailVendorName) {
		super(context, R.layout.listview_item, messages);
		this.context=context;
		this.messages=messages;
		this.mailVendorName=mailVendorName;
	}
	
		@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    
			LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.listview_item, parent, false);
	    TextView textSubject = (TextView) rowView.findViewById(R.id.itemMailSubject);
	    TextView textSender = (TextView) rowView.findViewById(R.id.itemMailSender);
	    ImageView image = (ImageView)rowView.findViewById(R.id.list_image);
	  
	    image.setImageResource(getImageResource(this.mailVendorName));
	   
	    MessageData message = messages.get(position);
	    try{
	    	textSubject.setText(message.getSubject());
	    	textSender.setText(message.getSender());
	    }
	    catch(Exception exc){
	    	
	    }
	    

	    return rowView;
	  }
		
		private int getImageResource(String vendorName){
			if(vendorName=="gmail"){
				return R.drawable.gmail;
			}
			else{
				return R.drawable.hotmail;
			}
		}
	
	

}

