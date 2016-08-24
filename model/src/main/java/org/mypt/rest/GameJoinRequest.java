package org.mypt.rest;

public class GameJoinRequest {
    private String gameDbId;
    private String userDbId;

	public String getGameDbId() {
		return gameDbId;
	}
	public void setGameDbId(String gameDbId) {
		this.gameDbId = gameDbId;
	}
	public String getUserDbId() {
		return userDbId;
	}
	public void setUserDbId(String userDbId) {
		this.userDbId = userDbId;
	}
}
