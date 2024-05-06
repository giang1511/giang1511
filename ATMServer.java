package demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ATMServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket serverSK = new ServerSocket(8000);
			DataInputStream inputServer;
			DataOutputStream outServer;
			while(true) {
				 Socket skServer = serverSK.accept();
				 System.out.println("Connection sucessful");
				 
				 inputServer = new DataInputStream(skServer.getInputStream());
				 String id = inputServer.readLine();
				 System.out.println("id ="+id);
				 String money = inputServer.readLine();
				 System.out.println("money ="+money);
				 
				 float addmoney = Float.parseFloat(money);
				 QTMBusiness db = new QTMBusiness();
				 int n = db.excuteDB("Insert into Account values('"+id+"','"+addmoney+"')");
				 if(n>0) JOptionPane.showMessageDialog(null, "Success");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

}
