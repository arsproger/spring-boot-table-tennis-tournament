$(document).ready(function () {
    $("#searchInput").on("keyup", function () {
        let value = $(this).val().toLowerCase();
        $("#playerCards .card").filter(function () {
            $(this).toggle($(this).find(".card-title").text().toLowerCase().indexOf(value) > -1)
        });
    });
});

function toggleCardSelection(card) {
    $(card).toggleClass("selected-card");
    let checkbox = $(card).find("input[type=checkbox]");
    checkbox.prop("checked", !checkbox.prop("checked"));
}