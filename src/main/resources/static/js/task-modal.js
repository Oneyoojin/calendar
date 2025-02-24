document.addEventListener("DOMContentLoaded", function () {
    const taskModal = document.getElementById("taskModal");
    const taskBoard = document.getElementById("taskBoard"); // 동적 추가 영역
    const taskTitleInput = document.getElementById("taskTitle");
    const taskDateInput = document.getElementById("taskDate");
    const taskTimeInput = document.getElementById("taskTime");
    const taskDescriptionInput = document.getElementById("taskDescription");
    const saveButton = document.querySelector(".save-btn");

    // 모달 열기
    window.openModal = function () {
        taskModal.style.display = "flex";
    };

    // 모달 닫기
    window.closeModal = function () {
        taskModal.style.display = "none";
        resetModalFields();
    };

    // 입력값 초기화
    function resetModalFields() {
        taskTitleInput.value = "";
        taskDateInput.value = "";
        taskTimeInput.value = "";
        taskDescriptionInput.value = "";
        saveButton.setAttribute("disabled", "true");
    }

    // "저장" 버튼 활성화 (제목 입력 시)
    taskTitleInput.addEventListener("input", function () {
        if (this.value.trim() !== "") {
            saveButton.classList.add("active");
            saveButton.removeAttribute("disabled");
        } else {
            saveButton.classList.remove("active");
            saveButton.setAttribute("disabled", "true");
        }
    });

    // 저장 버튼 클릭 시 할 일 추가
    window.saveTask = function () {
        const title = taskTitleInput.value.trim();
        const date = taskDateInput.value || "날짜 없음";
        const time = taskTimeInput.value || "시간 없음";
        const description = taskDescriptionInput.value.trim() || "설명 없음";

        if (title === "") {
            alert("제목을 입력하세요!");
            return;
        }

        // 새로운 할 일 카드 생성
        const taskCard = document.createElement("div");
        taskCard.classList.add("task-card");

        taskCard.innerHTML = `
            <h3>${title}</h3>
            <a href="#" class="add-task">➕ 할 일 추가</a>
            <p>📌 알림 <span class="alert">${date} ${time}</span></p>
            <p class="description">${description}</p>
        `;

        // 할 일 목록에 추가
        taskBoard.appendChild(taskCard);

        // 모달 닫기 및 입력 필드 초기화
        closeModal();
    };

    // 모달 외부 클릭 시 닫기
    window.onclick = function (event) {
        if (event.target === taskModal) {
            closeModal();
        }
    };
});
