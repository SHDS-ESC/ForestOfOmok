document.addEventListener("DOMContentLoaded", () => {
    connectWebSocket();
    // runApp();
});

function connectWebSocket() {
    var ws = new WebSocket("ws://localhost:8092/forestOfOmok/ws");

    ws.onopen = function() {
        console.log("웹소켓 연결됨");
    };

    ws.onmessage = function(event) {
        console.log("서버 응답: " + event.data);
    };

    ws.onclose = function() {
        console.log("웹소켓 연결 종료");
    };

    ws.onerror = function(error) {
        console.log("에러 발생: " + error);
    };

    // 채팅 보내기
    document.querySelector(".chat-send-btn").onclick = sendChatMsg;
    // 엔터키 이벤트
    document.querySelector(".chat-input").addEventListener("keydown", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            sendChatMsg();
        }
    });

    function sendChatMsg() {
        if (ws.readyState === WebSocket.OPEN) {
            const params = new URLSearchParams(location.search);
            const gameId = params.get('gameId');
            let data = JSON.stringify({ gameId: gameId, message: document.querySelector(".chat-input").value });
            ws.send(data);
            console.log("msg 전송 " + data);
            document.querySelector(".chat-input").value = ""; // 입력창 비우기
        } else {
            console.log("웹소켓이 연결되어 있지 않습니다.");
        }
    }

    function appendChatMessage(msg, isMine) {
	    const chatMessages = document.querySelector('.chat-messages');
	    const div = document.createElement('div');
	    div.className = isMine ? 'chat-message mine' : 'chat-message other';
	    div.textContent = msg;
	    chatMessages.appendChild(div);
	    // 스크롤을 항상 아래로
	    chatMessages.scrollTop = chatMessages.scrollHeight;
	}
}
	
var turn = 0; // turn을 전역으로 선언
var timer = null; // ★ timer를 전역으로 선언

setConf = () => {
    const cellWidth = 35,
        elemInfo = document.getElementById("info"),
        elemTimer = document.getElementById("timer"),
        elemMsg = document.getElementById("msg");

    var globalTime = 0,
        m = 15,
        k = 5,
        boardWidth = m * cellWidth,
        map = [],
        cell;
        // timer는 여기서 선언/초기화하지 않음

    return {
        cellWidth,
        elemInfo,
        elemTimer,
        elemMsg,
        globalTime,
        m,
        k,
        boardWidth,
        map,
        cell
        // timer는 반환하지 않음
    };
};

// turn 변수를 반드시 0으로 초기화(흑부터 시작)
var {
    cellWidth,
    elemInfo,
    elemTimer,
    elemMsg,
    globalTime,
    m,
    k,
    boardWidth,
    map,
    cell
    // timer는 여기서도 받지 않음
} = setConf();

runApp = () => {
    turn = 0; // 반드시 흑(1번)부터 시작
    setBoard();
    setItems();
    setTimer();
    updateCrown(1); // 시작 시 흑 차례 왕관 표시
    // 필요하다면 updateTurnLabel(1); // "내 차례" 레이블도 흑부터
};

const PLAYERS = {
    BLACK: { id: 1, name: "흑", label: "흑 player" },
    WHITE: { id: 2, name: "백", label: "백 player" }
};
// 현재 플레이어를 구하는 함수
// getCurrentPlayer 함수는 turn이 0일 때 흑(1번) 반환
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

    /*
    $.ajax({
	  url: '/forestOfOmok/omokTurn',
	  type: 'POST',
	  contentType: 'application/json; charset=UTF-8',
	  data: JSON.stringify({ gameId: gameId, row: cell.row, col: cell.column}),
	  success: function(response) {
		console.log("서버 응답 : ", response);
		
		
		if (response.result === "invalid") {
			//
		}
		
		
		
	  },
      error: function(xhr, status, error) {
	    console.log(xhr, status, error);
  	  }
	});
	*/
    
    // 현재 플레이어 객체를 가져옴
    const currentPlayer = getCurrentPlayer(turn);
    let forbidden = isForbidden(cell.row, cell.column, currentPlayer.id);
    if (forbidden) {
        showMessage(forbidden);
        return;
    }

    if (map[cell.column][cell.row] === 0) {
        elemTimer.innerHTML = ticksToTime(0);
        clearInterval(timer); // 항상 전역 timer만 clear
        setTimer();

        cell.player = currentPlayer.id;
        map[cell.column][cell.row] = cell.player;

        event.target.setAttribute("class", "fade-in player-" + cell.player);

        const nextPlayer = getCurrentPlayer(turn + 1);

        turn++;
        showMessage(isPlayerWon(cell.player));
        updateCrown(nextPlayer.id);
    }
};

