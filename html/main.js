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
//   setItems();
//   setTimer();
};

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

    return items >= k ? true : false;
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

    return items >= k ? true : false;
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

    return items >= k ? true : false;
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

    return items >= k ? true : false;
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

isItemClicked = (event) => {
  cell = event.target.cellProperties;

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
