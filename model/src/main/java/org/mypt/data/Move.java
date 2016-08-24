package org.mypt.data;

public class Move {
    private String dbId;
    private String gameDbId;
    private String userDbId;
    private int round;
    private String targetUserDbId;
    private MoveType moveType;
    
    public void init(String userDbId, String gameDbId){
    	this.gameDbId = gameDbId;
    	this.userDbId = userDbId;
    	this.moveType = MoveType.DO_NOTHING;
    }

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	public String getUserDbId() {
		return userDbId;
	}

	public void setUserDbId(String userDbId) {
		this.userDbId = userDbId;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public String getTargetUserDbId() {
		return targetUserDbId;
	}

	public void setTargetUserDbId(String targetUserDbId) {
		this.targetUserDbId = targetUserDbId;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}

	public String getGameDbId() {
		return gameDbId;
	}

	public void setGameDbId(String gameDbId) {
		this.gameDbId = gameDbId;
	}
}
