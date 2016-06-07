/*
 * ClassName 	: PlayerThread
 * Author 		: Charan Teja
 * version		: 1.0
 * Project		: Hex_Game- Human vs Human
*/
package com;

import java.io.Serializable;

//this class is used for data transmission between client and server..
public class StatusData implements Serializable {
	
	int clientId;
	int cellNumber;
	int winner;
	boolean isValidMove;
	boolean isGameEnd;
	boolean isRestart;
	String message;
	
	public boolean isRestart() {
		return isRestart;
	}
	public void setRestart(boolean isRestart) {
		this.isRestart = isRestart;
	}
	public int getWinner() {
		return winner;
	}
	public void setWinner(int winner) {
		this.winner = winner;
	}
	public boolean isDraw() {
		return isDraw;
	}
	public void setDraw(boolean isDraw) {
		this.isDraw = isDraw;
	}
	boolean isDraw;
	public boolean isGameEnd() {
		return isGameEnd;
	}
	public void setGameEnd(boolean isGameEnd) {
		this.isGameEnd = isGameEnd;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isValidMove() {
		return isValidMove;
	}
	public void setValidMove(boolean isValidMove) {
		this.isValidMove = isValidMove;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getCellNumber() {
		return cellNumber;
	}
	public void setCellNumber(int cellNumber) {
		this.cellNumber = cellNumber;
	}
}