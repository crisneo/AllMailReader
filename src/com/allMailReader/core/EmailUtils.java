package com.allMailReader.core;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

public class EmailUtils {
	
	public static String getMessageContent(Message message){
		String result="";
		try{
			 if(message instanceof MimeMessage)
		        {
		            MimeMessage m = (MimeMessage)message;
		            Object contentObject = m.getContent();
		            if(contentObject instanceof Multipart)
		            {
		                BodyPart clearTextPart = null;
		                BodyPart htmlTextPart = null;
		                Multipart content = (Multipart)contentObject;
		                int count = content.getCount();
		                for(int i=0; i<count; i++)
		                {
		                    BodyPart part =  content.getBodyPart(i);
		                    if(part.isMimeType("text/plain"))
		                    {
		                        clearTextPart = part;
		                        break;
		                    }
		                    else if(part.isMimeType("text/html"))
		                    {
		                        htmlTextPart = part;
		                    }
		                }

		                if(clearTextPart!=null)
		                {
		                    result = (String) clearTextPart.getContent();
		                }
		                else if (htmlTextPart!=null)
		                {
		                    String html = (String) htmlTextPart.getContent();
		                    result=html;
		                    //result = Jsoup.parse(html).text();
		                }

		            }
		             else if (contentObject instanceof String) // a simple text message
		            {
		                result = (String) contentObject;
		            }
		            else // not a mime message
		            {
		                //logger.log(Level.WARNING,"not a mime part or multipart {0}",message.toString());
		                result = "";
		            }
		}
		}
		catch(Exception ex){
			String msgex = ex.getMessage();
			
		}
		return result;
	}
	
	public static String getFrom(Address[] addresses){
		StringBuffer buffer=new StringBuffer();
		for(Address addrr:addresses){
			buffer.append(addrr.toString()+",");
		}
		return buffer.toString();
	}

}
