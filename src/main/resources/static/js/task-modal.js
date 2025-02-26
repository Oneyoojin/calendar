document.addEventListener("DOMContentLoaded", function () {
    const taskModal = document.getElementById("taskModal");
    const taskTitleInput = document.getElementById("taskTitle");
    const taskDateInput = document.getElementById("taskDate");
    const taskTimeInput = document.getElementById("taskTime");
    const taskDescriptionInput = document.getElementById("taskDescription");
    const saveButton = document.querySelector(".save-btn");

    // ✅ 모달 열기
    window.openModal = function () {
        taskModal.style.display = "flex";
    };

    // ✅ 모달 닫기
    window.closeModal = function () {
        taskModal.style.display = "none";
        resetModalFields();
    };

    // ✅ 입력값 초기화
    function resetModalFields() {
        taskTitleInput.value = "";
        taskDateInput.value = "";
        taskTimeInput.value = "";
        taskDescriptionInput.value = "";
        saveButton.setAttribute("disabled", "true");
    }

    // ✅ "저장" 버튼 활성화 (제목 입력 시)
    taskTitleInput.addEventListener("input", function () {
        if (this.value.trim() !== "") {
            saveButton.classList.add("active");
            saveButton.removeAttribute("disabled");
        } else {
            saveButton.classList.remove("active");
            saveButton.setAttribute("disabled", "true");
        }
    });

    // ✅ 모달 외부 클릭 시 닫기
    window.onclick = function (event) {
        if (event.target === taskModal) {
            closeModal();
        }
    };
});
