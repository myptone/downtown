package org.mypt.data;

import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class RoundForPlayerDescriptionGenerator {
	
	public String get(RoundResultForPlayer result){
		StringBuilder bldr = new StringBuilder();
		
		String move = getMove(result);
		bldr.append(result.getUserDbId()).append(move); 
	
		
		String attackedBy = getAttackedByString(result);
		if(attackedBy != null){
			bldr.append(" and ").append(attackedBy);
		}
				
		String groupAttackedBy = getGroupAttackedByString(result);
		if(groupAttackedBy != null){
			bldr.append(" and ").append(groupAttackedBy);
		}
		
		String restrainedBy = getRestrainedByString(result);
		if(restrainedBy != null){
			bldr.append(" and ").append(restrainedBy);
		}
		
		return bldr.toString();
	}
	
	
	
	private String getMove(RoundResultForPlayer result) {
		StringBuilder bldr = new StringBuilder();
		switch (result.getMove().getMoveType()) {
		case ATTACK:
			bldr.append(" attacked ").append(result.getMove().getTargetUserDbId());
			break;
		case GROUP_ATTACK:
			bldr.append(" insulted ").append(result.getMove().getTargetUserDbId());
			break;
		case UNIVERSAL_ATTACK:
			bldr.append(" called 911 ");
			if(result.isPenalizedForSimultaneousUniversalAttack()){
				bldr.append(" but so did ").append(getStringListAsString(result.getUniversallyAttackedByUserIds()));
			}
			break;
		case DEFEND:
			bldr.append(" hid behind the bar counter ");
			if(result.getConsecutiveDefendPenalty() > 1){
				bldr.append(" again ");
			}
			break;
		case DO_NOTHING:
			bldr.append(" did not make a move ");
			break;
		case RECHARGE:
			bldr.append(" is chugging a beer ");
			break;
		case RESTRAIN:
			bldr.append(" grabbed ").append(result.getMove().getTargetUserDbId());
			break;
			default:
		}
		return bldr.toString();
	}



	protected String getAttackedByString(RoundResultForPlayer result){
		if(result.getAttackedByUserIds().size() > 0){
			StringBuilder bldr = new StringBuilder();
			bldr.append(" was attacked by ").append(getStringListAsString(result.getAttackedByUserIds()));
			return bldr.toString();
		}
		return null;
	}
	
	protected String getGroupAttackedByString(RoundResultForPlayer result){
		if(result.getGroupAttackedByUserIds().size() > 0){
			StringBuilder bldr = new StringBuilder();
			bldr.append(" was insulted by ").append(getStringListAsString(result.getGroupAttackedByUserIds()));
			return bldr.toString();
		}
		return null;
	}
	
	protected String getRestrainedByString(RoundResultForPlayer result){
		if(result.getRestrainedByUserIds().size() > 0){
			StringBuilder bldr = new StringBuilder();
			bldr.append(" was grabbed by ").append(getStringListAsString(result.getRestrainedByUserIds()));
			return bldr.toString();
		}
		return null;
	}
	
	protected String getStringListAsString(List<String> strList){
		StringBuilder bldr = new StringBuilder();
		switch (strList.size()) {
		case 0:
			return null;
		case 1:
			return strList.get(0);			
		case 2:
			bldr.append(strList.get(0)).append(" and ").append(strList.get(1));
			return bldr.toString();			
		default:
			String str = StringUtils.join(", ", strList);
			if(str.lastIndexOf(",") != -1){
				bldr.append(str.substring(0,  str.lastIndexOf(","))).append(" and ").append(str.substring(str.lastIndexOf(",") + 1));
			}
			return bldr.toString();	
		}
	}
	

}
