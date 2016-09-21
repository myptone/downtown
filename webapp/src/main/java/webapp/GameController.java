package webapp;

import java.util.Date;

import org.mypt.DBConstants;
import org.mypt.GameDAO;
import org.mypt.UserDAO;
import org.mypt.data.Game;
import org.mypt.data.UriStrings;
import org.mypt.data.User;
import org.mypt.rest.GameJoinRequest;
import org.mypt.rest.GameJoinResponse;
import org.mypt.rest.GameJoinResponse.Status;
import org.mypt.ui.GameData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import webapp.data.GameDataReader;

@RestController
public class GameController {

	static Logger log = LoggerFactory.getLogger(GameController.class);

	GameNotifier gameNotifier = new GameNotifier();

	@RequestMapping(value = UriStrings.CREATE_GAME, method = RequestMethod.POST)
	public ResponseEntity<Game> create(@RequestBody Game game) {
		if (game != null) {
			if(game.getCreatedByUser_id() == null || game.getCreatedByUser_id().length() == 0){
				log.error("Game {} is an orphan game!", game.getName());
				return new ResponseEntity<Game>(game, HttpStatus.BAD_REQUEST);
			} 
			game.setStartTime(new Date());
			GameDAO gameDAO = new GameDAO();
			gameDAO.save(game, DBConstants.GAME);
			GameList.INSTANCE.put(game);
			//creator joins the game here
			GameJoinRequest gameJoinRequest = new GameJoinRequest();
			gameJoinRequest.setGameDbId(game.get_id());
			gameJoinRequest.setUserDbId(game.getCreatedByUser_id());
			performJoinOrExit(gameJoinRequest, true);
			
			return new ResponseEntity<Game>(game, HttpStatus.CREATED);
		} else {
			log.error("Game was null!");
			return new ResponseEntity<Game>(game, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = UriStrings.GET_GAME, method = RequestMethod.GET)
	public ResponseEntity<GameData> get(@RequestParam(value="gameDbId") String gameDbId) {
		if (gameDbId != null && gameDbId.length() != 0) {
			Game game = GameList.INSTANCE.get(gameDbId);
			GameDataReader gameDataReader = new GameDataReader();
			GameData gameData = gameDataReader.getData(game);
			if(game == null){
				log.error("Game with dbId {} not found!", gameDbId);
				return new ResponseEntity<GameData>(new GameData(), HttpStatus.BAD_REQUEST);
			} 
			return new ResponseEntity<GameData>(gameData, HttpStatus.OK);
		} else {
			log.error("GameDbId {} null or empty string!", gameDbId);
			return new ResponseEntity<GameData>(new GameData(), HttpStatus.BAD_REQUEST);
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
				
				//update User Name in GameJoinRequest and Player
				UserDAO userDAO = new UserDAO();
				User user = userDAO.load(joinRequest.getUserDbId());
				if(user == null){
					log.error("Cannot join User {} to Game {}", joinRequest.getUserDbId(), joinRequest.getGameDbId());
					gameJoinResponse = new GameJoinResponse();
					gameJoinResponse.setStatus(Status.FAILED_BAD_DATA);
					return new ResponseEntity<GameJoinResponse>(gameJoinResponse, HttpStatus.BAD_REQUEST);
				}
				joinRequest.setUserName(user.getName());
				
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

