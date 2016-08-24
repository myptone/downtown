package org.mypt.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {
	
	static Logger log = LoggerFactory.getLogger(Game.class);
	
    private String dbId;
    private String name;
    private String createdByUserDbId;
    private Date startTime;
    private Round currentRound;
    private List<Round> pastRounds = new ArrayList<Round>();
    private Map<String, Player> players = new HashMap<String, Player>();
    private GameStatus gameStatus;
    private GameSettings gameSettings;
    
    public Round getRound(int index){
    	if(currentRound.getRoundCount() == index){
    		return currentRound;
    	}
    	for(Round round : pastRounds){
    		if(round.getRoundCount() == index){
    			return round;
    		}
    	}
    	return null;
    }
    
	public Boolean haveAllPlayersMadeMovesForCurrentRound(){
		if(currentRound != null){
			return currentRound.haveAllPlayersMadeMoves();
		} else {
			log.error("currentRound is null");
		}
		return null;
	}
	
	public boolean hasMinimumPlayers(){
		if(this.getPlayers().size() >= this.getGameSettings().getMinimumPlayers()){
			return true;
		}
		return false;
	}
	
	public boolean initGame(){
		if(this.getGameStatus().equals(GameStatus.INITIATED) && this.hasMinimumPlayers()){
			currentRound = new Round();
			currentRound.init(0, dbId, getPlayerUserDbIdList());
			gameStatus = GameStatus.INPROGRESS;
			return true;
		}
		return false;
	}
	
	public void processCurrentRound(){
		currentRound.process(players);
	}
	
	protected List<String> getPlayerUserDbIdList(){
		List<String> playerUserDbIdList = new ArrayList<String>();
		for(String playerUserDbId : players.keySet()){
			playerUserDbIdList.add(playerUserDbId.trim());
		}
		return playerUserDbIdList;
	}
    

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Round getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(Round currentRound) {
		this.currentRound = currentRound;
	}

	public List<Round> getPastRounds() {
		return pastRounds;
	}
	
	public void setPastRounds(List<Round> pastRounds) {
		this.pastRounds = pastRounds;
	}
	
	public void addRoundToPastRounds(Round round){
		round.setRoundStatus(RoundStatus.COMPLETED);
		this.pastRounds.add(round);
	}
	


	public String getCreatedByUserDbId() {
		return createdByUserDbId;
	}

	public void setCreatedByUserDbId(String createdByUserDbId) {
		this.createdByUserDbId = createdByUserDbId;
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public Map<String, Player> getPlayers() {
		return players;
	}

	public void setPlayers(Map<String, Player> players) {
		this.players = players;
	}

	public GameSettings getGameSettings() {
		return gameSettings;
	}

	public void setGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

}
