package org.mypt.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Round {
	private String dbId;
	private String gameDbId;

	private Date startTime;
	private Date endTime;
	private Integer roundCount;
	private RoundStatus roundStatus;
	private HashMap<String, Move> playerMoveMap = new HashMap<String, Move>();
	private HashMap<String, RoundResultForPlayer> roundResultMapForPlayers = new HashMap<String, RoundResultForPlayer>();



	@SuppressWarnings("unchecked")
	public Round clone(){
		Round clone = new Round();
		clone.startTime = this.startTime;
		clone.roundCount = this.roundCount;
		clone.roundStatus = this.roundStatus;
		clone.endTime = this.endTime;
		clone.playerMoveMap = (HashMap<String, Move>) this.playerMoveMap.clone();
		return clone;
	}

	public void init(int roundCount, String gameDbId, List<String> playerUserDbIdList){
		this.gameDbId = gameDbId;
		this.roundStatus = RoundStatus.INPROGRESS;
		this.startTime = new Date();
		for(String playerUserDbId : playerUserDbIdList){
			Move move = new Move();
			move.init(playerUserDbId, gameDbId);
			this.playerMoveMap.put(playerUserDbId, move);
		}
	}

	public void process(Map<String, Player> players){
		
		//initiate result map for all users
		for (Move move : playerMoveMap.values()){
			Player player = players.get(move.getUserDbId());
			if(player != null){
				RoundResultForPlayer roundResultForPlayer = new RoundResultForPlayer();
				roundResultForPlayer.init(move, player);
				roundResultMapForPlayers.put(roundResultForPlayer.getUserDbId().trim(), roundResultForPlayer);
			}
		}

		//apply targeted moves on all users
		for(Move move : playerMoveMap.values()){
			switch (move.getMoveType()) {
			case ATTACK:
				roundResultMapForPlayers.get(move.getTargetUserDbId().trim()).addAttackedByUserIds(move.getUserDbId().trim());
				break;
			case GROUP_ATTACK:
				roundResultMapForPlayers.get(move.getTargetUserDbId().trim()).addGroupAttackedByUserIds(move.getUserDbId().trim());
				break;
			case RESTRAIN:
				roundResultMapForPlayers.get(move.getTargetUserDbId().trim()).addRestrainedByUserIds(move.getUserDbId().trim());
				break;
			case UNIVERSAL_ATTACK:
				for(RoundResultForPlayer roundResultForPlayer : roundResultMapForPlayers.values()){
					if(move.getUserDbId().trim().compareTo(roundResultForPlayer.getUserDbId().trim()) == 0){
						roundResultForPlayer.addUniversallyAttackedByUserIds(move.getUserDbId().trim());
					}
				}
				break;				
			default:
				break;
			}
		}

		// finally calculate score
		for(RoundResultForPlayer roundResultForPlayer : roundResultMapForPlayers.values()){
			roundResultForPlayer.calculateScore();
		}

		roundStatus = RoundStatus.COMPLETED;
	}
	
	public List<RoundResultForPlayer> getResults(){
		return new ArrayList<RoundResultForPlayer>(roundResultMapForPlayers.values());
	}

	public boolean isTimedOut(long timeoutLimitInSeconds){
		Date now = new Date();
		long timeoutInSeconds = Math.abs(this.startTime.getTime()-now.getTime())/1000;
		if(timeoutInSeconds > timeoutLimitInSeconds){
			return true;
		}
		return false;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public RoundStatus getRoundStatus() {
		return roundStatus;
	}
	public void setRoundStatus(RoundStatus roundStatus) {
		this.roundStatus = roundStatus;
	}

	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public HashMap<String, Move> getPlayerMoveMap() {
		return playerMoveMap;
	}

	public void setPlayerMoveMap(HashMap<String, Move> playerMoveMap) {
		this.playerMoveMap = playerMoveMap;
	}

	public int getMoveCount(){
		return this.playerMoveMap.size();
	}

	public boolean haveAllPlayersMadeMoves(){
		for(Move move : playerMoveMap.values()){
			if(move.getMoveType().compareTo(MoveType.DO_NOTHING) == 0){
				return false;
			}
		}
		return true;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	public String getGameDbId() {
		return gameDbId;
	}

	public void setGameDbId(String gameDbId) {
		this.gameDbId = gameDbId;
	}

	public Integer getRoundCount() {
		return roundCount;
	}

	public void setRoundCount(Integer roundCount) {
		this.roundCount = roundCount;
	}
}
