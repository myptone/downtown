/********************DATA CLASSES REQUIRED**************/
function User(){}
function GameJoinRequest(gameDbId, userDbId){
	this.gameDbId = gameDbId;
	this.userDbId = userDbId;
}
function  Move(gameDbId, userDbId, moveType, targetUserDbId){
	this.gameDbId = gameDbId;
	this.userDbId = userDbId;
    this.moveType = moveType;
	this.targetUserDbId = targetUserDbId;
}
function GameDef(name, createdByUser_id){
	this.name = name;
	this.createdByUser_id = createdByUser_id;
}
/********************DATA CLASSES END*******************/
/********************GLOBAL STORAGE**************/
var thisUserDbId;
/********************GLOBAL STORAGE END*******************/





//http://stackoverflow.com/questions/105034/create-guid-uuid-in-javascript
function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
}