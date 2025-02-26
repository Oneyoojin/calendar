document.addEventListener("DOMContentLoaded", function () {
    const taskBoard = document.getElementById("taskBoard");
    let taskMenuModal = document.getElementById("taskMenuModal");
    let firstTaskCard = null; // ✅ 첫 번째 카드 추적
    let sortAscending = true; // ✅ 기본 정렬은 오름차순
    const taskModal = document.getElementById("taskModal"); // ✅ 모달 요소 가져오기

    // ✅ taskMenuModal이 없으면 동적으로 생성하여 추가
    if (!taskMenuModal) {
        console.warn("⚠️ taskMenuModal 요소가 없음. 동적으로 생성합니다.");
        taskMenuModal = document.createElement("div");
        taskMenuModal.id = "taskMenuModal";
        taskMenuModal.style.display = "none";
        taskMenuModal.style.position = "absolute";
        taskMenuModal.style.zIndex = "1000";
        taskMenuModal.style.background = "white";
        taskMenuModal.style.boxShadow = "0px 4px 10px rgba(0, 0, 0, 0.2)";
        taskMenuModal.style.borderRadius = "8px";
        taskMenuModal.style.padding = "10px";
        taskMenuModal.style.fontSize = "14px";
        taskMenuModal.style.opacity = "0";
        taskMenuModal.style.transition = "opacity 0.3s ease, transform 0.2s ease";
        taskMenuModal.innerHTML = `
            <ul class="task-menu">
                <li class="selected">✔ 내가 정렬한 대로</li>
                <hr>
                <li>목록 이름 변경</li>
                <li class="disabled">목록 삭제 <span>기본 목록은 삭제할 수 없음</span></li>
                <hr>
                <li>목록 인쇄</li>
                <li>완료된 할 일 모두 삭제</li>
                <li class="disabled">오래된 할 일 정리</li>
            </ul>
        `;
        document.body.appendChild(taskMenuModal);
    }

    // ✅ 점 3개 클릭 시 메뉴 표시
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("dot-icon")) {
            event.stopPropagation();
            const dotMenu = event.target.closest(".dot-menu");
            const rect = dotMenu.getBoundingClientRect();

            taskMenuModal.style.top = `${rect.bottom + window.scrollY}px`;
            taskMenuModal.style.left = `${rect.left}px`;
            taskMenuModal.style.display = "block";
            taskMenuModal.style.opacity = "1";
            taskMenuModal.style.transform = "scale(1)";
        } else {
            if (!taskMenuModal.contains(event.target)) {
                taskMenuModal.style.opacity = "0";
                taskMenuModal.style.transform = "scale(0.95)";
                setTimeout(() => {
                    taskMenuModal.style.display = "none";
                }, 200);
            }
        }
    });

    // ✅ "만들기" 버튼 클릭 시 처리
    window.saveTask = function () {
        const title = document.getElementById("taskTitle").value.trim();
        const description = document.getElementById("taskDescription").value.trim();

        if (!title) {
            alert("제목을 입력하세요!");
            return;
        }

        if (!taskBoard) {
            console.error("❌ taskBoard 요소가 없습니다.");
            return;
        }

        if (!firstTaskCard) {
            // ✅ 첫 번째 "만들기" 클릭 시, 제목만 있는 카드 생성
            firstTaskCard = document.createElement("div");
            firstTaskCard.classList.add("task-card");
            firstTaskCard.dataset.hasContent = "true";
            firstTaskCard.style.position = "relative";

            firstTaskCard.innerHTML = `
                <h3>${title}</h3>
                <ul class="task-list"></ul> <!-- ✅ 리스트가 추가될 영역 -->
                <div class="sort-buttons" style="position: absolute; top: 10px; right: 50px;">
                    <img src="/images/—Pngtree—up and down arrow number_4706662.png" class="sort-icon" 
                         style="width: 24px; cursor: pointer;">
                </div>
                <div class="dot-menu" style="position: absolute; top: 10px; right: 10px;">
                    <img src="/images/free-icon-three-dot-menu-17399989.png" class="dot-icon">
                </div>
            `;
            taskBoard.appendChild(firstTaskCard);

            addSortEvent(firstTaskCard);
            addDotMenuEvent(firstTaskCard);

            console.log("✅ 첫 번째 카드 생성 완료 (알림 없음):", firstTaskCard);
        } else {
            // ✅ 두 번째 이후 "만들기" 클릭 시, 첫 번째 카드 아래에 리스트 추가
            let taskList = firstTaskCard.querySelector(".task-list");
            if (!taskList) {
                taskList = document.createElement("ul");
                taskList.classList.add("task-list");
                firstTaskCard.appendChild(taskList);
            }

            const newTaskItem = document.createElement("li");
            newTaskItem.classList.add("task-item");
            newTaskItem.innerHTML = `
                <h3>${title}</h3>
                <p class="description">${description || "내용 없음"}</p>
                <div class="dot-menu">
                    <img src="/images/free-icon-three-dot-menu-17399989.png" class="dot-icon">
                </div>
            `;
            taskList.appendChild(newTaskItem);

            addDotMenuEvent(newTaskItem);
        }

        closeModal(); // ✅ 모달 닫기 추가
    };

    // ✅ 모달 닫기 함수
    function closeModal() {
        console.log("✅ 모달 닫기 실행됨");
        if (!taskModal) {
            console.error("❌ 모달 요소를 찾을 수 없음");
            return;
        }

        taskModal.style.display = "none"; // ✅ 모달 숨김
        document.getElementById("taskTitle").value = "";
        document.getElementById("taskDescription").value = "";
    }

    // ✅ 정렬 기능 추가 함수
    function addSortEvent(taskCard) {
        const sortIcon = taskCard.querySelector(".sort-icon");
        if (sortIcon) {
            sortIcon.addEventListener("click", () => {
                sortAscending = !sortAscending; // ✅ 클릭 시 방향 전환
                sortTaskList(sortAscending);
            });
        }
    }

    // ✅ 리스트 정렬 함수
    function sortTaskList(ascending) {
        if (!firstTaskCard) return;

        const taskList = firstTaskCard.querySelector(".task-list");
        if (!taskList) return;

        const tasks = Array.from(taskList.children);

        tasks.sort((a, b) => {
            const titleA = a.querySelector("h3").textContent.toLowerCase();
            const titleB = b.querySelector("h3").textContent.toLowerCase();
            return ascending ? titleA.localeCompare(titleB) : titleB.localeCompare(titleA);
        });

        tasks.forEach(task => taskList.appendChild(task));

        console.log(ascending ? "✅ 오름차순 정렬 완료" : "✅ 내림차순 정렬 완료");
    }

    // ✅ 점 3개 버튼 이벤트 핸들러 추가 함수
    function addDotMenuEvent(taskElement) {
        const dotMenuButton = taskElement.querySelector(".dot-icon");
        if (dotMenuButton) {
            dotMenuButton.addEventListener("click", function (event) {
                event.stopPropagation();
                const rect = this.getBoundingClientRect();
                taskMenuModal.style.top = `${rect.bottom + window.scrollY}px`;
                taskMenuModal.style.left = `${rect.left}px`;
                taskMenuModal.style.display = "block";
                taskMenuModal.style.opacity = "1";
                taskMenuModal.style.transform = "scale(1)";
            });
        }
    }

    console.log("✅ saveTask 함수가 window 객체에 등록됨:", window.saveTask);
});
