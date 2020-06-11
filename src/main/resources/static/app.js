var stompClient = null;
var gameId = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/chess-game-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/game/init', function (cg) {
            gameId = JSON.parse(cg.body).payload.id;
            showGreeting(gameId);
            stompClient.subscribe('/game/' + gameId, chessCallback);
        });

    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function initGame() {
    stompClient.send("/app/game/init", {}, JSON.stringify({'name': $("#player1").val()}));
}

function joinGame() {
    gameId = $("#gameId").val();
    console.log("sending to " + gameId)
    stompClient.send("/app/game/join/" + gameId, {}, JSON.stringify({'name': $("#player2").val()}));
    stompClient.subscribe('/game/' + gameId, chessCallback);
}

function chessCallback(chessResponse) {
    let retObj = JSON.parse(chessResponse.body);
    if (retObj.success) {
        console.log("received：" + JSON.stringify(retObj.payload.activePlayer));
    } else {
        console.log("received: " + retObj.message);
    }
}

function playGame() {
    stompClient.send("/app/game/action/" + gameId, {}, JSON.stringify({
        'player': {'name': $("#player3").val()},
        'position': {'row': $("#row").val(), 'col': $("#col").val()}
    }));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send1").click(function () {
        initGame();
    });
    $("#send2").click(function () {
        joinGame();
    });
    $("#send3").click(function () {
        playGame();
    });
});

