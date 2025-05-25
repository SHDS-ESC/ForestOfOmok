document.addEventListener("DOMContentLoaded", () => {
    runApp();
    });

    setConf = () => {
    const cellWidth = 35,
        elemInfo = document.getElementById("info"),
        elemTimer = document.getElementById("timer"),
        elemMsg = document.getElementById("msg");

    var timer,
        globalTime = 0,
        m = 15,
        k = 5,
        boardWidth = m * cellWidth,
        map = [],
        cell,
        turn = 0;

    return {
        cellWidth,
        elemInfo,
        elemTimer,
        elemMsg,
        timer,
        globalTime,
        m,
        k,
        boardWidth,
        map,
        cell,
        turn
    };
    };

    var {
    cellWidth,
    elemInfo,
    elemTimer,
    elemMsg,
    timer,
    globalTime,
    m,
    k,
    boardWidth,
    map,
    cell,
    turn
    } = setConf();

    runApp = () => {
    setBoard();
    setItems();
    setTimer();
    };

    const PLAYERS = {
    BLACK: { id: 1, name: "흑", label: "1st player" },
    WHITE: { id: 2, name: "백", label: "2nd player" }
    };

    // 현재 플레이어를 구하는 함수
    function getCurrentPlayer(turn) {
    return turn % 2 === 0 ? PLAYERS.BLACK : PLAYERS.WHITE;
    }

function isForbidden(row, column, player) { // 함수명 변경
    // (1번 플레이어)만 금수 적용
    if (player === PLAYERS.BLACK.id) {
        // 3-3 금수: 열린 3이 2개 이상
        if (countOpenN(row, column, player, 3) >= 2) return "3-3 금수";
        // 4-4 금수: 열린 4가 2개 이상
        if (countOpenN(row, column, player, 4) >= 2) return "4-4 금수";
        // 6목(장목) 금수: 6목 이상이면 금수
        if (isOverline(row, column, player)) return "6목 금수";
    }
    return false;
}

    // EX)
    // 3-3 체크: countOpenN(row, column, player, 3)
    // 4-4 체크: countOpenN(row, column, player, 4)'
