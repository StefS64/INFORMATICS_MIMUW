interface Dot {
  row: number;
  col: number;
  color: string;
}

interface BoardConfig {
  name: string;
  rows: number;
  cols: number;
  dots: Dot[];
}

interface Window {
  initialBoard?: {
    id?: number;
    name: string;
    rows: number;
    cols: number;
    dots: Dot[];
  };
}

let boardConfig: BoardConfig = {
  name: "",
  rows: 0,
  cols: 0,
  dots: [],
};

let currentColor: string = "#ff0000";
let pendingDotColor: string | null = null;
let pendingDotCount = 0;
let deletePairMode = false;

function createGrid(rows: number, cols: number) {
  const container = document.getElementById("board-container");
  if (!container) return;
  container.innerHTML = "";
  const table = document.createElement("table");
  table.style.borderCollapse = "collapse";
  for (let r = 0; r < rows; r++) {
    const tr = document.createElement("tr");
    for (let c = 0; c < cols; c++) {
      const td = document.createElement("td");
      td.style.width = "32px";
      td.style.height = "32px";
      td.style.border = "1px solid #aaa";
      td.style.textAlign = "center";
      td.dataset.row = r.toString();
      td.dataset.col = c.toString();
      td.addEventListener("click", () => handleCellClick(r, c, td));
      tr.appendChild(td);
    }
    table.appendChild(tr);
  }
  container.appendChild(table);
  renderDots();
}

function handleCellClick(row: number, col: number, cell: HTMLTableCellElement) {
  if (deletePairMode) {
    // Find the dot at this cell
    const dot = boardConfig.dots.find(dot => dot.row === row && dot.col === col);
    if (dot) {
      // Remove both dots of this color
      boardConfig.dots = boardConfig.dots.filter(d => d.color.trim().toLowerCase() !== dot.color.trim().toLowerCase());
      renderDots();
      saveBoardState();
      deletePairMode = false;
      const btn = document.getElementById("delete-dot-pair") as HTMLButtonElement;
      btn.style.background = "";
      btn.textContent = "Usuń parę kropek";
    }
    return;
  }
  if (pendingDotColor === null) return;
  if (boardConfig.dots.some(dot => dot.row === row && dot.col === col)) return;
  const color = pendingDotColor.trim().toLowerCase();
  const colorCount = boardConfig.dots.filter(dot => dot.color.trim().toLowerCase() === color).length;
  if (colorCount >= 2) {
    alert("Para tego koloru już istnieje. Wybierz inny kolor.");
    pendingDotColor = null;
    pendingDotCount = 0;
    return;
  }
  boardConfig.dots.push({ row, col, color });
  pendingDotCount++;
  renderDots();
  saveBoardState();
  if (pendingDotCount === 2) {
    pendingDotColor = null;
    pendingDotCount = 0;
    (document.getElementById("add-dot-pair") as HTMLButtonElement).disabled = false;
    (document.getElementById("dot-color") as HTMLInputElement).disabled = false;
  }
}

function renderDots() {
  const container = document.getElementById("board-container");
  if (!container) return;
  const tds = container.getElementsByTagName("td");
  for (let td of Array.from(tds)) {
    td.innerHTML = "";
    td.style.background = "";
    const row = parseInt(td.dataset.row || "0");
    const col = parseInt(td.dataset.col || "0");
    const dot = boardConfig.dots.find(d => d.row === row && d.col === col);
    if (dot) {
      const circle = document.createElement("div");
      circle.style.width = "18px";
      circle.style.height = "18px";
      circle.style.margin = "auto";
      circle.style.borderRadius = "50%";
      circle.style.background = dot.color;
      circle.style.border = "2px solid #fff";
      td.appendChild(circle);
    }
  }
}

function saveBoardState() {
  boardConfig.name = (document.getElementById("board-name") as HTMLInputElement).value;
  const id = (window.initialBoard && window.initialBoard.id) ? window.initialBoard.id : undefined;
  const payload = { ...boardConfig, id };
  fetch("/board/save/", {
      method: "POST",
      headers: {
          "Content-Type": "application/json",
          "X-CSRFToken": (document.querySelector('[name=csrfmiddlewaretoken]') as HTMLInputElement)?.value || "",
      },
      body: JSON.stringify(payload),
  })
  .then(res => res.json())
  .then(data => {
    if (data.success) {
        if (!window.initialBoard) {
            window.initialBoard = {
                id: data.board_id,
                name: boardConfig.name,
                rows: boardConfig.rows,
                cols: boardConfig.cols,
                dots: boardConfig.dots,
            };
        } else {
            window.initialBoard.id = data.board_id;
        }
    }
  });
}

function setup() {
  document.getElementById("generate-board")?.addEventListener("click", () => {
    const name = (document.getElementById("board-name") as HTMLInputElement).value;
    const rows = parseInt((document.getElementById("board-rows") as HTMLInputElement).value);
    const cols = parseInt((document.getElementById("board-cols") as HTMLInputElement).value);

    // If dimensions changed, clear dots, else keep them
    if (boardConfig.rows !== rows || boardConfig.cols !== cols) {
      boardConfig = {
        name,
        rows,
        cols,
        dots: boardConfig.dots.filter(dot => dot.row < rows && dot.col < cols),
      };
    } else {
      boardConfig.name = name;
    }
    createGrid(rows, cols);
    saveBoardState(); // <-- automatyczny zapis
  });

  document.getElementById("dot-color")?.addEventListener("input", (e) => {
    currentColor = (e.target as HTMLInputElement).value;
  });

  document.getElementById("add-dot-pair")?.addEventListener("click", () => {
    let color = (document.getElementById("dot-color") as HTMLInputElement).value.trim().toLowerCase();
    // Count how many pairs of each color exist
    const colorCounts: Record<string, number> = {};
    for (const dot of boardConfig.dots) {
        const c = dot.color.trim().toLowerCase();
        colorCounts[c] = (colorCounts[c] || 0) + 1;
    }
    // If this color already has a pair, block it
    if (colorCounts[color] === 2) {
        alert("Para tego koloru już istnieje. Wybierz inny kolor.");
        return;
    }
    pendingDotColor = color;
    pendingDotCount = colorCounts[color] || 0;
  });

  document.getElementById("save-board")?.addEventListener("click", () => {
    saveBoardState();
  });

  document.getElementById("delete-dot-pair")?.addEventListener("click", () => {
    deletePairMode = !deletePairMode;
    const btn = document.getElementById("delete-dot-pair") as HTMLButtonElement;
    btn.style.background = deletePairMode ? "#faa" : "";
    btn.textContent = deletePairMode ? "Kliknij kropkę do usunięcia pary" : "Usuń parę kropek";
  });
}

document.addEventListener("DOMContentLoaded", () => {
  if (window.initialBoard) {
    boardConfig = {
      name: window.initialBoard.name,
      rows: window.initialBoard.rows,
      cols: window.initialBoard.cols,
      dots: window.initialBoard.dots,
    };
    (document.getElementById("board-name") as HTMLInputElement).value = boardConfig.name;
    (document.getElementById("board-rows") as HTMLInputElement).value = boardConfig.rows.toString();
    (document.getElementById("board-cols") as HTMLInputElement).value = boardConfig.cols.toString();
    createGrid(boardConfig.rows, boardConfig.cols);
  }
  setup();
});