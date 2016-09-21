    var GameListEntry = React.createClass({
    play : function(){
		var gameJoinRequest = new GameJoinRequest(this.props._id, thisUserDbId);
		var json = JSON.stringify(gameJoinRequest); 
		$.ajax({
			type: "POST",
			data: json,
			url: "http://localhost:8080/webapp/joinGame",
			contentType: "application/json; charset=utf-8",
			dataType: 'json',
			cache: false,
			success: function(data) {
				console.log(data);
				renderGame(this.props._id, "play");		
				}.bind(this),
			error: function(xhr, status, err) {
				console.error(this.props.url, status, err.toString());
				}.bind(this)
		});		
    },	
    view : function(){
	  renderGame(this.props._id, "view");
    },	
    render : function(){
		var action;
		if(this.props.status == "INITIATED"){
			action = <a href="#" className="small success button" onClick={this.play}>JOIN</a>
		} else {
			action = <a href="#" className="small secondary button" onClick={this.view}>VIEW</a>
		}	
		
		
    return (
      <div className="callout">
        <div onClick={this.handleClick}>{this.props.name}</div>
		<div>Status : {this.props.status} Players : {this.props.numplayers} {action} </div>	
      </div>
      )
    }
    });

    var GameList = React.createClass({
    render: function(){
      var gameNodes = this.props.data.map(function(game){
      return (
      <GameListEntry  _id={game._id} name={game.name} status={game.status} numplayers={game.numplayers} />        
      )
      });
      return (
        <div>
          {gameNodes}
        </div>
      )
    }
    });

    var MenuBox = React.createClass({
    gotoGames : function(){
      renderGames();
    },
    gotoUser : function(){
      renderUserSetup();
    },    
    render : function(){
	var menuItems;
	if(thisUserDbId == null){
		menuItems =       <div>
        <a href="#" className="button" onClick={this.gotoUser}>User</a>  
      </div>
	} else {
		menuItems = <div>
        <a href="#" className="button" onClick={this.gotoGames}>Games</a>  
        <a href="#" className="button" onClick={this.gotoUser}>User</a>  
      </div>
	}	
      return (
      <div>
	  {menuItems}
      </div>
      )
    }
    });

    var GamesBox = React.createClass({
	getInitialState: function() {
		return {data: []};
	},
	loadGamesFromServer: function() {
		$.ajax({
		  url: "http://localhost:8080/webapp/gamelist",
		  dataType: 'json',
		  cache: false,
		  success: function(data) {
			console.log(data);
			this.setState({data: data});
		  }.bind(this),
		  error: function(xhr, status, err) {
			console.error(this.props.url, status, err.toString());
		  }.bind(this)
		});
	},
	componentWillMount: function() {
		this.loadGamesFromServer();
		this.timer = setInterval(this.loadGamesFromServer, this.props.pollInterval);
	},	
    componentWillUnmount: function () {
        clearInterval(this.timer);
    },	
    startNewGame : function(){
      renderNewGame();
    },
    render : function(){
      return (
      <div>
      <MenuBox />
        <a href="#" className="button" onClick={this.startNewGame}>Start New Game</a>         
        <GameList data={this.state.data}/>
      </div>
      )
    }
    });

    var UserBox = React.createClass({
    save : function(){
		var user = new User();
		user.name = document.getElementById('userNameInput').value;
		var json = JSON.stringify(user); 
		$.ajax({
			type: "POST",
			data: json,
			url: "http://localhost:8080/webapp/createUser",
			contentType: "application/json; charset=utf-8",
			dataType: 'json',
			cache: false,
			success: function(data) {
				console.log(data);
				thisUserDbId = data._id;
				this.setState({data: data});
				}.bind(this),
			error: function(xhr, status, err) {
				console.error(this.props.url, status, err.toString());
				}.bind(this)
		}); 	  
    },    
    render : function(){
      return (
      <div>
      <MenuBox />
        <label>Name</label>
        <input type="text" placeholder="" id="userNameInput" />
        <a href="#" className="button expanded" onClick={this.save}>Save</a>               
      </div>
      )
    }
    });

    var NewGameBox = React.createClass({
    save : function(){
		var game = new GameDef(document.getElementById('gameName').value, thisUserDbId);
		var json = JSON.stringify(game); 
		$.ajax({
			type: "POST",
			data: json,
			url: "http://localhost:8080/webapp/createGame",
			contentType: "application/json; charset=utf-8",
			dataType: 'json',
			cache: false,
			success: function(data) {
				console.log(data);
				renderGame(data._id, "play");	
				}.bind(this),
			error: function(xhr, status, err) {
				console.error(this.props.url, status, err.toString());
				}.bind(this)
		}); 	  	  
    },    
    render : function(){
      return (
      <div>
      <MenuBox />
        <label>Name</label>
        <input type="text" placeholder="" id="gameName" />        
        <a href="#" className="button expanded" onClick={this.save}>Save</a>               
      </div>
      )
    }
    });

    var GamePlayerListEntry = React.createClass({
    sendMove : function(move){
		var move = new Move(this.props.gameDbId, thisUserDbId, move, this.props.userDbId)
		var json = JSON.stringify(move); 
		$.ajax({
			type: "POST",
			data: json,
			url: "http://localhost:8080/webapp/createMove",
			contentType: "application/json; charset=utf-8",
			dataType: 'json',
			cache: false,
			success: function(data) {
				console.log(data);
				}.bind(this),
			error: function(xhr, status, err) {
				console.error(this.props.url, status, err.toString());
				}.bind(this)
		}); 	  
    },		
	attack : function(){
		this.sendMove("ATTACK");
    },	
	restrain : function(){
	  this.sendMove("RESTRAIN");
    },
	group_attack : function(){
	  this.sendMove("GROUP_ATTACK");
    },	
    render : function(){
		var ctrls;
		if(this.props.mode == "play" && this.props.gameStatus == "INPROGRESS"){
			ctrls = <div>
				<a href="#" className="small alert button" onClick={this.attack}>PUNCH</a>
				<a href="#" className="small alert button" onClick={this.restrain}>GRAB</a>
				<a href="#" className="small alert button" onClick={this.group_attack}>INSULT</a>
			</div>
		} 		
    return (
      <div className="callout">
        <div>{this.props.name}</div>
        <div>{this.props.points} {this.props.consecutiveDefendCount}</div>
		<div>{this.props.prevRoundDescription}</div>
		{ctrls}
      </div>
      )
    }
    });	
	
    var GamePlayerList = React.createClass({
	  propTypes: {
		data: React.PropTypes.array.isRequired
	  },		
    render: function(){
		var mode = this.props.mode;
		var gameStatus = this.props.gameStatus;
		var gameDbId= this.props.gameDbId;
		var playerNodes = this.props.data.map(function(player){
			return (
			<GamePlayerListEntry mode={mode} gameStatus={gameStatus} gameDbId={gameDbId}  userDbId={player.userDbId} points={player.points} consecutiveDefendCount={player.consecutiveDefendCount} name={player.name} prevRoundDescription={player.prevRoundDescription} />        
		  )
      });
      return (
        <div>
          {playerNodes}
        </div>
      )
    }
    });	
    
    var Game = React.createClass({
	getInitialState: function() {
		return {data : {_id: '', name: '', createdByUser_id:'',startTime:'',gameStatus:'', players:[]}};
	},		
	loadGameFromServer: function() {
		$.ajax({
		  url: "http://localhost:8080/webapp/getGame?gameDbId=" + this.props.gameDbId,
		  dataType: 'json',
		  cache: false,
		  success: function(data) {
			console.log(data);
			this.setState({data: data});
		  }.bind(this),
		  error: function(xhr, status, err) {
			console.error(this.props.url, status, err.toString());
		  }.bind(this)
		});
	},
   sendMove : function(move){
		var move = new Move(this.props.gameDbId, thisUserDbId, move, this.props.userDbId)
		var json = JSON.stringify(move); 
		$.ajax({
			type: "POST",
			data: json,
			url: "http://localhost:8080/webapp/createMove",
			contentType: "application/json; charset=utf-8",
			dataType: 'json',
			cache: false,
			success: function(data) {
				console.log(data);
				}.bind(this),
			error: function(xhr, status, err) {
				console.error(this.props.url, status, err.toString());
				}.bind(this)
		}); 	  
    },	
	componentWillMount: function() {
		this.loadGameFromServer();
		this.timer = setInterval(this.loadGameFromServer, this.props.pollInterval);
	},	
    componentWillUnmount: function () {
        clearInterval(this.timer);
    },
    defend: function () {
		this.sendMove("DEFEND");
    },	
    recharge: function () {
		this.sendMove("RECHARGE");
    },
    universalAttack: function () {
		this.sendMove("UNIVERSAL_ATTACK");
    },	
    render: function(){
		var roundCount;
		if(this.state.data.currentRound != null){
			roundCount = <div>round : {this.state.data.currentRound.roundCount}</div>;
		}
		var ctrls;
		if(this.props.mode == "play" && this.state.data.status == "INPROGRESS"){
			ctrls = <div>
			<a href="#" className="small secondary button" onClick={this.defend}>DEFEND</a>
			<a href="#" className="small secondary button" onClick={this.recharge}>CHUG YOUR BEER</a>
			<a href="#" className="small secondary button" onClick={this.universalAttack}>CALL 911</a>
			</div> 	
		} 	
      return (
        <div>
          <div>_id : {this.state.data._id}</div>
		  <div>name : {this.state.data.name}</div>
          <div>createdByUser : {this.state.data.createdByUser}</div>
		  <div>startTime : {new Date(+this.state.data.startTime).toISOString()}</div>
		  <div>gameStatus : {this.state.data.status}</div>
			{roundCount}		  
			{ctrls}
		  <GamePlayerList data={this.state.data.players} mode={this.props.mode} gameStatus={this.state.data.status} gameDbId={this.props.gameDbId}/>
        </div>
      )
    }
    });      
    
    var GameBox = React.createClass({
    render : function(){
      return (
      <div>
      <MenuBox />
        <Game gameDbId={this.props.gameDbId} mode={this.props.mode} pollInterval={5000}/>
      </div>
      )
    }
    });

    function renderGame(gameDbId, mode_){
    ReactDOM.render(
      <GameBox gameDbId={gameDbId} mode={mode_}/>,
      document.getElementById('content')
      );
    }            

    function renderNewGame(){
    ReactDOM.render(
      <NewGameBox/>,
      document.getElementById('content')
      );
    }    

    function renderGames(){
    ReactDOM.render(
      <GamesBox pollInterval={5000}/>,
      document.getElementById('content')
      );
    }

    function renderUserSetup(){
    ReactDOM.render(
      <UserBox/>,
      document.getElementById('content')
      );
    }

    function start(){
		if(thisUserDbId == null){
			renderUserSetup();
		} else {
			renderGames();
		}
    }
    
    start();
    
