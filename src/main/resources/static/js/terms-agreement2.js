document.addEventListener("DOMContentLoaded", () => {
    const agreeAll = document.getElementById("agreeAll");
    const nextButton = document.getElementById("nextButton");

    if (!agreeAll || !nextButton) return;

    // "전체 동의" 체크 여부에 따라 버튼 활성화
    agreeAll.addEventListener("change", () => {
        nextButton.disabled = !agreeAll.checked;
    });

    // "다음" 버튼 클릭 시 이동
    nextButton.addEventListener("click", () => {
        if (!nextButton.disabled) {
            window.location.href = "/api/sample/find-username2";
        }
    });
});
