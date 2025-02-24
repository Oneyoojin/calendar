// 모달 열기
function openModal() {
    document.getElementById("taskModal").style.display = "flex";
}

// 모달 닫기
function closeModal() {
    document.getElementById("taskModal").style.display = "none";
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    let modal = document.getElementById("taskModal");
    if (event.target === modal) {
        closeModal();
    }
};

// 할 일 저장 후 "오늘의 할 일" 목록에 추가
function saveTask() {
    let title = document.getElementById("taskTitle").value.trim();
    let todayTasks = document.getElementById("today-tasks");

    if (title === "") {
        alert("제목을 입력하세요!");
        return;
    }

    let newTask = document.createElement("li");
    newTask.textContent = `📌 ${title}`;
    todayTasks.appendChild(newTask);

    // 모달 닫기 및 입력 필드 초기화
    closeModal();
    document.getElementById("taskTitle").value = "";
}
