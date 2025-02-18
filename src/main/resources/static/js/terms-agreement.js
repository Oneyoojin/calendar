document.addEventListener("DOMContentLoaded", () => {
    const agreeAll = document.getElementById("agreeAll");
    const agreeItems = document.querySelectorAll(".agree-item");
    const nextButton = document.getElementById("nextButton");

    if (!agreeAll || agreeItems.length === 0 || !nextButton) return;

    // 버튼 활성화 상태 업데이트
    const updateButtonState = () => {
        nextButton.disabled = !Array.from(agreeItems).every(i => i.checked);
    };

    // "전체 동의" 체크 시 모든 항목 체크
    agreeAll.addEventListener("change", () => {
        agreeItems.forEach(item => item.checked = agreeAll.checked);
        updateButtonState();
    });

    // 개별 체크 시 전체 동의 상태 업데이트
    agreeItems.forEach(item => {
        item.addEventListener("change", () => {
            agreeAll.checked = Array.from(agreeItems).every(i => i.checked);
            updateButtonState();
        });
    });

    // "다음" 버튼 클릭 시 이동
    nextButton.addEventListener("click", () => {
        if (!nextButton.disabled) {
            window.location.href = "/api/calendar/find-username2"; // 🔥 Spring Boot 컨트롤러에서 처리
        }
    });

    // 초기 버튼 상태 업데이트
    updateButtonState();
});
