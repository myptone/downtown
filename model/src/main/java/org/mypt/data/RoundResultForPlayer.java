package org.mypt.data;

import java.util.ArrayList;
import java.util.List;

public class RoundResultForPlayer {
	private String userDbId;
	private List<String> attackedByUserIds = new ArrayList<String>();
	private List<String> groupAttackedByUserIds = new ArrayList<String>();
	private List<String> restrainedByUserIds = new ArrayList<String>();
	private List<String> universallyAttackedByUserIds = new ArrayList<String>();
	private Long calculatedscore;
	private boolean isScoreCalculated = false;
	private String roundResultDescription;
	

	private Move move;
	private Player player;

	public void init(Move move, Player player) {
		this.userDbId = move.getUserDbId();
		this.setMove(move);
		this.player = player;
	}

	public Long calculateScore(){
		if(!isScoreCalculated){
			int score = 0;
			score = score + getUniversalAttackDamage();
			score = score + getAttackDamage();
			score = score + getGroupAttackDamage();
			score = score + getConsecutiveDefendPenalty();
			score = score + getNoMovePenalty();
			calculatedscore = Long.valueOf(score);
			isScoreCalculated = true;
			player.setPoints(player.getPoints() + calculatedscore);
			if(player.getPoints() < 0){
				player.setPoints(0L);
			}
			RoundForPlayerDescriptionGenerator generator = new RoundForPlayerDescriptionGenerator();
			roundResultDescription = generator.get(this);
		} 
		return calculatedscore;
	}

	public String getDescription(){
		return roundResultDescription;
	}

	public int getNoMovePenalty() {
		int noMovePenalty = 0;
		if(this.move.getMoveType().compareTo(MoveType.DO_NOTHING) == 0){
			noMovePenalty = -1;
		} 
		return noMovePenalty;
	}

	public int getConsecutiveDefendPenalty() {
		int consecutiveDefendPenalty = 0;
		if(isDefending()){
			player.setConsecutiveDefendCount(player.getConsecutiveDefendCount() + 1);
		} else {
			player.setConsecutiveDefendCount(0);
		}
		if(player.getConsecutiveDefendCount() > 0){
			consecutiveDefendPenalty = (player.getConsecutiveDefendCount() - 1) * -2;	
		}
		return consecutiveDefendPenalty;
	}

	public int getGroupAttackDamage() {
		int groupAttackScore = 0;
		if(groupAttackedByUserIds.size() > 1){
			groupAttackScore = groupAttackedByUserIds.size() * -2;
			if(isDefending()){
				groupAttackScore += 1;
			}
		}
		return groupAttackScore;
	}

	public int getUniversalAttackDamage(){
		int universalAttackScore = 0; 
		if(universallyAttackedByUserIds.size() == 1){
			if(isAttacked() || isAttacking() || isGrabbed() || isGroupAttacking() || isGrabbing()){
				universalAttackScore = -3;
			}
		}
		if(isDefending() || isRecharging()){
			universalAttackScore = 0;
		}
		if(isPenalizedForSimultaneousUniversalAttack()){
			universalAttackScore -= 3;
		}
		return universalAttackScore;
	}
	
	public boolean isPenalizedForSimultaneousUniversalAttack(){
		if(isUniversallyAttacking() && universallyAttackedByUserIds.size() > 1){
			return true;
		}
		return false;
	}
	
	public int getAttackDamage(){
		int attackScore = 0;
		if(attackedByUserIds.size() > 0){
			attackScore = attackedByUserIds.size() * -1;

			if(isGrabbed() || isRecharging()){
				attackScore = attackScore * 2;
			}
			if(isDefending()){
				attackScore += 1;
			}
			if(isGrabbingAttacker()){
				attackScore += 1;
			}
		}
		return attackScore;
	}

	public boolean isUniversallyAttacking() {
		if(move.getMoveType().compareTo(MoveType.UNIVERSAL_ATTACK) == 0)
			return true;
		return false;
	}

	public boolean isDefending() {
		if(move.getMoveType().compareTo(MoveType.DEFEND) == 0)
			return true;
		return false;
	}

	public boolean isGroupAttacking() {
		if(move.getMoveType().compareTo(MoveType.GROUP_ATTACK) == 0)
			return true;
		return false;
	}

	public boolean isGrabbing() {
		if(move.getMoveType().compareTo(MoveType.RESTRAIN) == 0)
			return true;
		return false;
	}

	public boolean isGroupAttacked() {
		if(this.groupAttackedByUserIds.size() >0)
			return true;
		return false;
	}

	public boolean isAttacking() {
		if(move.getMoveType().compareTo(MoveType.ATTACK) == 0)
			return true;;
		return false;
	}

	public boolean isAttacked() {
		if(this.attackedByUserIds.size() >0)
			return true;
		return false;
	}

	public boolean isGrabbingAttacker() {
		if(move.getMoveType().compareTo(MoveType.RESTRAIN) == 0){
			for(String attackedByUserId : attackedByUserIds){
				if(move.getTargetUserDbId().trim().compareTo(attackedByUserId.trim()) == 0){
					return true;
				}
			}
		}
		return false;
	}

	public boolean isRecharging() {
		if(move.getMoveType().compareTo(MoveType.RECHARGE) == 0)
			return true;;
		return false;
	}

	public boolean isGrabbed() {
		if(this.restrainedByUserIds.size() >0)
			return true;
		return false;
	}

	public void addAttackedByUserIds(String attackedByUserId) {
		this.attackedByUserIds.add(attackedByUserId);
	}

	public void addGroupAttackedByUserIds(String groupAttackedByUserId) {
		this.groupAttackedByUserIds.add(groupAttackedByUserId);
	}

	public void addRestrainedByUserIds(String restrainedByUserId) {
		this.restrainedByUserIds.add(restrainedByUserId);
	}

	public void addUniversallyAttackedByUserIds(String universallyAttackedByUserId) {
		this.universallyAttackedByUserIds.add(universallyAttackedByUserId);
	}



	public List<String> getAttackedByUserIds() {
		return attackedByUserIds;
	}

	public void setAttackedByUserIds(List<String> attackedByUserIds) {
		this.attackedByUserIds = attackedByUserIds;
	}


	public List<String> getGroupAttackedByUserIds() {
		return groupAttackedByUserIds;
	}

	public void setGroupAttackedByUserIds(List<String> groupAttackedByUserIds) {
		this.groupAttackedByUserIds = groupAttackedByUserIds;
	}

	public List<String> getRestrainedByUserIds() {
		return restrainedByUserIds;
	}

	public void setRestrainedByUserIds(List<String> restrainedByUserIds) {
		this.restrainedByUserIds = restrainedByUserIds;
	}

	public String getUserDbId() {
		return userDbId;
	}

	public void setUserDbId(String userDbId) {
		this.userDbId = userDbId;
	}

	public List<String> getUniversallyAttackedByUserIds() {
		return universallyAttackedByUserIds;
	}

	public void setUniversallyAttackedByUserIds(List<String> universallyAttackedByUserIds) {
		this.universallyAttackedByUserIds = universallyAttackedByUserIds;
	}

	public Player getPlayer(){
		return this.player;
	}

	public Move getMove() {
		return move;
	}


	public void setMove(Move move) {
		this.move = move;
	}

	public String getRoundResultDescription() {
		return roundResultDescription;
	}
}
