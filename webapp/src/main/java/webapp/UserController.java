package webapp;

import java.util.UUID;

import org.mypt.DBConstants;
import org.mypt.UserDAO;
import org.mypt.data.UriStrings;
import org.mypt.data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	static Logger log = LoggerFactory.getLogger(UserController.class);
    
    @RequestMapping("/webapp/greeting")
    public User greetingSome(@RequestParam(value="name", defaultValue="Vijay") String name) {
    	User user = new  User();
    	user.set_id(UUID.randomUUID().toString());
    	user.setName(name);
    	log.info("creating User with name {}", name);
        return user;
    }
    
	@RequestMapping(value = UriStrings.CREATE_USER, method = RequestMethod.POST)
	public ResponseEntity<User> create(@RequestBody User user) {

	    if (user != null) {
	    	log.info("creating User with name {}", user.getName());
	    	UserDAO userDAO = new UserDAO();
	    	userDAO.save(user, DBConstants.USER);
	    } else {
	    	log.error("User was null!");
	    }

	    
	    return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
    
}

