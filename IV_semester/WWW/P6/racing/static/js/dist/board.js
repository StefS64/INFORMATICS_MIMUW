"use strict";
let boardConfig = {
    name: "",
    rows: 0,
    cols: 0,
    dots: [],
};
let currentColor = "#ff0000";
let pendingDotColor = null;
let pendingDotCount = 0;
let deletePairMode = false;
function createGrid(rows, cols) {
    const container = document.getElementById("board-container");
    if (!container)
        return;
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
function handleCellClick(row, col, cell) {
    if (deletePairMode) {
        // Find the dot at this cell
        const dot = boardConfig.dots.find(dot => dot.row === row && dot.col === col);
        if (dot) {
            // Remove both dots of this color
            boardConfig.dots = boardConfig.dots.filter(d => d.color.trim().toLowerCase() !== dot.color.trim().toLowerCase());
            renderDots();
            saveBoardState();
            deletePairMode = false;
            const btn = document.getElementById("delete-dot-pair");
            btn.style.background = "";
            btn.textContent = "Usuń parę kropek";
        }
        return;
    }
    if (pendingDotColor === null)
        return;
    if (boardConfig.dots.some(dot => dot.row === row && dot.col === col))
        return;
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
        document.getElementById("add-dot-pair").disabled = false;
        document.getElementById("dot-color").disabled = false;
    }
}
function renderDots() {
    const container = document.getElementById("board-container");
    if (!container)
        return;
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
    var _a;
    boardConfig.name = document.getElementById("board-name").value;
    const id = (window.initialBoard && window.initialBoard.id) ? window.initialBoard.id : undefined;
    const payload = Object.assign(Object.assign({}, boardConfig), { id });
    fetch("/board/save/", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-CSRFToken": ((_a = document.querySelector('[name=csrfmiddlewaretoken]')) === null || _a === void 0 ? void 0 : _a.value) || "",
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
            }
            else {
                window.initialBoard.id = data.board_id;
            }
        }
    });
}
function setup() {
    var _a, _b, _c, _d, _e;
    (_a = document.getElementById("generate-board")) === null || _a === void 0 ? void 0 : _a.addEventListener("click", () => {
        const name = document.getElementById("board-name").value;
        const rows = parseInt(document.getElementById("board-rows").value);
        const cols = parseInt(document.getElementById("board-cols").value);
        // If dimensions changed, clear dots, else keep them
        if (boardConfig.rows !== rows || boardConfig.cols !== cols) {
            boardConfig = {
                name,
                rows,
                cols,
                dots: boardConfig.dots.filter(dot => dot.row < rows && dot.col < cols),
            };
        }
        else {
            boardConfig.name = name;
        }
        createGrid(rows, cols);
        saveBoardState(); // <-- automatyczny zapis
    });
    (_b = document.getElementById("dot-color")) === null || _b === void 0 ? void 0 : _b.addEventListener("input", (e) => {
        currentColor = e.target.value;
    });
    (_c = document.getElementById("add-dot-pair")) === null || _c === void 0 ? void 0 : _c.addEventListener("click", () => {
        let color = document.getElementById("dot-color").value.trim().toLowerCase();
        // Count how many pairs of each color exist
        const colorCounts = {};
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
    (_d = document.getElementById("save-board")) === null || _d === void 0 ? void 0 : _d.addEventListener("click", () => {
        saveBoardState();
    });
    (_e = document.getElementById("delete-dot-pair")) === null || _e === void 0 ? void 0 : _e.addEventListener("click", () => {
        deletePairMode = !deletePairMode;
        const btn = document.getElementById("delete-dot-pair");
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
        document.getElementById("board-name").value = boardConfig.name;
        document.getElementById("board-rows").value = boardConfig.rows.toString();
        document.getElementById("board-cols").value = boardConfig.cols.toString();
        createGrid(boardConfig.rows, boardConfig.cols);
    }
    setup();
});
//# sourceMappingURL=board.js.map