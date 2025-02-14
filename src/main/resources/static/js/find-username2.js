document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    const inputs = form.querySelectorAll("input[type='text'], input[type='email']");
    const agreeCheckbox = document.getElementById("agreeAll");
    const nextButton = document.getElementById("nextButton");
    const nextLink = document.getElementById("nextLink");

    // 버튼 상태 업데이트 함수
    const updateButtonState = () => {
        const allFilled = [...inputs].every(input => input.value.trim() !== ""); 
        const isChecked = agreeCheckbox.checked;

        if (allFilled && isChecked) {
            nextButton.disabled = false;
            nextButton.classList.remove("bg-gray-400", "cursor-not-allowed");
            nextButton.classList.add("bg-blue-600", "hover:bg-blue-700");

            // 링크 활성화
            nextLink.href = "/find-username2";  
        } else {
            nextButton.disabled = true;
            nextButton.classList.remove("bg-blue-600", "hover:bg-blue-700");
            nextButton.classList.add("bg-gray-400", "cursor-not-allowed");

            // 링크 비활성화
            nextLink.href = "javascript:void(0);";  
        }
    };

    // 입력 필드 및 체크박스 이벤트 리스너 추가
    inputs.forEach(input => {
        input.addEventListener("input", updateButtonState);
    });

    agreeCheckbox.addEventListener("change", updateButtonState);
});
