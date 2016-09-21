package webapp;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.mypt.data.Game;
import org.mypt.data.GameSettings;
import org.mypt.data.GameStatus;
import org.mypt.data.Move;
import org.mypt.data.Player;
import org.mypt.data.Round;
import org.mypt.data.RoundResultForPlayer;
import org.mypt.data.RoundStatus;
import org.mypt.rest.GameJoinRequest;
import org.mypt.rest.GameJoinResponse;
import org.mypt.rest.GameJoinResponse.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mypt.rest.PutMoveResponse;

public enum GameList {
	INSTANCE; 
	public ConcurrentHashMap<String, Game> gameMap = new ConcurrentHashMap<String, Game>();
	
	static Logger log = LoggerFactory.getLogger(GameList.class);
	
	public synchronized void put(Game game){
		
    	log.info("creating Game with name {} by user {}", game.getName(), game.getCreatedByUser_id());
    	game.setStartTime(new Date());
		
		//add creator to the game
		Player player = new Player();
		player.setPoints(10L);
		player.setUser_id(game.getCreatedByUser_id().trim()); 
		game.getPlayers().put(game.getCreatedByUser_id().trim(), player);
		
		//add settings if null
		if(game.getGameSettings() == null){
			GameSettings gameSettings = new GameSettings();
			game.setGameSettings(gameSettings);
		}

		//initiate game
		game.setGameStatus(GameStatus.INITIATED);
		gameMap.put(game.get_id(), game);
		
		
	}
	
	public synchronized Game remove(String gameDbId){
		return gameMap.remove(gameDbId);
	}
	
	public synchronized Game get(String gameDbId){
		return gameMap.get(gameDbId);
	}
	
	public synchronized GameJoinResponse joinUserToGame(GameJoinRequest joinRequest){
		GameJoinResponse gameJoinResponse = new GameJoinResponse();
		if(gameMap.get(joinRequest.getGameDbId()) != null && joinRequest.getUserDbId() != null){
				Game game = gameMap.get(joinRequest.getGameDbId());
				switch (game.getGameStatus()) {
				case INITIATED:
					Player player = new Player();
					player.setUserName(joinRequest.getUserName());
					player.setPoints(10L);
					player.setUser_id(joinRequest.getUserDbId().trim());
					gameMap.get(joinRequest.getGameDbId()).getPlayers().put(joinRequest.getUserDbId().trim(), player);
					//Try initiating the game
					if(game.initGame()){
						gameJoinResponse.setStatus(Status.SUCCESS_GAME_IS_STARTED);
					} else {
						gameJoinResponse.setStatus(Status.SUCCESS);	
					}
					break;
					case COMPLETED:
					case PROCESSING:
					case INPROGRESS:
						gameJoinResponse.setStatus(Status.FAILED_GAME_IS_STARTED);
						break;
						default:
							log.error("WTF !!!!!!!!!! {}", game.getGameStatus());
					break;
				}
		}
		
		if(gameMap.get(joinRequest.getUserDbId()) != null) {
			gameJoinResponse.setStatus(Status.FAILED_BAD_DATA);
		}
		
		if(gameMap.get(joinRequest.getGameDbId()) == null) {
			gameJoinResponse.setStatus(Status.FAILED_GAME_IS_NOT_AVAILABLE);
		}
		return gameJoinResponse;
	}
	public synchronized GameJoinResponse exitUserFromGame(GameJoinRequest joinRequest){
		GameJoinResponse gameJoinResponse = new GameJoinResponse();
		if(gameMap.get(joinRequest.getGameDbId()) != null && joinRequest.getUserDbId() != null){
			
				gameMap.get(joinRequest.getGameDbId()).getPlayers().remove(joinRequest.getUserDbId().trim());
				//TODO Game over???
				gameJoinResponse.setStatus(Status.SUCCESS);
			
		}

		if(gameMap.get(joinRequest.getUserDbId()) != null) {
			gameJoinResponse.setStatus(Status.FAILED_BAD_DATA);
		}
		
		if(gameMap.get(joinRequest.getGameDbId()) != null) {
			gameJoinResponse.setStatus(Status.FAILED_GAME_IS_NOT_AVAILABLE);
		}
		
		return gameJoinResponse;
	}
	
	public synchronized PutMoveResponse putMoveForUser(Move move){
		PutMoveResponse putMoveResponse = new PutMoveResponse();
		if(gameMap.get(move.getGameDbId()) != null && move.getUserDbId() != null){
			Game game = gameMap.get(move.getGameDbId());
			switch (game.getGameStatus()) {
			case INPROGRESS:
				Round round = game.getCurrentRound();
				switch (round.getRoundStatus()) {
				case INPROGRESS:
					game.getCurrentRound().getPlayerMoveMap().put(move.getUserDbId(), move);
					if(game.getCurrentRound().haveAllPlayersMadeMoves()){
						game.getCurrentRound().setRoundStatus(RoundStatus.PROCESSING);
						putMoveResponse.setStatus(PutMoveResponse.Status.SUCCESS_ROUND_OVER);	
					} else {
						putMoveResponse.setStatus(PutMoveResponse.Status.SUCCESS);
					}
					break;
					case PROCESSING:
					case COMPLETED:
						putMoveResponse.setStatus(PutMoveResponse.Status.FAILED_ROUND_OVER);
						break;
				default:
					log.error("WTF !!!!!!!!!! {}", round.getRoundStatus());
					putMoveResponse.setStatus(PutMoveResponse.Status.FAILED_BAD_DATA);
					break;
				}
				break;
				case PROCESSING:
				case COMPLETED:
					putMoveResponse.setStatus(PutMoveResponse.Status.FAILED_GAME_OVER);
					break;
				case INITIATED:
					putMoveResponse.setStatus(PutMoveResponse.Status.FAILED_GAME_NOT_STARTED);
					break;
			default:
				log.error("WTF !!!!!!!!!! {}", game.getGameStatus());
				putMoveResponse.setStatus(PutMoveResponse.Status.FAILED_BAD_DATA);
				break;
			}
			
			//process round if it is over 
			if(putMoveResponse.getStatus().compareTo(PutMoveResponse.Status.SUCCESS_ROUND_OVER) == 0){
				game.processCurrentRound();
			}
			
		} else {
			putMoveResponse.setStatus(PutMoveResponse.Status.FAILED_BAD_DATA);
		}
		return putMoveResponse;
	}
		
	public synchronized List<RoundResultForPlayer> getRoundResults(String gameDbId, int roundCount){
		Game game = gameMap.get(gameDbId);
		Round round = game.getRound(roundCount);
		if(round != null){
			switch (round.getRoundStatus()) {
			case COMPLETED:
				return round.getResults();
			case INPROGRESS:
				if(round.haveAllPlayersMadeMoves() || round.isTimedOut(game.getGameSettings().getRoundDurationInSecs())){
					round.process(game.getPlayers());
					return round.getResults();
				}
				break;
			case PROCESSING:
				return null;
			default:
				break;
			}
			
		}
		return null;
	}
	
	public synchronized int getGameCount(){
		return gameMap.values().size(); 
	}
}
