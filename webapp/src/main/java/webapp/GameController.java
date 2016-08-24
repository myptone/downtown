package webapp;

import org.mypt.data.Game;
import org.mypt.data.UriStrings;
import org.mypt.rest.GameJoinRequest;
import org.mypt.rest.GameJoinResponse;
import org.mypt.rest.GameJoinResponse.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	static Logger log = LoggerFactory.getLogger(GameController.class);

	GameNotifier gameNotifier = new GameNotifier();

	@RequestMapping(value = UriStrings.CREATE_GAME, method = RequestMethod.POST)
	public ResponseEntity<Game> create(@RequestBody Game game) {

		if (game != null) {
			if(game.getCreatedByUserDbId() == null || game.getCreatedByUserDbId().length() == 0){
				log.error("Game {} is an orphan game!", game.getName());
			} 
			GameList.INSTANCE.put(game);
			return new ResponseEntity<Game>(game, HttpStatus.CREATED);
		} else {
			log.error("Game was null!");
			return new ResponseEntity<Game>(game, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = UriStrings.JOIN_GAME, method = RequestMethod.POST)
	public ResponseEntity<GameJoinResponse> join(@RequestBody GameJoinRequest joinRequest ) {
		return performJoinOrExit(joinRequest, true);
	}

	@RequestMapping(value = "/webapp/exitGame", method = RequestMethod.POST)
	public ResponseEntity<GameJoinResponse> exit(@RequestBody GameJoinRequest joinRequest ) {
		return performJoinOrExit(joinRequest, false);
	}

	protected ResponseEntity<GameJoinResponse> performJoinOrExit(GameJoinRequest joinRequest, boolean isJoin){
		if (isValidGameJoinRequest(joinRequest)) {
			GameJoinResponse gameJoinResponse;
			if(isJoin){
				gameJoinResponse = GameList.INSTANCE.joinUserToGame(joinRequest);
				if(gameJoinResponse.getStatus() != null){
					switch (gameJoinResponse.getStatus()) {
					case SUCCESS:
						gameNotifier.userHasJoinedGame(joinRequest);
						break;
					case SUCCESS_GAME_IS_STARTED:
						gameNotifier.gameHasStartedWithJoiningUser(joinRequest);
						break;		
					default:
						break;
					}
				}
			}
			else {
				gameJoinResponse = GameList.INSTANCE.exitUserFromGame(joinRequest); 
				gameNotifier.userHasExitedGame(joinRequest);
			}
			return new ResponseEntity<GameJoinResponse>(gameJoinResponse, HttpStatus.OK);
		} else {
			log.error("GameJoinRequest was invalid!");
			GameJoinResponse gameJoinResponse = new GameJoinResponse();
			gameJoinResponse.setStatus(Status.FAILED_BAD_DATA);
			return new ResponseEntity<GameJoinResponse>(gameJoinResponse, HttpStatus.BAD_REQUEST);
		}
	}

	protected boolean isValidGameJoinRequest(GameJoinRequest joinRequest){
		if (joinRequest == null){
			log.error("GameJoinRequest is null");
			return false;
		}
		if (joinRequest.getGameDbId() == null){
			log.error("GameJoinRequest.getGameDbId is null");
			return false;
		}
		if (joinRequest.getGameDbId().trim().length() ==0){
			log.error("GameJoinRequest.getGameDbId is empty str");
			return false;
		}
		if (joinRequest.getUserDbId() == null){
			log.error("GameJoinRequest.getUserDbId is null");
			return false;
		}
		if (joinRequest.getUserDbId().trim().length() ==0){
			log.error("GameJoinRequest.getUserDbId is empty str");
			return false;
		}
		return true;
	}

}

