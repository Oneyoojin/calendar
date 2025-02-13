document.addEventListener("DOMContentLoaded", function () {
    const agreeAll = document.getElementById("agreeAll");
    const agreeItems = document.querySelectorAll(".agree-item");
    const nextButton = document.getElementById("nextButton");

    if (!agreeAll || agreeItems.length === 0 || !nextButton) {
        console.error("필수 요소가 존재하지 않습니다.");
        return;
    }

    // 전체 동의 체크박스 이벤트
    agreeAll.addEventListener("change", function () {
        agreeItems.forEach(item => {
            item.checked = agreeAll.checked;
        });
        updateButtonState();
    });

    // 개별 동의 체크박스 이벤트
    agreeItems.forEach(item => {
        item.addEventListener("change", function () {
            // 모든 개별 항목이 체크되었는지 확인하여 전체 동의 체크 상태 갱신
            agreeAll.checked = [...agreeItems].every(i => i.checked);
            updateButtonState();
        });
    });

    // 버튼 활성화 상태 업데이트
    function updateButtonState() {
        nextButton.disabled = ![...agreeItems].every(i => i.checked);
    }

    // 초기 버튼 상태 업데이트 (페이지 로드 시)
    updateButtonState();
});
