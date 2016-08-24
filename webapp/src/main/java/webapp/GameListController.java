package webapp;

import org.mypt.data.UriStrings;
import org.mypt.ui.GameData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import webapp.data.GameDataReader;

@RestController
public class GameListController {
	
	static Logger log = LoggerFactory.getLogger(GameListController.class);
    
    @RequestMapping(UriStrings.GAME_LIST)
    public GameData[] getGamelist(@RequestParam(value="criteria" , defaultValue="NO_VALUE") String criteria) {
    	
    	GameDataReader reader = new GameDataReader();
    	return reader.getCurrentGames();
    }
}


