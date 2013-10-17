package de.uniulm.bagception.bluetooth.protocol;

import java.io.InputStreamReader;


public class PayloadContentLengthProtocol {

	private final StringBuilder head;
	private final StringBuilder message;
	private int messageLength=-1;
	private boolean headerRecv = false;
	private final PayloadContentLengthProtocolCallback callback;
	
	
	
	public PayloadContentLengthProtocol(PayloadContentLengthProtocolCallback callback) {
		head = new StringBuilder();
		message = new StringBuilder();
		this.callback=callback;
		
	}
	
	public synchronized void in(String c){
		if (c.length()==0){
			Log.d("RECV","len = 0");
			return;
		}
		
		if (headerRecv){
			inMessage(c);
		}else{
			inHeader(c);
		}
	}
	
	private void inHeader(String h){
		Log.d("header","h is "+h);

		if (h.length()==0) return;
		
		Log.d("header","reading header bytes");
		//probing header 
		if (head.length()==0){
			try{
				
				System.out.println("try parsing header: "+Integer.parseInt(h.charAt(0)+""));
			}catch(NumberFormatException e){
				Log.d("ERROR", "Error probing header");
				reset();
				return;
			}
		}
		
		int headerSeparator = h.indexOf(':');
		
		if (headerSeparator == -1){
			Log.d("header","header terminator not in message");
			head.append(h);
		}else{
			Log.d("header","header terminator in message");
			headerRecv=true;
			String head = h.substring(0, headerSeparator);
			String msgContinue = h.substring(headerSeparator+1);
			Log.d("header","msgContinue "+msgContinue);
			this.head.append(head);
			
			Log.d("header","header: "+this.head.toString());
			try{
				this.messageLength=Integer.parseInt(this.head.toString());	
			}catch(NumberFormatException e){
				Log.d("ERROR", "Error parsing protocol");
				reset();
			}
			
			Log.d("RECV","messageLength "+messageLength);
			inMessage(msgContinue);
		}
	}
	
	private void inMessage(String m){
		Log.d("header","m is "+m);
		if (m.length()==0)return;
		headerRecv=false;
		Log.d("header","reading message bytes");
		//header complete recv
		int messageLength = message.length()+m.length(); //the length of the current  message + all currently recv bytes
		if (messageLength<this.messageLength){
			//all currently recv bytes are part of the message
			headerRecv=true;
			message.append(m);
		}else{
			//the recv bytes contain a new message or is empty
			int partMessageOnly =  this.messageLength-message.length(); //offset of the next message
			
			String tail = m.substring(partMessageOnly);
			String partMessageOnlyString = m.substring(0,partMessageOnly); //the string that contains the rest of this message
			message.append(partMessageOnlyString);
			callback.onMessageRecv(message.toString());
			head.setLength(0);
			message.setLength(0);
			
			Log.d("header","continue with "+tail);
			inHeader(tail);

		}
	}
	
	
	

	private void reset(){
		head.setLength(0);
		message.setLength(0);
		headerRecv=false;
	}
}
