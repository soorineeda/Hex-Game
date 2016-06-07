package com;

import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientButtons extends Frame implements ActionListener{
	
	ClientButtons(){
		final Button client1 = new Button("Client1");
		final Button client2 = new Button("Client2");
		final Button client3 = new Button("");

		client1.setBounds(180,80,100,40);
		client2.setBounds(180,180,100,40);
		client1.setEnabled(true);
		client2.setEnabled(false);
		this.add(client1);
		this.add(client2);
		this.add(client3);
		client1.addActionListener(this);
		client2.addActionListener(this);
		client3.addActionListener(this);
		
		client1.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
				ClientConsole clientConsole = new ClientConsole(1);
				clientConsole.smtpConnect();
				client1.setEnabled(false);
				client2.setEnabled(true);
	        }
		});
		
		client2.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
				ClientConsole clientConsole = new ClientConsole(2);
				clientConsole.smtpConnect();
				client2.setEnabled(false);
				ClientButtons.this.setVisible(false);
	        }
		});
		
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});

	}
	
	public void windows(){
		ClientButtons s = new ClientButtons();
		s.setSize(450,400);
		s.setTitle("Administrator console");
		s.setVisible(true);		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
