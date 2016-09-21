package org.mypt.data;

public class Player {
    private String user_id;
    private Long points;
    private int consecutiveDefendCount;
    private String userName;
    

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
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
