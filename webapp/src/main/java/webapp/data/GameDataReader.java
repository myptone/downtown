package webapp.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mypt.UserDAO;
import org.mypt.data.Game;
import org.mypt.data.Player;
import org.mypt.data.Round;
import org.mypt.data.RoundResultForPlayer;
import org.mypt.data.User;
import org.mypt.ui.GameData;
import org.mypt.ui.PlayerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webapp.GameList;

public class GameDataReader {
	
	static Logger log = LoggerFactory.getLogger(GameDataReader.class);
	
	public GameData getData(Game game){
		GameData data = new GameData();
		if(game == null){
			return data;
		}
		data._id = game.get_id();
		data.numplayers = game.getPlayers().size();
		
		UserDAO userDAO = new UserDAO();
		User user = userDAO.load(game.getCreatedByUser_id());
		if(user != null){
			data.createdByUser = user.getName();			
		}
		
		data.name = game.getName();
		data.startTime = game.getStartTime();
		if(game.getGameStatus() != null){
			data.status = game.getGameStatus().name();
		}

		ArrayList<PlayerData> playerDataList = new ArrayList<PlayerData>();
		Collection<Player> playerCollection = game.getPlayers().values();
		for(Player player : playerCollection){
			PlayerData playerData = getData(player); 
			RoundResultForPlayer roundResultForPlayer = getPreviousRoundResultForPlayer(player.getUser_id(), game);
			if(roundResultForPlayer != null){
				playerData.prevRoundDescription = roundResultForPlayer.getDescription();
				if(playerData.prevRoundDescription != null){
					playerData.prevRoundDescription = cleanDescription(playerData.prevRoundDescription, playerCollection);
				}
			}
			playerDataList.add(playerData);
		}
	
		data.players = new PlayerData[playerDataList.size()];
		data.players = (PlayerData[]) playerDataList.toArray(data.players);
		
		data.currentRound = game.getCurrentRound();
		
		return data;
	}
	
	private String cleanDescription(String dirtyDesc, Collection<Player> playerCollection){
		for(Player player : playerCollection){
			dirtyDesc = dirtyDesc.replace(player.getUser_id(), player.getUserName());
		}
		return dirtyDesc;
	}
	
	RoundResultForPlayer getPreviousRoundResultForPlayer(String userDbId, Game game){
		if(game.getPastRounds().size() > 0){
			Round lastRound = game.getPastRounds().get(game.getPastRounds().size() - 1);
			for(RoundResultForPlayer roundResultForPlayer : lastRound.getResults()){
				if(roundResultForPlayer.getUserDbId().compareTo(userDbId) == 0){
					return roundResultForPlayer;
				}
			}
		}
		return null;
	}
	
	public GameData[] getCurrentGames(){
    	if(GameList.INSTANCE.gameMap != null && GameList.INSTANCE.gameMap.values() != null){
    		List<GameData> gameDataList = new ArrayList<GameData>();
    		for(Game game : GameList.INSTANCE.gameMap.values()){
    			gameDataList.add(getData(game));
    		}
    		GameData[] gameDatas = gameDataList.toArray(new GameData[gameDataList.size()]);
    		return gameDatas;
    	} else {
    		log.error("GameList.INSTANCE.gameMap is NULL");
    		return null;
    	}
	}
	
	public PlayerData getData(Player player){
		PlayerData data = new PlayerData();
		data.consecutiveDefendCount = player.getConsecutiveDefendCount();
		data.name = player.getUserName();
		data.userDbId = player.getUser_id();
		data.points = player.getPoints();
		return data;
	}

}
