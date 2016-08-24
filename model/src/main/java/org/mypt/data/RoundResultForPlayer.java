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
	

	private Move move;
	private Player player;

	public void init(Move move, Player player) {
		this.userDbId = move.getUserDbId();
		this.setMove(move);
		this.player = player;
	}

	public Long calculateScore(){
		int score = 0;
		score = score + getUniversalAttackDamage();
		score = score + getAttackDamage();
		score = score + getGroupAttackDamage();
		score = score + getConsecutiveDefendPenalty();
		calculatedscore = Long.valueOf(score);
		return calculatedscore;
	}

	private int getConsecutiveDefendPenalty() {
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

	private int getGroupAttackDamage() {
		int groupAttackScore = 0;
		if(groupAttackedByUserIds.size() > 1){
			groupAttackScore = groupAttackedByUserIds.size() * -2;
			if(isDefending()){
				groupAttackScore += 1;
			}
		}
		return groupAttackScore;
	}

	private int getUniversalAttackDamage(){
		int universalAttackScore = 0; 
		if(universallyAttackedByUserIds.size() == 1){
			if(isAttacked() || isAttacking() || isGrabbed() || isGroupAttacking() || isGrabbing()){
				universalAttackScore = -3;
			}
		}
		if(isDefending() || isRecharging()){
			universalAttackScore = 0;
		}
		if(isUniversallyAttacking() && universallyAttackedByUserIds.size() >0){
			universalAttackScore -= 3;
		}
		return universalAttackScore;
	}
	
	private int getAttackDamage(){
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

	private boolean isUniversallyAttacking() {
		if(move.getMoveType().compareTo(MoveType.UNIVERSAL_ATTACK) == 0)
			return true;
		return false;
	}

	private boolean isDefending() {
		if(move.getMoveType().compareTo(MoveType.DEFEND) == 0)
			return true;
		return false;
	}

	private boolean isGroupAttacking() {
		if(move.getMoveType().compareTo(MoveType.GROUP_ATTACK) == 0)
			return true;
		return false;
	}

	private boolean isGrabbing() {
		if(move.getMoveType().compareTo(MoveType.RESTRAIN) == 0)
			return true;
		return false;
	}

//	private boolean isGroupAttacked() {
//		if(this.groupAttackedByUserIds.size() >0)
//			return true;
//		return false;
//	}

	private boolean isAttacking() {
		if(move.getMoveType().compareTo(MoveType.ATTACK) == 0)
			return true;;
		return false;
	}

	private boolean isAttacked() {
		if(this.attackedByUserIds.size() >0)
			return true;
		return false;
	}

	private boolean isGrabbingAttacker() {
		if(move.getMoveType().compareTo(MoveType.ATTACK) == 0){
			for(String attackedByUserId : attackedByUserIds){
				if(move.getTargetUserDbId().trim().compareTo(attackedByUserId.trim()) == 0){
					return true;
				}
			}
		}
		return false;
	}

	private boolean isRecharging() {
		if(move.getMoveType().compareTo(MoveType.RECHARGE) == 0)
			return true;;
		return false;
	}

	private boolean isGrabbed() {
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


	public Move getMove() {
		return move;
	}


	public void setMove(Move move) {
		this.move = move;
	}


}