setTimer = () => {
    clearInterval(timer); // 혹시 남아있던 타이머도 정지
    let maxTime = 30;
    const timerBox = document.querySelector('.timer-box');
    const timerText = document.getElementById("timer");
    timerText.innerText = `0:${maxTime < 10 ? '0' : ''}${maxTime}`;
    timer = setInterval(() => {
        maxTime--;
        globalTime++;
        timerText.innerText = `0:${maxTime < 10 ? '0' : ''}${maxTime}`;
        if (maxTime <= 5) {
            timerBox.classList.add('shake');
            timerText.classList.add('red-time');
        } else {
            timerBox.classList.remove('shake');
            timerText.classList.remove('red-time');
        }
        if (maxTime <= 0) {
            clearInterval(timer);
            timerBox.classList.remove('shake');
            timerText.classList.remove('red-time');
            autoPlaceStone();
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
    document.getElementById("resignQuestion").style.display = "block";
    };

document.getElementById("resignYes").onclick = function() {
    document.getElementById("resignQuestion").style.display = "none";
    // 현재 턴의 플레이어가 항복
    const currentPlayer = getCurrentPlayer(turn);
    const winner = currentPlayer.id === PLAYERS.BLACK.id ? PLAYERS.WHITE : PLAYERS.BLACK;
    showMessage("resign", winner);
};


    document.getElementById("resignNo").onclick = function() {
    document.getElementById("resignQuestion").style.display = "none";
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
function showMessage(result, winnerObj) {
    if (result === true) {
        let winner = cell.player === PLAYERS.BLACK.id ? PLAYERS.BLACK.label : PLAYERS.WHITE.label;
        showWinModal(`${winner}가 승리했습니다!`);
    } else if (result === "draw") {
        showWinModal("무승부입니다!");
    } else if (result === "resign" && winnerObj) {
        showWinModal(`${winnerObj.label}가 승리했습니다! (상대 항복)`);
    } else if (typeof result === "string") { // 금수
        showAlertModal(`${result}입니다! 다른 곳에 놓아주세요.`);
    }
}

// 금수 알림 모달 함수
function showAlertModal(msg) {
    const alertModal = document.getElementById('alertModal');
    const alertMsg = document.getElementById('alertMsg');
    const alertCloseBtn = document.getElementById('alertCloseBtn');
    alertMsg.textContent = msg;
    alertModal.style.display = 'block';
    alertCloseBtn.onclick = function() {
        alertModal.style.display = 'none';
    };
}

function updateTimer(timeLeft) {
  const timerBox = document.querySelector('.timer-box');
  const timerText = document.getElementById('timer');
  timerText.innerText = `0:${timeLeft < 10 ? '0' : ''}${timeLeft}`;
  if (timeLeft <= 5) {
    timerBox.classList.add('shake');
    timerText.classList.add('red-time');
  } else {
    timerBox.classList.remove('shake');
    timerText.classList.remove('red-time');
  }
}

$(function () {
  // modal.html 불러오기
  $("#modalComponent").load("modal.html");
});

// 메뉴 버튼 클릭 시 드롭다운 토글
document.getElementById("menuBtn").onclick = function(e) {
  e.stopPropagation();
  const menu = document.getElementById("menuDropdown");
  menu.style.display = menu.style.display === "none" ? "block" : "none";
};
// 메뉴 외부 클릭 시 닫기
document.addEventListener("click", function(e) {
  document.getElementById("menuDropdown").style.display = "none";
});
// 메뉴 내부 클릭 시 닫히지 않게
document.getElementById("menuDropdown").onclick = function(e) {
  e.stopPropagation();
};
// 항복 버튼 기존 로직 연결
document.getElementById("resignBtn").onclick = function() {
  document.getElementById("resignQuestion").style.display = "block";
  document.getElementById("menuDropdown").style.display = "none";
};
// 무르기 버튼(undoBtn) 클릭 이벤트(구현 필요)
document.getElementById("undoBtn").onclick = function() {
  document.getElementById("undoRequestModal").style.display = "block";
  document.getElementById("menuDropdown").style.display = "none";
};

document.getElementById("undoRequestYes").onclick = function() {
  document.getElementById("undoRequestModal").style.display = "none";
  // 대기 모달 표시
  const waitingModal = document.getElementById("undoWaitingModal");
  const waitingMsg = document.getElementById("undoWaitingMsg");
  const timerElem = document.getElementById("undoWaitingTimer");
  const closeBtn = document.getElementById("undoWaitingCloseBtn");
  waitingModal.style.display = "block";
  waitingMsg.innerHTML = "상대방이 무르기 요청을<br />수락할 때까지 대기중...";
  timerElem.style.display = "";
  closeBtn.style.display = "none";

  let waitTime = 8;
  timerElem.textContent = waitTime;
  let waitInterval = setInterval(() => {
    waitTime--;
    timerElem.textContent = waitTime;
    if (waitTime <= 0) {
      clearInterval(waitInterval);
      timerElem.style.display = "none";
      waitingMsg.innerHTML = "상대방이 무르기 요청에<br>응답하지 않았습니다.";
      closeBtn.style.display = "";
    }
  }, 1000);

  // 닫기 버튼 이벤트
  closeBtn.onclick = function() {
    waitingModal.style.display = "none";
  };
};

document.getElementById("undoRequestNo").onclick = function() {
  document.getElementById("undoRequestModal").style.display = "none";
};

function updateCrown(currentPlayerId) {
  const crown1 = document.getElementById('player1-crown');
  const crown2 = document.getElementById('player2-crown');
  if (currentPlayerId === 1) {
    crown1.style.display = 'block';
    crown2.style.display = 'none';
  } else {
    crown1.style.display = 'none';
    crown2.style.display = 'block';
  }
}

// 턴이 바뀔 때마다 호출
// 예시: updateCrown(1); // 1번 플레이어 차례
// 예시: updateCrown(2); // 2번 플레이어 차례

function showWinModal(msg) {
    const winModal = document.getElementById('winModal');
    const winModalMsg = document.getElementById('winModalMsg');
    const rematchBtn = document.getElementById('rematchBtn');
    const exitBtn = document.getElementById('exitBtn');
    const rematchInfo = document.getElementById('rematchInfo');

    winModalMsg.textContent = msg;
    winModal.style.display = 'block';
    rematchBtn.style.display = 'inline-block';
    exitBtn.style.display = 'inline-block';
    rematchInfo.style.display = 'none';

    rematchBtn.onclick = function () {
        winModal.style.display = 'none';
        rematchBtn.style.display = 'none';
        exitBtn.style.display = 'none';

        // 이긴 사람이 흑돌(선공)을 갖도록
        let winnerId = cell.player; // cell.player가 이긴 사람
        turn = 0; // 선공(흑)부터 시작

        // playerInfo 돌 색상만 바꿔주기 (자리 바꾸지 않음)
        const leftStone = document.querySelector('.player-info.player-left .player-stone');
        const rightStone = document.querySelector('.player-info.player-right .player-stone');
        if (winnerId === 1) {
            leftStone.className = 'player-stone player-stone-black';
            rightStone.className = 'player-stone player-stone-white';
        } else {
            leftStone.className = 'player-stone player-stone-white';
            rightStone.className = 'player-stone player-stone-black';
        }

        // 기존 바둑판, 돌, map 초기화
        const items = document.getElementById('items');
        if (items) items.remove();
        const canvas = document.querySelector('canvas');
        if (canvas) canvas.remove();
        map = [];
        setBoard();
        setItems();
        setTimer();
        updateCrown(1); 
    };

    exitBtn.onclick = function () {
        location.reload();
    };
}

// 레디 상태 변수
let ready1 = false, ready2 = false;

// 레디 버튼 이벤트
document.getElementById("readyBtn1").onclick = function() {
    ready1 = true;
    document.getElementById("readyBtn1").disabled = true;
    updateReadyStatus();
    checkReady();
};
document.getElementById("readyBtn2").onclick = function() {
    ready2 = true;
    document.getElementById("readyBtn2").disabled = true;
    updateReadyStatus();
    checkReady();
};

function updateReadyStatus() {
    let msg = "";
    msg += ready1 ? "1P 준비 완료! " : "1P 대기중... ";
    msg += ready2 ? "2P 준비 완료!" : "2P 대기중...";
    document.getElementById("readyStatus").textContent = msg;
}

function checkReady() {
    if (ready1 && ready2) {
        hideReadyModal();
        runApp();
    }
}

function showReadyModal() {
    document.getElementById("readyModal").style.display = "block";
    // 게임 UI, 채팅, 메뉴 등 숨기기
    document.querySelectorAll(
      ".container.game, #chatbox, .timer-wrap, #menuBtn, #menuDropdown"
    ).forEach(el => el.style.display = "none");
    // 배경만 남음
}

function hideReadyModal() {
    document.getElementById("readyModal").style.display = "none";
    // 게임 UI, 채팅, 메뉴 등 다시 보이기
    document.querySelectorAll(
      ".container.game, #chatbox, .timer-wrap, #menuBtn, #menuDropdown"
    ).forEach(el => el.style.display = "");
}

// 페이지 로드시 레디 모달 띄우기
window.onload = function() {
    showReadyModal();
};