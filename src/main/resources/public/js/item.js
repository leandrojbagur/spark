$(document).ready(function() {
    $("#editBtn").click(function () {
        var itemId = $("#itemId").text();
        window.location = "/edit/" + itemId;
    });

    $("#backBtn").click(function () {
        window.location = "/store";
    });

    $("body").on("click", ".remove", function() {
        $(this).parents(".input-group").remove();
    });
});