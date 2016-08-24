package webapp;

import org.mypt.rest.GameJoinRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameNotifier {
	
	static Logger log = LoggerFactory.getLogger(GameNotifier.class);
	
	public void userHasJoinedGame(GameJoinRequest joinRequest){
		log.info("{} user has joined game {}", joinRequest.getGameDbId(), joinRequest.getUserDbId());
	}
	
	public void userHasExitedGame(GameJoinRequest joinRequest){
		log.info("{} user has exited game {}", joinRequest.getGameDbId(), joinRequest.getUserDbId());
	}
	
	public void gameHasStartedWithJoiningUser(GameJoinRequest joinRequest){
		log.info("{} game has started with joining User {}", joinRequest.getGameDbId(), joinRequest.getUserDbId());
	}
	
	public void roundIsOver(String gameDbId){
		log.info("{} game round is over", gameDbId);
	}
	
}
