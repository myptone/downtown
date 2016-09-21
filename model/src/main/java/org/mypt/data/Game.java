package org.mypt.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game extends BDOBase{
	
	static Logger log = LoggerFactory.getLogger(Game.class);
	static int ROUND_DURATION_SECS = 10; 
	
    private String name;
    private String createdByUser_id;
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
			currentRound.init(1, _id, getPlayerUserDbIdList());
			gameStatus = GameStatus.INPROGRESS;
			return true;
		}
		return false;
	}
	
	public void process(){
		 Date now = new Date();
		 long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - this.getCurrentRound().getStartTime().getTime());
		 if(ROUND_DURATION_SECS <= diffInSeconds){
			 if(this.getCurrentRound().getRoundStatus().compareTo(RoundStatus.INPROGRESS) ==0){
				 this.getCurrentRound().setRoundStatus(RoundStatus.PROCESSING);
				 this.processCurrentRound();
				 for(RoundResultForPlayer roundResultForPlayer : this.getCurrentRound().getResults()){
					 log.info(" {} roundScore {} player points {}", roundResultForPlayer.getUserDbId(), roundResultForPlayer.calculateScore(), roundResultForPlayer.getPlayer().getPoints());
					 if(roundResultForPlayer.getPlayer().getPoints() == 0){
						 log.info(" {} player is knocked out!", roundResultForPlayer.getUserDbId(), roundResultForPlayer.calculateScore(), roundResultForPlayer.getPlayer().getPoints());
					 }
				 }
				 if(this.isGameOver()){
					 // publish some stuff
					 this.setGameStatus(GameStatus.PROCESSING);
					 log.info("Game {} over", this.get_id());
					 this.setGameStatus(GameStatus.COMPLETED);
				 } else {
					 log.info("Timing out game {} round {}", this.get_id(), this.getCurrentRound().getRoundCount());
					 this.startNewRound();
				 	log.info("For game {} new round {} begins", this.get_id(), this.getCurrentRound().getRoundCount());
				 }
			 }
		 }
	}
	
	public void processCurrentRound(){
		currentRound.process(players);
	}
	
	public void startNewRound(){
		int currentRoundCount = currentRound.getRoundCount();
		addRoundToPastRounds(currentRound);
		currentRound = new Round();
		currentRound.init(currentRoundCount + 1, _id, getPlayerUserDbIdList());
		gameStatus = GameStatus.INPROGRESS;
	}
	
	public boolean isGameOver(){
		int countOfPlayersWithNonZeroPoints = 0;
		for(Player player : this.players.values()){
			if(player.getPoints() > 0)
				countOfPlayersWithNonZeroPoints++;
		}
		if(countOfPlayersWithNonZeroPoints < 3){
			return true;
		}
		return false;
	}
	
	protected List<String> getPlayerUserDbIdList(){
		List<String> playerUserDbIdList = new ArrayList<String>();
		for(String playerUserDbId : players.keySet()){
			playerUserDbIdList.add(playerUserDbId.trim());
		}
		return playerUserDbIdList;
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

	public String getCreatedByUser_id() {
		return createdByUser_id;
	}

	public void setCreatedByUser_id(String createdByUser_id) {
		this.createdByUser_id = createdByUser_id;
	}

}
