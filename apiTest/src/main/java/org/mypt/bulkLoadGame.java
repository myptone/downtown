package org.mypt;

import java.util.UUID;

import org.mypt.data.Game;
import org.mypt.data.UriStrings;
import org.mypt.data.User;
import org.mypt.rest.GameJoinRequest;
import org.mypt.rest.GameJoinResponse;
import org.mypt.ui.GameData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class bulkLoadGame implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(bulkLoadGame.class);
    
    static String baseUrl = "http://localhost:8080/";
    

    public static void main(String args[]) {
        SpringApplication.run(bulkLoadGame.class);
    }

    @Override
    public void run(String... args) throws Exception {
    	User creator = createUser("Vijay " + "Creator");
    	Game game = createGame("LOR " + UUID.randomUUID().toString(), creator);
    	for(int x = 0; x < 10; x++){
	    	User user = createUser("Vijay " + UUID.randomUUID().toString());
	    	joinUserToGame(game, user);
	    	String status = getGameStatus(game);
	    	log.info(status);
    	}
    }

    private String getGameStatus(Game game) {
        RestTemplate restTemplate = new RestTemplate();
        GameData[] gameDatas = restTemplate.getForObject(baseUrl + UriStrings.GAME_LIST, GameData[].class);
        for(GameData gameData : gameDatas){
        	if(gameData.dbId.compareTo(game.getDbId()) == 0){
        		return gameData.status;
        	}
        }
		return null;
	}

	private void joinUserToGame(Game game, User user) {
    	RestTemplate restTemplate = new RestTemplate();
    	GameJoinRequest gameJoinRequest = new GameJoinRequest();
    	gameJoinRequest.setGameDbId(game.getDbId());
    	gameJoinRequest.setUserDbId(user.getDbId());
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);

    	HttpEntity<GameJoinRequest> request = new HttpEntity<GameJoinRequest>(gameJoinRequest,headers);
    	
    	ResponseEntity<GameJoinResponse> response = restTemplate.
    			  exchange(baseUrl + UriStrings.JOIN_GAME, HttpMethod.POST, request, GameJoinResponse.class);
    	
    	GameJoinResponse gameJoinResponse = response.getBody();
    	
    	log.info(gameJoinResponse.getStatus().name());
		
    	//GameJoinRequest userResp = restTemplate.postForObject(baseUrl + UriStrings.JOIN_GAME, gameJoinRequest, GameJoinRequest.class);
	}

	public User createUser(String userName){
    	RestTemplate restTemplate = new RestTemplate();
    	User user = new User();
    	user.setName(userName);
    	User userResp = restTemplate.postForObject(baseUrl + UriStrings.CREATE_USER, user, User.class);
    	log.info(userResp.getName());
    	return userResp;
    }
    
    
    public Game createGame(String name, User creator){
    	RestTemplate restTemplate = new RestTemplate();
    	Game game = new Game();
    	game.setName(name);
    	game.setCreatedByUserDbId(creator.getDbId());
    	Game resp = restTemplate.postForObject(baseUrl + UriStrings.CREATE_GAME, game, Game.class);
    	log.info(resp.getName());
    	return resp;
    }
}
