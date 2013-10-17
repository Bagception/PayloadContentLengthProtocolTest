package de.uniulm.bagception.bluetooth.protocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main implements PayloadContentLengthProtocolCallback{
	
	PayloadContentLengthProtocol pp;
	
	public static void main(String[] args) {
		
	}

	
	public Main() {
		Main m = new Main();
		pp = new PayloadContentLengthProtocol(this);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		InputStreamReader ir;
	
	}
	
	public void recv(String s){
		pp.in(s);
	}
	
	@Override
	public void onMessageRecv(String msg) {
		System.out.println("MESSAGE "+msg);
	}
	
	
}
