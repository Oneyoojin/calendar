document.addEventListener("DOMContentLoaded", function () {
    /** 📌 "할 일 추가" 모달 관련 요소 */
    const taskModal = document.getElementById("taskModal");
    const taskTitleInput = document.getElementById("taskTitle");
    const taskDateInput = document.getElementById("taskDate");
    const taskTimeInput = document.getElementById("taskTime");
    const taskDescriptionInput = document.getElementById("taskDescription");
    const saveButton = document.querySelector(".save-btn");
    const confirmTaskButton = document.getElementById("confirmTaskButton");

    /** ✅ 요소가 정상적으로 로드되었는지 확인 */
    if (!taskModal) {
        console.error("🚨 Error: 'taskModal' 요소가 없습니다. HTML을 확인하세요.");
        return;
    }
    if (!taskTitleInput) {
        console.error("🚨 Error: 'taskTitleInput' 요소를 찾을 수 없습니다. HTML을 확인하세요.");
        return;
    }
    if (!saveButton) {
        console.error("🚨 Error: 'saveButton' 요소를 찾을 수 없습니다. HTML을 확인하세요.");
        return;
    }
    if (!confirmTaskButton) {
        console.warn("⚠️ Warning: 'confirmTaskButton' 요소를 찾을 수 없습니다. 일부 기능이 제한될 수 있습니다.");
    }

    /** ✅ "할 일 추가" 모달 열기 */
    window.openModal = function () {
        taskModal.style.display = "flex";
    };

    /** ✅ "할 일 추가" 모달 닫기 */
    window.closeModal = function () {
        taskModal.style.display = "none";
        resetModalFields();
    };

    /** ✅ 입력값 초기화 */
    function resetModalFields() {
        taskTitleInput.value = "";
        if (taskDateInput) taskDateInput.value = "";
        if (taskTimeInput) taskTimeInput.value = "";
        if (taskDescriptionInput) taskDescriptionInput.value = "";
        saveButton.setAttribute("disabled", "true");
        if (confirmTaskButton) confirmTaskButton.disabled = true;
    }

    /** ✅ "저장" 버튼 및 "완료" 버튼 활성화 (제목 입력 시) */
    taskTitleInput.addEventListener("input", function () {
        const isNotEmpty = taskTitleInput.value.trim() !== "";
        saveButton.disabled = !isNotEmpty;
        saveButton.classList.toggle("active", isNotEmpty);
        if (confirmTaskButton) confirmTaskButton.disabled = !isNotEmpty;
    });

    /** ✅ 모달 외부 클릭 시 닫기 */
    window.onclick = function (event) {
        if (event.target === taskModal) {
            closeModal();
        }
    };
});
