package org.mypt.data;

public class GameSettings {
	private Long roundDurationInSecs = 10L;
	private Long minimumPlayers = 8L;

	public Long getRoundDurationInSecs() {
		return roundDurationInSecs;
	}

	public void setRoundDurationInSecs(Long roundDurationInSecs) {
		this.roundDurationInSecs = roundDurationInSecs;
	}

	public Long getMinimumPlayers() {
		return minimumPlayers;
	}

	public void setMinimumPlayers(Long minimumPlayers) {
		this.minimumPlayers = minimumPlayers;
	}
	
}
