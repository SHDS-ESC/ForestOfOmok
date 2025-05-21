document.addEventListener("DOMContentLoaded", () => {
  runApp();
});

setConf = () => {
  const cellWidth = 35,
    elemInfo = document.getElementById("info"),
    elemTimer = document.getElementById("timer"),
    elemMsg = document.getElementById("msg");

  var timer,
    globalTicks = 0,
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
    globalTicks,
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
  globalTicks,
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

// 3-3 금수 체크 함수 (흑백 모두 적용)
function isForbiddenMove(row, column, player) {
  // 1번 플레이어(흑)만 금수 적용
  if (player === 1) {
    // 3-3 금수: 열린 3이 2개 이상
    if (countOpenThrees(row, column, player) >= 2) return "3-3 금수";
    // 4-4 금수: 열린 4가 2개 이상
    if (countOpenFours(row, column, player) >= 2) return "4-4 금수";
    // 6목(장목) 금수: 6목 이상이면 금수
    if (isOverline(row, column, player)) return "6목 금수";
  }
  return false;
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
      if (map[x] && map[x][y] === player) count++;
      else break;
    }
    for (let d = 1; d < 6; d++) {
      let x = column - dx * d, y = row - dy * d;
      if (map[x] && map[x][y] === player) count++;
      else break;
    }
    if (count >= 6) return true;
  }
  return false;
}

// 장목(6목 이상)은 승리로 인정하지 않음
isPlayerWon = () => {
  one = () => {
    items = 1;
    row = cell.row;
    while (map[cell.column][row] === cell.player && row > 0) {
      row--;
      if (map[cell.column][row] === cell.player) items++;
    }
    row = cell.row;
    while (map[cell.column][row] === cell.player && row < m - 1) {
      row++;
      if (map[cell.column][row] === cell.player) items++;
    }
    // 5목만 승리, 6목 이상은 무효
    return items === k ? true : false;x
  };

  two = () => {
    items = 1;
    column = cell.column;
    while (map[column][cell.row] === cell.player && column > 0) {
      column--;
      if (map[column][cell.row] === cell.player) items++;
    }
    column = cell.column;
    while (map[column][cell.row] === cell.player && column < m - 1) {
      column++;
      if (map[column][cell.row] === cell.player) items++;
    }
    return items === k ? true : false;
  };

  three = () => {
    items = 1;
    row = cell.row;
    column = cell.column;
    while (map[column][row] === cell.player && row > 0 && column > 0) {
      row++;
      column--;
      if (map[column][row] === cell.player) items++;
    }
    row = cell.row;
    column = cell.column;
    while (map[column][row] === cell.player && row < m - 1 && column < m - 1) {
      row--;
      column++;
      if (map[column][row] === cell.player) items++;
    }
    return items === k ? true : false;
  };

  four = () => {
    items = 1;
    row = cell.row;
    column = cell.column;
    while (map[column][row] === cell.player && row > 0 && column > 0) {
      row--;
      column--;
      if (map[column][row] === cell.player) items++;
    }
    row = cell.row;
    column = cell.column;
    while (map[column][row] === cell.player && row < m - 1 && column < m - 1) {
      row++;
      column++;
      if (map[column][row] === cell.player) items++;
    }
    return items === k ? true : false;
  };

  let row,
    column,
    items,
    won = one() || two() || three() || four();

  return turn === m * m && won === false ? "draw" : won;
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

// 열린 3 체크
function countOpenThrees(row, column, player) {
  // 간단한 예시: 실제 오목 규칙에 맞게 더 정교하게 구현 필요
  // 여기서는 3개 연속 + 양쪽이 비어있는 경우를 센다
  let count = 0;
  const directions = [
    [1, 0], [0, 1], [1, 1], [1, -1]
  ];
  for (let [dx, dy] of directions) {
    let line = '';
    for (let d = -4; d <= 4; d++) {
      let x = column + dx * d, y = row + dy * d;
      if (d === 0) line += player;
      else if (map[x] && map[x][y] === player) line += player;
      else if (map[x] && map[x][y] === 0) line += '0';
      else line += 'x';
    }
    if (line.includes('0' + player + player + player + '0')) count++;
  }
  return count;
}

// 열린 4 체크
function countOpenFours(row, column, player) {
  let count = 0;
  const directions = [
    [1, 0], [0, 1], [1, 1], [1, -1]
  ];
  for (let [dx, dy] of directions) {
    let line = '';
    for (let d = -4; d <= 4; d++) {
      let x = column + dx * d, y = row + dy * d;
      if (d === 0) line += player;
      else if (map[x] && map[x][y] === player) line += player;
      else if (map[x] && map[x][y] === 0) line += '0';
      else line += 'x';
    }
    if (line.includes('0' + player + player + player + player + '0')) count++;
  }
  return count;
}

isItemClicked = (event) => {
  cell = event.target.cellProperties;
  // 금수 체크
  let forbidden = isForbiddenMove(cell.row, cell.column, turn % 2 === 0 ? 1 : 2);
  if (forbidden) {
    elemMsg.innerHTML = "<div>" + forbidden + "입니다! 다른 곳에 놓아주세요.</div>";
    elemMsg.setAttribute("class", "show fade-in");
    // 1.5초 후 메시지 자동 숨김
    setTimeout(() => {
      elemMsg.innerHTML = "";
      elemMsg.removeAttribute("class");
    }, 1500);
    return;
  }

  if (map[cell.column][cell.row] === 0) {
    elemTimer.innerHTML = ticksToTime(0);
    clearInterval(timer);
    setTimer();

    cell.player = turn++ % 2 === 0 ? 1 : 2;
    map[cell.column][cell.row] = cell.player;

    event.target.setAttribute("class", "fade-in player-" + cell.player);

    elemInfo.innerHTML =
      cell.player === 2 ? "1st player's turn" : "2nd player's turn";

    showMessage(isPlayerWon(cell.player));
  }
};

setTimer = () => {
  let ticks = 0;
  timer = setInterval(() => {
    ticks++;
    globalTicks++;
    elemTimer.innerHTML = ticksToTime(ticks);
  }, 1000);
};

ticksToTime = (ticks) => {
  let minutes = (seconds = 0);
  minutes = Math.floor(ticks / 60);
  seconds = ticks - minutes * 60;
  return minutes + "m : " + seconds + "s";
};

showMessage = (status) => {
  if (status === true) {
    elemMsg.innerHTML =
      "<div>Player " +
      cell.player +
      " won! (" +
      ticksToTime(globalTicks) +
      ")</div>";
    elemMsg.setAttribute("class", "show fade-in");
    elemInfo.innerHTML = elemTimer.innerHTML = "";
    clearInterval(timer);
  } else if (status === "draw") {
    elemMsg.innerHTML = "<div>Draw! (" + ticksToTime(globalTicks) + ")</div>";
    elemMsg.setAttribute("class", "show fade-in");
    elemInfo.innerHTML = elemTimer.innerHTML = "";
    clearInterval(timer);
  }
};
