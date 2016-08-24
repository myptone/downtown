package org.mypt.rest;

public class RoundResultRequest {
	private String gameDbId;
	private int roundCount;

	public int getRoundCount() {
		return roundCount;
	}
	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}
	public String getGameDbId() {
		return gameDbId;
	}
	public void setGameDbId(String gameDbId) {
		this.gameDbId = gameDbId;
	}
}
