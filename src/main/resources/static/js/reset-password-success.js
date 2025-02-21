// 비밀번호 확인 함수
function validatePasswordForm(event) {
    const newPassword = document.querySelector('input[name="newPassword"]').value;
    const confirmPassword = document.querySelector('input[name="confirmPassword"]').value;
    const errorMessage = document.querySelector('.error-message p');

    // 비밀번호가 일치하지 않으면
    if (newPassword !== confirmPassword) {
        errorMessage.textContent = "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.";
        document.querySelector('.error-message').style.display = 'block'; // 오류 메시지 표시
        event.preventDefault(); // 폼 제출 방지
        return false;
    }

    // 비밀번호가 일치하면 오류 메시지 숨기기
    document.querySelector('.error-message').style.display = 'none';
    return true;
}

// 폼 제출 시 비밀번호 확인
document.getElementById('resetPasswordForm').addEventListener('submit', function(event) {
    validatePasswordForm(event);  // 비밀번호 확인을 위한 함수 호출
});
