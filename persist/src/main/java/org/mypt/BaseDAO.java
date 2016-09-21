package org.mypt;

import java.io.IOException;
import java.util.UUID;

import org.bson.Document;
import org.codehaus.jackson.map.ObjectMapper;
import org.mypt.data.BDOBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class BaseDAO {
	
	private static final Logger log = LoggerFactory.getLogger(BaseDAO.class);
	MongoClient mongoClient = null;
	
	public MongoCollection<Document> connect(String collectionName){
		log.debug("Connecting to database...");
		mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("barfight");
		MongoCollection<Document> collection = db.getCollection(collectionName);
		
		return collection;
	}
	
	public void close(){
		log.debug("Closing connection to database...");
		mongoClient.close();
	}
	
	public DAOStatus save(BDOBase obj, String collectionName){
		//https://avaldes.com/java-connecting-to-mongodb-3-2-examples/
		String jsonString;
		Document doc;
		ObjectMapper mapper;    

		MongoCollection<Document> collection = connect(collectionName);
		
		if(obj.get_id() == null || obj.get_id().length() == 0){
			obj.set_id(UUID.randomUUID().toString());
		}

		// Use Jackson to convert Object to JSON String
		mapper = new ObjectMapper();
		try {
			jsonString = mapper.writeValueAsString(obj);

			// Insert JSON into MongoDB
			log.debug("saving obj {}",jsonString);
			doc = Document.parse(jsonString);
			collection.insertOne(doc);
		} catch (IOException ioe) {
			log.error("",ioe);
		} finally {
			close();
		}

		return DAOStatus.SUCCESS;
	}

	
	public Document loadById(String _id, String collectionName){
		Document document = null;
		MongoCollection<Document> collection = connect(collectionName);
		try (MongoCursor<Document> cursor = collection.find(new BasicDBObject("_id", _id)).iterator()) {
		    while (cursor.hasNext()) {
		    	document = cursor.next();
		    }
		}
		
		return document;
	}
	
}
