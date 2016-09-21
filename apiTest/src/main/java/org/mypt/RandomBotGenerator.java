package org.mypt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomBotGenerator {
	
	private static final Logger log = LoggerFactory.getLogger(RandomBotGenerator.class);

	private Random random = new Random();
	private List<Bot> list = new ArrayList<Bot>();
	int index = -1;

	public RandomBotGenerator () {
		try {
			FileInputStream fstream = new FileInputStream("D:\\work\\barfight\\downtown\\zBots.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				String[] botData = strLine.split("~");
				if(botData.length > 0){
				 list.add(new Bot(botData[0], /*botData[1]*/""  ));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
		
		//Random Shuffling
		for(int i = 0; i < 100; i ++){
			Collections.shuffle(list);
		}

	}
	public Bot getRandomBot() {
	    int index = random.nextInt(list.size());
	    return list.get(index);
	}
	
	public Bot getNextBot(){
		index ++; 
		if(index >= list.size()){
			index = 0;
		}
		return list.get(index);
	}

}