document.addEventListener("DOMContentLoaded", function () {
    const taskBoard = document.getElementById("taskBoard");
    let taskMenuModal = document.getElementById("taskMenuModal");

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
                <li>날짜</li>
                <li>최근 별표표시한 항목</li>
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
    } else {
        console.log("✅ taskMenuModal이 정상적으로 존재합니다.");
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

            console.log("✅ 점 3개 클릭됨, 모달 위치 조정 완료");
        } else {
            // ✅ 외부 클릭 시 메뉴 닫기
            if (!taskMenuModal.contains(event.target)) {
                taskMenuModal.style.opacity = "0";
                taskMenuModal.style.transform = "scale(0.95)";
                setTimeout(() => {
                    taskMenuModal.style.display = "none";
                }, 200);
            }
        }
    });

    // ✅ 할 일 카드 추가 기능 (window 객체에 등록하여 전역에서 접근 가능)
    window.saveTask = function () {
        const title = document.getElementById("taskTitle")?.value.trim();
        const date = document.getElementById("taskDate")?.value || "날짜 없음";
        const description = document.getElementById("taskDescription")?.value.trim() || "설명 없음";

        if (!title) {
            alert("제목을 입력하세요!");
            return;
        }

        if (!taskBoard) {
            console.error("❌ taskBoard 요소가 없습니다.");
            return;
        }

        // ✅ 새로운 할 일 카드 생성
        const newTask = document.createElement("div");
        newTask.classList.add("task-card");
        newTask.innerHTML = `
            <h3>${title}</h3>
            <div class="dot-menu">
                <img src="/images/free-icon-three-dot-menu-17399989.png" class="dot-icon">
            </div>
            <p>📌 알림: ${date}</p>
            <p class="description">${description}</p>
        `;

        // ✅ 생성된 카드 `taskBoard`에 추가
        taskBoard.appendChild(newTask);
        newTask.style.display = "block";

        console.log("✅ 새로운 카드 생성 완료:", newTask);

        // ✅ 추가된 카드의 점 3개 이벤트 핸들러
        newTask.querySelector(".dot-icon").addEventListener("click", function (event) {
            event.stopPropagation();
            const rect = this.getBoundingClientRect();
            taskMenuModal.style.top = `${rect.bottom + window.scrollY}px`;
            taskMenuModal.style.left = `${rect.left}px`;
            taskMenuModal.style.display = "block";
            taskMenuModal.style.opacity = "1";
            taskMenuModal.style.transform = "scale(1)";
            console.log("✅ 점 3개 클릭됨, 모달 위치 조정 완료 (동적 추가)");
        });

        document.getElementById("taskModal").style.display = "none";
    };

    console.log("✅ saveTask 함수가 window 객체에 등록됨:", window.saveTask);
});
