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
        loadChessboard(retObj.payload.chessboard);
        updatePlayerInfo(retObj.payload.activePlayer);
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

function loadChessboard(chessboard) {
    let table = $('#chessboard');
    table.find("tbody tr").remove();
    for (let i = 0; i < chessboard.grid.length; i++) {
        let fields = "";
        for (let j = 0; j < chessboard.grid[i].length; j++) {
            let piece = chessboard.grid[i][j];
            let pos = "pos_" + i + "_" + j;
            if (piece) {
                let cls = piece.color == 'Red' ? 'text-danger' : 'text-nothing';
                let name = piece.hidden ? '&nbsp;&nbsp;' : piece.name
                fields += "<td id='" + pos + "' class='bg-info font-weight-bold " + cls + "'>" + name + "</td>"
            } else {
                fields += "<td id='" + pos + "'></td>"
            }
        }
        table.append("<tr>" + fields + "</tr>");
    }
}

function updatePlayerInfo(player) {
    if (player) {
        let color = player.color ? (player.color == 'Red' ? "红方" : "黑方") : "颜色未定";
        $("#activePlayer").html("等待<b>" + player.name + "（" + color + "）</b>走子...")
    } else {
        $("#activePlayer").empty();
    }
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
    $("td").mouseover(function () {
        console.log("clicked: " + this.id);
    });
});

