package org.mypt;

import java.io.IOException;

import org.bson.Document;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.mypt.data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.util.JSON;

public class UserDAO extends BaseDAO {
	private static final Logger log = LoggerFactory.getLogger(UserDAO.class);
	
	public User load(String _id){
		User user = null;		
		ObjectMapper mapper = new  ObjectMapper();
		
		Document document = loadById(_id, DBConstants.USER);
		String jsonString = JSON.serialize(document);
		try {
			user = mapper.readValue(jsonString, User.class);
		} catch (JsonParseException e) {
			log.error("",e);
		} catch (JsonMappingException e) {
			log.error("",e);
		} catch (IOException e) {
			log.error("",e);
		}
		return user;
	}
}
