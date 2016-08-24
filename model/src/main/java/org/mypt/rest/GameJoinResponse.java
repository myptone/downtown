package org.mypt.rest;

public class GameJoinResponse {

	public enum Status{
		SUCCESS,
		SUCCESS_GAME_IS_STARTED,
		FAILED_GAME_IS_STARTED,
		FAILED_GAME_IS_NOT_AVAILABLE,
		FAILED_BAD_DATA
	}
	private Status status;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}		 
}
