package webapp.data;

import java.util.ArrayList;
import java.util.List;

import org.mypt.data.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webapp.GameList;

public class GameDataReader {
	
	static Logger log = LoggerFactory.getLogger(GameDataReader.class);
	
	public GameData getData(Game game){
		GameData data = new GameData();
		data.dbId = game.getDbId();
		data.createdByUser = game.getCreatedByUserDbId();// get users name
		data.numplayers = game.getPlayers().size();
		data.name = game.getName();
		if(game.getGameStatus() != null){
			data.status = game.getGameStatus().name();
		}
		return data;
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

}
