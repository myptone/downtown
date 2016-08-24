package webapp;

import org.mypt.data.Move;
import org.mypt.rest.PutMoveResponse;
import org.mypt.rest.PutMoveResponse.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoveController {
	
	static Logger log = LoggerFactory.getLogger(MoveController.class);
	
	GameNotifier gameNotifier = new GameNotifier();
    
	@RequestMapping(value = "/webapp/createMove", method = RequestMethod.POST)
	public ResponseEntity<PutMoveResponse> create(@RequestBody Move move) {
		if(isValidMove(move)){
	    	log.info("recording Move from User {} for Game {}", move.getUserDbId(), move.getGameDbId());
	    	PutMoveResponse putMoveResponse = GameList.INSTANCE.putMoveForUser(move);
	    	if(putMoveResponse.getStatus().compareTo(Status.SUCCESS_ROUND_OVER) == 0){
	    		gameNotifier.roundIsOver(move.getGameDbId());
	    	}
	    	return new ResponseEntity<PutMoveResponse>(putMoveResponse, HttpStatus.OK);
		} else {
	    	log.error("Move was invalid!");
	    	PutMoveResponse putMoveResponse = new PutMoveResponse();
	    	putMoveResponse.setStatus(Status.FAILED_BAD_DATA);
	    	return new ResponseEntity<PutMoveResponse>(putMoveResponse, HttpStatus.BAD_REQUEST);
		}
	}
	
	protected boolean isValidMove(Move move){
		if (move == null){
			log.error("Move is null");
			return false;
		}
		if (move.getGameDbId() == null){
			log.error("Move.getGameDbId is null");
			return false;
		}
		if (move.getGameDbId().trim().length() ==0){
			log.error("Move.getGameDbId is empty str");
			return false;
		}
		if (move.getUserDbId() == null){
			log.error("Move.getUserDbId is null");
			return false;
		}
		if (move.getUserDbId().trim().length() ==0){
			log.error("Move.getUserDbId is empty str");
			return false;
		}
		return true;
	}

    
}

