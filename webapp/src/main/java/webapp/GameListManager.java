package webapp;

import java.util.Collection;
import org.mypt.data.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class GameListManager {

    private static final Logger log = LoggerFactory.getLogger(GameListManager.class);

    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 2000)
    public void manageGameList() {
    	 Collection<Game> currentGames = GameList.INSTANCE.gameMap.values();
    	 for(Game game : currentGames){
    		 switch (game.getGameStatus()) {
			case INPROGRESS:
				game.process();
				break;
			case COMPLETED:
				//currentGames.remove(game);
				//log.info("{} Game removed ", game.get_id());
				break;
			default:
				break;
			}
    	 }
        //log.info("{} Games {}", dateFormat.format(new Date()), GameList.INSTANCE.getGameCount());
    }
}
