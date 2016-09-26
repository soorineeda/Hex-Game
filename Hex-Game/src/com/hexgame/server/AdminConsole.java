package com;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import com.AdminConsole.SMTPServer.ServerSMTPThread;
import com.StatusData;
import com.ClientConsole;

public class AdminConsole extends Frame implements ActionListener
{
	static int gameStatus[] = new int[37];
	Button reset = new Button("Reset the game");
	boolean isGameEnd = false;
	boolean firstPlayer  = true;
	ArrayList<ServerSMTPThread> listOfCLients = new ArrayList<ServerSMTPThread>();
	
	AdminConsole()
	{
		final Button mainButton = new Button("Start Server");
		final Label statusMessage = new Label("Please start the Server..");

		mainButton.setBounds(150,100,100,40);
		statusMessage.setBounds(50,160,300,20);
		
		statusMessage.setVisible(false);

		this.add(mainButton);
		this.add(statusMessage);
		
		reset.setBounds(150,220,100,40);
		reset.setVisible(true);
		reset.setEnabled(false);
		this.add(reset);

		Button clientButton = new Button("");
		clientButton.setBounds(400,400,100,40);
		this.add(clientButton);
		
		
		mainButton.addActionListener(this);
		reset.addActionListener(this);
		
		
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});
		
		mainButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	SMTPServer smtpServer = new SMTPServer();
	        	smtpServer.start();
	        	statusMessage.setText("Server Started successfully.. Please start the clients..");
	        	statusMessage.setVisible(true);
	        	mainButton.setEnabled(false);
	        	
	        	ClientButtons cb = new ClientButtons();
	        	cb.windows();
	        }
		});	
		
		reset.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	resetBoard();
	        }

			private void resetBoard() {
				reset.setEnabled(false);
				System.out.println("startNewGame");
				firstPlayer = true;
		    	for(int i = 0;i<37;i++){
		    		gameStatus[i] = -1;
		    	}
		    	StatusData statusData = new StatusData();
		    	statusData.setRestart(true);
		    	isGameEnd = false;
		    	for(int i = listOfCLients.size(); --i >= 0;) {
		    		ServerSMTPThread nst = listOfCLients.get(i);
					System.out.println("client id:"+nst);
					nst.outMessage(statusData);
				}
		    	
		    	statusMessage.setText("New Game Started..");
		    	statusMessage.setVisible(true);
			}
		});
	}
	
	public static void main(String args[]){
		AdminConsole s = new AdminConsole();
		s.setSize(450,400);
		s.setTitle("Administrator console");
		s.setVisible(true);
		
		for(int i = 0;i<37;i++){
			gameStatus[i] = -1;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
	}
	
	class SMTPServer extends Thread{
		public void run(){
			int port = 1600;
			ServerSocket serverSocket = null;
			try{
				serverSocket = new ServerSocket(port);
				System.out.println("server is open for connections @Port:"+port);
				while(!isGameEnd)
				{
					Socket socket = serverSocket.accept();
					System.out.println("connection requeest from client..accepted..!");
					ServerSMTPThread serverSMTPThread = new ServerSMTPThread(socket); 
					serverSMTPThread.start();
					listOfCLients.add(serverSMTPThread);
				}
			}
			catch (IOException e){
				try {
					serverSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		class ServerSMTPThread extends Thread {
			Socket socket;
			ObjectInputStream objectInputStream;
			ObjectOutputStream objectOutputStream;
			Stack<Integer> stack = new Stack<Integer>();
			int id;
			String username;
			String date;
			ServerSMTPThread(Socket socket) {
				this.socket = socket;
				System.out.println("Thread trying to create Object Input/Output Streams");
				try
				{
						objectInputStream =
		                    new ObjectInputStream(socket.getInputStream());
						objectOutputStream =
		                   new ObjectOutputStream(socket.getOutputStream());
						System.out.println("naren..in and out are successfully created.");
				}
				catch (IOException e) {
					System.out.println("naren..IOException");
					return;
				}
				System.out.println("Buffers created successfully...");
			}
			public void run() {
				while(true) {
					try {
						StatusData statusData =  (StatusData) objectInputStream.readObject();
						System.out.println("PlayerThread id:"+statusData.getClientId());
						validateGame(statusData);
					}
					catch (IOException e) {
						break;				
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				close();
			}
			

			private boolean isValidMove(int cellNumber,int clientId) {
				System.out.println("cellNumber is:"+cellNumber);
				if(gameStatus[cellNumber] == -1){
					gameStatus[cellNumber] = clientId;
					for(int i = 1;i<37;i++){
						System.out.print(gameStatus[i]+",");
						if(i%6 == 0){
							System.out.println();
						}
					}
					return true;
				}else{
					return false;
				}
			}
			

			public void validateGame(StatusData statusData){
				String message = "";
				System.out.println("firstPlayer"+firstPlayer);
				System.out.println("Client Id:"+statusData.getClientId());
				if(statusData.getClientId() == 1 && firstPlayer){
					if(isValidMove(statusData.getCellNumber(),statusData.getClientId())){
						message = "Correct Move..Game is in Progress!!";
						stack.clear();
						if(checkGameEnd(statusData.getClientId())){
							System.out.println("Game ended..");
							//naren..
							reset.setVisible(true);
							reset.setEnabled(true);
							statusData.setGameEnd(true);
							System.out.println("player won is:"+statusData.getClientId());
							statusData.setWinner(statusData.getClientId());
						}else{
							statusData.setGameEnd(false);						
						}
						statusData.setValidMove(true);
						firstPlayer = false;
					}else{
						message = "Invalid Move-- This block is already selected";
						statusData.setValidMove(false);
					}
				}else if(statusData.getClientId() == 2 && !firstPlayer){
					if(isValidMove(statusData.getCellNumber(),statusData.getClientId())){
						message = "Correct Move..Game is in Progress!!";
						if(checkGameEnd(statusData.getClientId())){
							System.out.println("Game ended..");
							//naren..
							reset.setVisible(true);
							reset.setEnabled(true);
							statusData.setGameEnd(true);
							System.out.println("player won is:"+statusData.getClientId());
							statusData.setWinner(statusData.getClientId());
						}else{
							statusData.setGameEnd(false);
						}
						statusData.setValidMove(true);
						firstPlayer = true;
					}else{
						message = "Invalid Move-- This block is already selected";
						statusData.setValidMove(false);
					}
				}
				else{
					message = "Attempt by Player"+statusData.getClientId()+"!!!--It's Other Players Turn Now..";
					statusData.setValidMove(false);
				}
				statusData.setMessage(message);
				System.out.println("in BroadCast()...");
				System.out.println("No of Clients is:"+listOfCLients.size());
				for(int i = listOfCLients.size(); --i >= 0;){
					ServerSMTPThread serverSMTPThread = listOfCLients.get(i);
					System.out.println("client id:"+serverSMTPThread);
					System.out.println(statusData.isGameEnd());
					serverSMTPThread.outMessage(statusData);
				}
			}
			
			private boolean checkGameEnd(int clientId) {
				System.out.println("isGameEnd starts..");
				if(clientId == 2){
					for(int i = 1;i<36;i+=6){
						if(gameStatus[i] == clientId){
							int newblocks[]  = new int[37];
							for(int j = 0;j<37;j++){
								newblocks[j] = gameStatus[j];
							}
							stack.clear();
							stack.push(i);
							newblocks[i] = -1;
							checkGameEnd(newblocks,clientId);
							if(isGameEnd)
								return true;
						}
					}
				}
				else{	
					for(int i = 1;i<7;i++){
						if(gameStatus[i] == clientId){
							int newblocks[]  = new int[37];
							for(int j = 0;j<37;j++){
								newblocks[j] = gameStatus[j];
							}
							stack.clear();
							stack.push(i);
							newblocks[i] = -1;
							checkGameEnd(newblocks,clientId);
							if(isGameEnd)
								return true;
						}
					}
				}
				System.out.println("isGameEnd end..return false");
				return false;
			}
			
			private boolean outMessage(StatusData cInfo) {
				System.out.println("");
				System.out.println("cInfo id:"+cInfo.getClientId());
				if(!socket.isConnected()) {
					System.out.println("socket is closed..");
					close();
					return false;
				}else{
					System.out.println("Socket is not closed..");
				}
				try {
					System.out.println("objectOutputStream:"+objectOutputStream);
					objectOutputStream.writeObject(cInfo);
				}
				catch(IOException e) {
					System.out.println("naren.. IOException in writeMsg..");
				}
				return true;
			}
			
			private void checkGameEnd(int[] newgameStatus, int clientId) {
				if(!stack.isEmpty() && !isGameEnd){
					int top = stack.peek();
					int count = 0;
					if(clientId ==2 && top%6 == 0)
						isGameEnd = true;
					else if(clientId == 1 && top > 30)
						isGameEnd = true;
					if(!isGameEnd && top > 6 && newgameStatus[top-6] == clientId){
						count++;
						stack.push(top-6);
						newgameStatus[top-6] = -1;
					}
					if(!isGameEnd && top<31 && newgameStatus[top+6] == clientId){
						count++;
						stack.push(top+6);
						newgameStatus[top+6] = -1;
					}
					if(!isGameEnd && top > 6 && top%6 != 0&& newgameStatus[top-5] == clientId){
						count++;
						stack.push(top-5);
						newgameStatus[top-5] = -1;
					}
					if(!isGameEnd && top<31 && top%6 != 1 && newgameStatus[top+5] == clientId){
						count++;
						stack.push(top+5);
						newgameStatus[top+5] = -1;
					}
					if(!isGameEnd && top%6 != 0 && newgameStatus[top+1] == clientId){
						count++;
						stack.push(top+1);
						newgameStatus[top+1] = -1;
					}
					if(!isGameEnd && top%6 != 1&& newgameStatus[top-1] == clientId){
						count++;
						stack.push(top-1);
						newgameStatus[top-1] = -1;
					}
					if(count == 0){
						stack.pop();
					}
					if(!isGameEnd)
						checkGameEnd(newgameStatus, clientId);
				}
			}

			
			private void close() {
				try {
					if(objectOutputStream != null) objectOutputStream.close();
				}
				catch(Exception e) {}
				try {
					if(objectInputStream != null) objectInputStream.close();
				}
				catch(Exception e) {};
				try {
					if(socket != null) socket.close();
				}
				catch (Exception e) {}
			}

		
		}
	}
}