

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.uniulm.bagception.bluetooth.protocol.PayloadContentLengthProtocol;
import de.uniulm.bagception.bluetooth.protocol.PayloadContentLengthProtocolCallback;

public class Main implements PayloadContentLengthProtocolCallback{
	
	PayloadContentLengthProtocol pp;
	
	public static void main(String[] args) {
		new Main();
	}

	
	public Main() {
		
		pp = new PayloadContentLengthProtocol(this);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			try {
				String line = br.readLine();
				recv(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	public void recv(String s){
		pp.in(s);
	}
	
	@Override
	public void onMessageRecv(String msg) {
		System.out.println("MESSAGE "+msg);
	}
	
	
}