const EMPTY = 0;
function countOpenN(row, column, player, n) {
    let count = 0;
    const directions = [
        [1, 0], [0, 1], [1, 1], [1, -1]
    ];
    for (let [dx, dy] of directions) {
        let line = '';
        for (let d = -4; d <= 4; d++) {
            let x = column + dx * d, y = row + dy * d;
            if (d === 0) line += player;
            else if (map[x] && (map[x][y] === player)) line += player;
            else if (map[x] && (map[x][y] === EMPTY)) line += 'E'; // 빈 칸은 'E'로 표시
            else line += 'x';
        }
        if (line.includes('E' + player.toString().repeat(n) + 'E')) count++;
    }
    return count;
}

    // 6목(장목) 체크 함수
    function isOverline(row, column, player) {
    const directions = [
        [1, 0], [0, 1], [1, 1], [1, -1]
    ];
    for (let [dx, dy] of directions) {
        let count = 1;
        for (let d = 1; d < 6; d++) {
        let x = column + dx * d, y = row + dy * d;
        if (map[x] && (map[x][y] === player)) count++;
        else break;
        }
        for (let d = 1; d < 6; d++) {
        let x = column - dx * d, y = row - dy * d;
        if (map[x] && (map[x][y] === player)) count++;
        else break;
        }
        if (count >= 6) return true;
    }
    return false;
    }

    // 장목(6목 이상)은 흑만 무효, 백은 6목 이상도 승리로 인정
    isPlayerWon = () => {
    // 각 방향별로 연속된 돌 개수 세는 함수
    const countStones = (dx, dy) => {
        let cnt = 1;
        let r = cell.row, c = cell.column;
        // 반대 방향
        let nr = r - dy, nc = c - dx;
        while (map[nc] && (map[nc][nr] === cell.player)) {
        cnt++;
        nr -= dy;
        nc -= dx;
        }
        // 정 방향
        nr = r + dy; nc = c + dx;
        while (map[nc] && (map[nc][nr] === cell.player)) {
        cnt++;
        nr += dy;
        nc += dx;
        }
        return cnt;
    };

    // 네 방향 검사
    const directions = [
        [1, 0],  // 가로
        [0, 1],  // 세로
        [1, 1],  // 대각 ↘
        [1, -1], // 대각 ↗
    ];

    let won = false;
    for (let [dx, dy] of directions) {
        let cnt = countStones(dx, dy);
        if (cell.player === PLAYERS.BLACK.id) {
        // 흑: 5개만 승리, 6목 이상은 무효
        if (cnt === k) won = true;
        } else if (cell.player === PLAYERS.WHITE.id) {
        // 백: 5개 이상이면 모두 승리
        if (cnt >= k) won = true;
        }
    }

    return (turn === m * m) && !won ? "draw" : won;
    };

    setBoard = () => {
    const elementBoard = document.createElement("canvas"),
        elementContext = elementBoard.getContext("2d");
    elementBoard.setAttribute("width", boardWidth);
    elementBoard.setAttribute("height", boardWidth);
    document.body.appendChild(elementBoard);

    elementContext.beginPath();
    elementContext.lineWidth = "1";
    elementContext.strokeStyle = "#6d4c36";

    for (let i = 0; i < m; i++) {
        elementContext.moveTo(cellWidth * i + cellWidth / 2, cellWidth / 2);
        elementContext.lineTo(
        cellWidth * i + cellWidth / 2,
        boardWidth - cellWidth / 2
        );
        elementContext.moveTo(cellWidth / 2, cellWidth * i + cellWidth / 2);
        elementContext.lineTo(
        boardWidth - cellWidth / 2,
        cellWidth * i + cellWidth / 2
        );
    }

    elementContext.stroke();
    };

    setItems = () => {
    const elementItems = document.createElement("div");
    elementItems.setAttribute("id", "items");
    document.body.appendChild(elementItems);

    for (let row = 0; row < m; row++) {
        let elementRow = document.createElement("div");
        elementItems.appendChild(elementRow);
        map[row] = [];
        for (let column = 0; column < m; column++) {
        map[row][column] = 0;
        let elementItem = document.createElement("div");
        elementItem.cellProperties = {
            row: row,
            column: column,
            player: 0
        };
        elementRow.appendChild(elementItem);
        elementItem.addEventListener("click", isItemClicked);
        }
    }
};

 isItemClicked = (event) => {
    cell = event.target.cellProperties;
    
	$.ajax({
	  url: '/forestOfOmok/omokTurn',
	  type: 'POST',
	  contentType: 'application/json; charset=UTF-8',
	  data: JSON.stringify({ row: cell.row, col: cell.column}),
	  success: function(response) {
		console.log("서버 응답 : ", response);
		alert("서버 메세지 : " + response.msg);
	  },
      error: function(xhr, status, error) {
	    console.log(xhr, status, error);
  	  }
	});
    
    // 현재 플레이어 객체를 가져옴
    const currentPlayer = getCurrentPlayer(turn);
    // 금수 체크
    let forbidden = isForbidden(cell.row, cell.column, currentPlayer.id);
    if (forbidden) {
        showMessage(forbidden); // 금수 메시지도 showMessage로 통일
        return;
    }

    if (map[cell.column][cell.row] === 0) {
        elemTimer.innerHTML = ticksToTime(0);
        clearInterval(timer);
        setTimer();

        cell.player = currentPlayer.id;
        map[cell.column][cell.row] = cell.player;

        event.target.setAttribute("class", "fade-in player-" + cell.player);

        // 다음 플레이어 차례 표시
        const nextPlayer = getCurrentPlayer(turn + 1);
        elemInfo.innerHTML = `${nextPlayer.label}'s turn`;

        turn++;
        showMessage(isPlayerWon(cell.player));
    }
};

    setTimer = () => {
    let maxTime = 30; // 30초부터 0초로 떨어지도록 변경 
    document.getElementById("timer").innerText = `0:${maxTime < 10 ? '0' : ''}${maxTime}`;
    timer = setInterval(() => {
        maxTime--;
        globalTime++;
        document.getElementById("timer").innerText = `0:${maxTime < 10 ? '0' : ''}${maxTime}`;
        if (maxTime <= 0) {
        clearInterval(timer);
        autoPlaceStone(); // 0초가 되면 autoPlaceStone()함수로로 돌을 랜덤한 위치에 착수시킴
        }
    }, 1000);
    };

    // 제한시간 30초가 지나면 랜덤한 빈 칸에 돌을 놓는 함수
    function autoPlaceStone() {
    // 빈 칸 목록 만들기
    let emptyCells = [];
    for (let row = 0; row < m; row++) {
        for (let col = 0; col < m; col++) {
        if (map[col][row] === 0) {
            emptyCells.push({ row: row, column: col });
        }
        }
    }
    if (emptyCells.length === 0) return; // 빈 칸 없으면 종료

    // 랜덤한 빈 칸 선택
    let idx = Math.floor(Math.random() * emptyCells.length);
    let cellInfo = emptyCells[idx];

    // 해당 칸의 DOM 요소 찾기
    let itemRows = document.querySelectorAll("#items > div");
    let item = itemRows[cellInfo.row].children[cellInfo.column];

    // 클릭 이벤트 강제 실행
    item.click();
    }

    document.getElementById("resignBtn").onclick = function() {
    document.getElementById("resignModal").style.display = "block";
    };

