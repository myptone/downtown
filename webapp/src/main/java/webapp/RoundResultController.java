package webapp;

import java.util.List;

import org.mypt.data.RoundResultForPlayer;
import org.mypt.rest.RoundResultRequest;
import org.mypt.rest.RoundResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoundResultController {

	static Logger log = LoggerFactory.getLogger(RoundResultController.class);

	GameNotifier gameNotifier = new GameNotifier();

	@RequestMapping(value = "/webapp/getResults", method = RequestMethod.POST)
	public ResponseEntity<RoundResultResponse> create(@RequestBody RoundResultRequest req) {
		RoundResultResponse response = new RoundResultResponse();
		if (isValidRoundResultRequest(req)) {
			List<RoundResultForPlayer> roundResultForPlayers = GameList.INSTANCE.getRoundResults(req.getGameDbId(), req.getRoundCount());
			
			response.roundResultForPlayers = roundResultForPlayers;
			return new ResponseEntity<RoundResultResponse>(response, HttpStatus.CREATED);
		} else {
			log.error("Game was null!");
			return new ResponseEntity<RoundResultResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}

	protected boolean isValidRoundResultRequest(RoundResultRequest request){
		if (request == null){
			log.error("RoundResultRequest is null");
			return false;
		}
		if (request.getGameDbId() == null){
			log.error("RoundResultRequest.getGameDbId is null");
			return false;
		}
		if (request.getGameDbId().trim().length() ==0){
			log.error("RoundResultRequest.getGameDbId is empty str");
			return false;
		}
		if (request.getRoundCount() == 0){
			log.error("RoundResultRequest.getRoundCount is 0");
			return false;
		}
		return true;
	}

}



