document.addEventListener("DOMContentLoaded", function () {
    const userCards = document.querySelectorAll(".user-card");

    userCards.forEach(card => {
        card.addEventListener("click", function () {
            this.classList.toggle("selected");
            const checkbox = this.querySelector("input[type='checkbox']");
            checkbox.checked = !checkbox.checked;
        });
    });
});
