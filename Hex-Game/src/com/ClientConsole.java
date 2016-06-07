package com;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.StatusData;

public class ClientConsole extends JFrame implements MouseListener{
	public JLabel label;
	boolean indicator = false;
	ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
	int playerID ;
	Graphics graphics;

	boolean isGameEnd = false;
	public ClientConsole(int i) {
		playerID = i;
		setSize(470, 600);
		setTitle("Client"+i);
		label = new JLabel("....", JLabel.CENTER);
		add(label);
		label.addMouseListener(this);		
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public void paint(Graphics g){
		int x1 = 50, y1 = 150;
		for(int k = 0;k<6;k++){
			int n = 6;
			int x[] = {x1,x1+25,x1+25,x1,x1-25,x1-25};
			int y[] = {y1,y1+25,y1+50,y1+75,y1+50,y1+25};
			g.setColor(Color.blue);
			g.drawPolygon(x,y,n);
			for(int j = 0;j< 5;j++){
				x[5] = x[1];
				x[4] = x[2];
				y[5] = y[1];
				y[4] = y[4];
				for(int i = 0;i<4;i++){
					x[i] = x[i]+50;
				}
					g.setColor(Color.blue);
					g.drawPolygon(x,y,n);
			}
			x1 += 25;
			y1 += 50;
		}
	}
	public void clearBoard(Graphics g){
		int x1 = 50, y1 = 150;
		for(int k = 0;k<6;k++){
			int n = 6;
			int x[] = {x1,x1+25,x1+25,x1,x1-25,x1-25};
			int y[] = {y1,y1+25,y1+50,y1+75,y1+50,y1+25};
			g.setColor(Color.white);
			g.fillPolygon(x, y, n);
			g.setColor(Color.blue);
			g.drawPolygon(x,y,n);
			for(int j = 0;j< 5;j++){
				x[5] = x[1];
				x[4] = x[2];
				y[5] = y[1];
				y[4] = y[4];
				for(int i = 0;i<4;i++){
					x[i] = x[i]+50;
				}
				g.setColor(Color.white);
				g.fillPolygon(x, y, n);
					g.setColor(Color.blue);
					g.drawPolygon(x,y,n);
			}
			x1 += 25;
			y1 += 50;
		}
		g.setColor(Color.white);
		g.fillRect(40, 500, 400, 30);
		g.setColor(Color.blue);
		g.drawString("New Game Started-- It's Player1's Turn...", 40, 520);
	}
	@Override
	public void mouseClicked(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();
		int blockNumber = 0;
		int x1 = 16,x2 = 66,y1 = 137, y2 = 162;
		int xrootstart = 16, xrootend = 66,yrootstart = 137,yrootend = 162;
		int count = 0;
		System.out.println("[x,y]--["+x+","+y+"]");
		for(int j = 0;j<6;j++){
			for(int i = 0;i<6;i++){
				count++;
				if(x > xrootstart && x < xrootend && y >yrootstart && y <yrootend){
					System.out.println(xrootstart+","+xrootend+","+yrootstart+","+yrootend);
					blockNumber = count;
					System.out.println("block:["+i+","+j+"]"+"blockNumber"+blockNumber);
					
		        	try {
		        		System.out.println(isGameEnd);
		        		if(isGameEnd){
		        			graphics.setColor(Color.white);
		        			graphics.fillRect(40, 500, 550, 30);
		        			graphics.setColor(Color.blue);
		        			graphics.drawString("This Game ended..Please restart the Game", 40, 520);
		        		}else{
			        		System.out.println("action performed on a button..");
			        		StatusData clientInfo = new StatusData();
			        		clientInfo.setClientId(playerID);
			        		clientInfo.setCellNumber(blockNumber);
			        		objectOutputStream.writeObject(clientInfo);
		        		}
					} catch (IOException e1) {
						System.out.println("failed to write to server..");
						e1.printStackTrace();
					}
				}
				xrootstart += 50;
				xrootend += 50;
			}
			xrootstart = x1 += 25;
			xrootend = x2 += 25;
			yrootstart = y1 += 50;
			yrootend = y2 += 50;
		}
		if(blockNumber == 0){
			System.out.println("Not a valid Selection..Please click on the center of the Hexagon..");
		}else{
		}
	}
	public void smtpConnect(){
		System.out.println("setConnection.. executes..");
		int port = 1600;
        Socket client;
		try {
			client = new Socket("localhost", port);
	        System.out.println("Just connected to "+ client.getRemoteSocketAddress());
	        OutputStream outputStream = client.getOutputStream();
	        objectOutputStream = new ObjectOutputStream(outputStream);
	        InputStream inputStream = client.getInputStream();
	        objectInputStream = new ObjectInputStream(inputStream);
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException ");
			e.printStackTrace();
		}
		ClientSMTPReceiver clientSMTPReceiver = new ClientSMTPReceiver(this.getGraphics());
		clientSMTPReceiver.start();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	class ClientSMTPReceiver extends Thread{
		public ClientSMTPReceiver(Graphics g){
			graphics = g;
		}
		void updateBoard(int cellNumber, int clientID,String message){
			System.out.println("call to paint..");
			int x1 = 50, y1 = 150;
			int x[] = {x1,x1+25,x1+25,x1,x1-25,x1-25};
			int y[] = {y1,y1+25,y1+50,y1+75,y1+50,y1+25};
			int n = 6;
			int count = 0;
			do{		
				x[0] = x1; 		y[0] = y1;
				x[1] = x1+25; 	y[1] = y1+25;
				x[2] = x1+25;	y[2] = y1+50;
				x[3] = x1;		y[3] = y1+75;
				x[4] = x1-25;	y[4] = y1+50;
				x[5] = x1-25;	y[5] = y1+25;
				count++;
				for(int j = 0;count < cellNumber && j< 5;j++){
					count++;
					x[5] = x[1];
					x[4] = x[2];
					y[5] = y[1];
					y[4] = y[4];
					for(int i = 0;i<4;i++){
						x[i] = x[i]+50;
					}
				}
				x1 += 25;
				y1 += 50;
			}while(count < cellNumber);
			if(clientID == 1){
				Color c1 = new Color(243,103,68);
				graphics.setColor(c1);
				graphics.fillPolygon(x, y, n);
				graphics.setColor(Color.black);
				graphics.drawPolygon(x, y, n);
				graphics.setColor(Color.white);
				graphics.fillRect(40, 500, 550, 30);
				graphics.setColor(Color.blue);
				graphics.drawString(message, 40, 520);
			}else{
				Color c2 = new Color(100,210,172);
				graphics.setColor(c2);
				graphics.fillPolygon(x, y, n);
				graphics.setColor(Color.black);
				graphics.drawPolygon(x, y, n);
				graphics.setColor(Color.white);
				graphics.fillRect(40, 500, 550, 30);
				graphics.setColor(Color.blue);
				graphics.drawString(message, 40, 520);
			}
		}
		void cleanAllBlocks(){
			int x1 = 50, y1 = 50;
			for(int k = 0;k<6;k++){		
				int n = 6;
				int x[] = {x1,x1+25,x1+25,x1,x1-25,x1-25};
				int y[] = {y1,y1+25,y1+50,y1+75,y1+50,y1+25};
				graphics.setColor(Color.white);
				graphics.fillPolygon(x,y,n);
				for(int j = 0;j< 5;j++){
					x[5] = x[1];
					x[4] = x[2];
					y[5] = y[1];
					y[4] = y[4];
					for(int i = 0;i<4;i++){
						x[i] = x[i]+50;
					}
						graphics.drawPolygon(x,y,n);
				}
				x1 += 25;
				y1 += 50;
			}
		}

		public void run(){
			System.out.println("run started for Receiver thread..");
			while(true){
				try {
					StatusData statusData =  (StatusData) objectInputStream.readObject();
					System.out.println(" @Client"+playerID);
					System.out.println("PlayerThread id: "+statusData.getClientId());
					System.out.println("cell Number: "+statusData.getCellNumber());
					System.out.println("isRestart:"+statusData.isRestart());
					if(statusData.isRestart()){
						isGameEnd = false;
						System.out.println("set isGameEnd to false @player"+playerID);
						clearBoard(graphics);
					}else{
						if(statusData.isValidMove()){
								updateBoard(statusData.getCellNumber(),statusData.getClientId(),statusData.getMessage());
						}else{
							System.out.println("its not a valid move..");
							graphics.setColor(Color.white);
							graphics.fillRect(40, 500, 550, 30);
							graphics.setColor(Color.blue);
							graphics.drawString(statusData.getMessage(), 40, 520);
						}
						if(statusData.isGameEnd()){
							isGameEnd  = true;
							System.out.println("set isGameEnd to true @Player"+playerID);
							graphics.setColor(Color.white);
							graphics.fillRect(40, 500, 550, 30);
							graphics.setColor(Color.blue);
							graphics.drawString("Congrats!!!!Player"+statusData.getWinner()+" is the winner..Game Ends..Please Start NewGame..", 40, 520);
						}else{
							
						}

					}
				} catch (IOException e1) {
					System.out.println("Exception objectInputStream run in ThreadReceiver..");
					e1.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}