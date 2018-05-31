$(document).ready(function() {
    $("#saveBtn").click(function () {
        var data = parseFormData($("#itemForm"));
        $.post("/create", JSON.stringify(data), function(response) {
            $.notify({
                message: JSON.parse(response).message
            }, {
                type: 'success'
            });
        })
        .fail(function(response) {
            $.notify({
                message: JSON.parse(response.responseText).message
            }, {
                type: 'danger'
            });
        });

    });

    $("#editBtn").click(function () {
        var itemId = $("#itemId").val();
        var data = parseFormData($("#itemForm"));
        $.ajax({
            url: "/update/" + itemId,
            method: "PUT",
            data: JSON.stringify(data),
            success: function(response) {
                $.notify({
                    message: JSON.parse(response).message
                }, {
                    type: 'success'
                });
            },
            error: function (response) {
                $.notify({
                    message: JSON.parse(response.responseText).message
                }, {
                    type: 'danger'
                });
            }
        });
    });

    $("#backBtn").click(function () {
        window.location = "/store";
    });

    $("#addImage").click(function() {
        var html = $(".copy-fields").html();
        $("#itemForm").append(html);
    });

    $("body").on("click", ".remove", function() {
        $(this).parents(".input-group").remove();
    });

    function parseFormData(form) {
        var formData = form.serializeArray();
        var data = {};
        $(formData).each(function(index, obj) {
            if (obj.name === 'pictures') {
                if (!data.hasOwnProperty(obj.name)) {
                    data[obj.name] = [];
                }
                data[obj.name].push(obj.value);
            } else {
                data[obj.name] = obj.value;
            }
        });
        return data;
    }
});