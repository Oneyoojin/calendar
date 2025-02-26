document.addEventListener("DOMContentLoaded", function () {
    const taskMenuModal = document.getElementById("taskMenuModal");
    const dotIcons = document.querySelectorAll(".dot-icon");

    // ✅ 점 3개 클릭 시 taskMenuModal 표시
    dotIcons.forEach(dotIcon => {
        dotIcon.addEventListener("click", function (event) {
            event.stopPropagation(); // 클릭 이벤트 전파 방지

            if (!taskMenuModal) {
                console.error("taskMenuModal 요소를 찾을 수 없습니다.");
                return; // ✅ taskMenuModal이 없으면 실행하지 않음
            }

            if (taskMenuModal.classList.contains("show")) {
                taskMenuModal.classList.remove("show");
                taskMenuModal.style.display = "none";
            } else {
                taskMenuModal.classList.add("show");
                taskMenuModal.style.display = "block";
            }
        });
    });

    // ✅ 모달 외부 클릭 시 닫기
    document.addEventListener("click", function (event) {
        if (taskMenuModal && !taskMenuModal.contains(event.target)) {
            taskMenuModal.classList.remove("show");
            taskMenuModal.style.display = "none";
        }
    });
});