document.getElementById("resignYes").onclick = function() {
    document.getElementById("resignModal").style.display = "none";
    // 현재 턴의 플레이어가 항복
    const currentPlayer = getCurrentPlayer(turn);
    const winner = currentPlayer.id === PLAYERS.BLACK.id ? PLAYERS.WHITE : PLAYERS.BLACK;
    showMessage("resign", winner);
};


    document.getElementById("resignNo").onclick = function() {
    document.getElementById("resignModal").style.display = "none";
    };

    ticksToTime = (ticks) => {
    let minutes = (seconds = 0);
    minutes = Math.floor(ticks / 60);
    seconds = ticks - minutes * 60;
    return minutes + "m : " + seconds + "s";
    };

    function resetGame() {
    // 페이지 새로고침으로 간단하게 리셋
    location.reload(); 
    }

    // 5목 승리 시 자동 리셋 (showMessage 함수 또는 isPlayerWon 호출 후)
function showMessage(result, winnerObj) { // 승리
    if (result === true) {
        let winner = cell.player === PLAYERS.BLACK.id ? PLAYERS.BLACK.label : PLAYERS.WHITE.label;
        elemMsg.innerHTML = `<div>${winner}가 승리했습니다! [5초 후 다시 시작]</div>`;
        elemMsg.setAttribute("class", "show fade-in");
        document.querySelectorAll("#items div div").forEach(el => {
            el.onclick = null;
        });
        clearInterval(timer);
        setTimeout(resetGame, 5000);
    } else if (result === "draw") { // 무승부
        elemMsg.innerHTML = `<div>무승부입니다!</div>`;
        elemMsg.setAttribute("class", "show fade-in");
        document.querySelectorAll("#items div div").forEach(el => {
            el.onclick = null;
        });
        clearInterval(timer);
        setTimeout(resetGame, 5000);
    } else if (result === "resign" && winnerObj) { // 항복
        elemMsg.innerHTML = `<div>${winnerObj.label}가 승리했습니다!(상대 항복) [5초 후 다시 시작]</div>`;
        elemMsg.setAttribute("class", "show fade-in");
        document.querySelectorAll("#items div div").forEach(el => {
            el.onclick = null;
        });
        clearInterval(timer);
        setTimeout(resetGame, 5000);
    } else if (typeof result === "string") { // 금수
        elemMsg.innerHTML = `<div>${result}입니다! 다른 곳에 놓아주세요.</div>`;
        elemMsg.setAttribute("class", "show fade-in");
        setTimeout(() => {
            elemMsg.innerHTML = "";
            elemMsg.removeAttribute("class");
        }, 1500);
    }
}