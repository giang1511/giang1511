package demo;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class ATMClient extends JFrame {

	private JPanel contentPane;
	private JTextField tfID;
	private JTextField tfSoTien;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ATMClient frame = new ATMClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ATMClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblID = new JLabel("ID");
		lblID.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblID.setBounds(60, 49, 87, 20);
		contentPane.add(lblID);
		
		tfID = new JTextField();
		tfID.setBounds(217, 46, 173, 32);
		contentPane.add(tfID);
		tfID.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nhap so tien");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(60, 97, 159, 32);
		contentPane.add(lblNewLabel_1);
		
		tfSoTien = new JTextField();
		tfSoTien.setBounds(217, 97, 173, 32);
		contentPane.add(tfSoTien);
		tfSoTien.setColumns(10);
		
		JButton btnNap = new JButton("Nap tien");
		btnNap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					 Socket clientSK = new Socket(InetAddress.getByName("localhost"), 8000);
					 String id = tfID.getText();
					 String money = tfSoTien.getText();
					 
					 DataOutputStream outClient = new DataOutputStream(clientSK.getOutputStream());
					 outClient.writeBytes(id+"\n");
					 outClient.writeBytes(money+"\n");
					 outClient.flush();
					 
					 
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace(); 
				}
			}
		});
		btnNap.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNap.setBounds(60, 165, 115, 32);
		contentPane.add(btnNap);
	}
}
