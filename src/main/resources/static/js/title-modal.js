document.addEventListener("DOMContentLoaded", function () {
    const taskModal = document.getElementById("taskModal");
    const taskTitleInput = document.getElementById("taskTitle");
    const confirmTaskButton = document.getElementById("confirmTaskButton");

    // ✅ 요소가 존재하는지 확인 후 이벤트 추가
    if (taskTitleInput && confirmTaskButton) {
        // ✅ 입력 감지 후 "완료" 버튼 활성화
        taskTitleInput.addEventListener("input", function () {
            confirmTaskButton.disabled = taskTitleInput.value.trim() === "";
        });
    } else {
        console.error("🚨 Error: 'taskTitleInput' 또는 'confirmTaskButton' 요소를 찾을 수 없습니다. HTML을 확인하세요.");
    }

    // ✅ "할 일 추가" 버튼 클릭 시 모달 열기
    window.openModal = function () {
        if (taskModal) {
            taskModal.style.display = "flex";
        } else {
            console.error("🚨 Error: 'taskModal' 요소가 없습니다.");
        }
    };

    // ✅ "닫기" 버튼 클릭 시 모달 닫기
    window.closeModal = function () {
        if (taskModal) {
            taskModal.style.display = "none";
            if (taskTitleInput) taskTitleInput.value = "";
            if (confirmTaskButton) confirmTaskButton.disabled = true;
        }
    };
});
