document.addEventListener("DOMContentLoaded", function () {
    const titleModal = document.getElementById("titleModal");
    const listTitleInput = document.getElementById("listTitle");
    const confirmTitleButton = document.getElementById("confirmTitleButton");
    const taskBoard = document.getElementById("taskBoard");

    // ✅ "새 목록 만들기" 클릭 시 모달 열기
    window.openTitleModal = function () {
        titleModal.style.display = "block";
    };

    // ✅ "취소" 클릭 시 모달 닫기
    window.closeTitleModal = function () {
        titleModal.style.display = "none";
        listTitleInput.value = "";
        confirmTitleButton.disabled = true;
    };

    // ✅ 입력 감지 후 "완료" 버튼 활성화
    listTitleInput.addEventListener("input", function () {
        confirmTitleButton.disabled = listTitleInput.value.trim() === "";
    });

    // ✅ "완료" 클릭 시 새 목록 추가 (사이드바 & 메인 화면 카드)
    window.createList = function () {
        const listTitle = listTitleInput.value.trim();
        if (listTitle === "") return;

        console.log("✅ 새 목록 추가됨:", listTitle);

        // 🔹 사이드바 리스트에 추가
        const taskLists = document.querySelector(".task-lists");
        const newListItem = document.createElement("li");
        newListItem.textContent = listTitle;
        taskLists.appendChild(newListItem);

        // 🔹 메인 화면에 카드 추가
        const newCard = document.createElement("div");
        newCard.classList.add("title-task-card");
        newCard.innerHTML = `
            <h3>${listTitle}</h3>
            <p>새 목록이 추가되었습니다.</p>
        `;
        taskBoard.appendChild(newCard);

        closeTitleModal();
    };
});
