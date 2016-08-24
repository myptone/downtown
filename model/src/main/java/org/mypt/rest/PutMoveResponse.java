package org.mypt.rest;

public class PutMoveResponse {

	public enum Status{
		SUCCESS,
		SUCCESS_ROUND_OVER,
		FAILED_BAD_DATA,
		FAILED_ROUND_OVER,
		FAILED_GAME_OVER,
		FAILED_GAME_NOT_STARTED
	}
	private Status status;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}		 
}
