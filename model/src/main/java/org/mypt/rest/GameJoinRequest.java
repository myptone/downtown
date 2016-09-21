package org.mypt.rest;

public class GameJoinRequest {
    private String gameDbId;
    private String userDbId;
    private String userName;

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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
