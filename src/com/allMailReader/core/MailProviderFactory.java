package com.allMailReader.core;

import com.allMailReader.gmail.GmailProvider;
import com.allMailReader.hotmail.HotmailProvider;

public class MailProviderFactory {
	private static MailProviderFactory instance=null;
	
	public static MailProviderFactory getInstance(){
		if(instance==null){
			instance=new MailProviderFactory();
		}
		return instance;
	}
	//1:gmail, 2:hotmail, 3:yahoo, 4:other
	public IMailProvider getMailProvider(int mailService){
		IMailProvider provider=null;
		switch(mailService){
		case 1:
			provider=new GmailProvider();
			break;
		case 2:
			provider=new HotmailProvider();
			break;
		default:
		
		break;
		}
		return provider;
	}
}
