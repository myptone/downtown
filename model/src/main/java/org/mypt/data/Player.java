package org.mypt.data;

public class Player {
    private String userDbId;
    private Long points;
    private int consecutiveDefendCount;
    
	public String getUserDbId() {
		return userDbId;
	}
	public void setUserDbId(String userDbId) {
		this.userDbId = userDbId;
	}
	public Long getPoints() {
		return points;
	}
	public void setPoints(Long points) {
		this.points = points;
	}
	public int getConsecutiveDefendCount() {
		return consecutiveDefendCount;
	}
	public void setConsecutiveDefendCount(int consecutiveDefendCount) {
		this.consecutiveDefendCount = consecutiveDefendCount;
	}

}
