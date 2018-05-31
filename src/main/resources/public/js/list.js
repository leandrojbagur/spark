$(document).ready(function() {
    var table = $("#itemTable").DataTable({
        ajax: {
            url: "/items",
            dataSrc: "",
            method: "GET"
        },
        columns: [
            { data: "itemId",
                render: function(data, type) {
                    if(type === 'display') {
                        data = '<a href="/item/' + data + '">' + data + '</a>';
                    }

                    return data;
                }
            },
            { data: "title" },
            { data: "description" },
            { data: "categoryId" },
            {
                data: null,
                render: function () {
                    return '<button name="deleteBtn"><span class="glyphicon glyphicon-trash"></span></button>';
                },
                sortable: false
            }],
        language: {
            "emptyTable": "No hay items",
            "info":           "Mostrando _START_ a _END_ de _TOTAL_ items",
            "infoEmpty":      "Mostrando 0 a 0 de 0 items",
            "thousands":      ".",
            "lengthMenu":     "Mostrar _MENU_ items",
            "loadingRecords": "Cargando...",
            "processing":     "Procesando...",
            "search":         "Buscar:",
            "zeroRecords":    "No se encuentran items",
            "paginate": {
                "first":      "Primera",
                "last":       "Última",
                "next":       "Siguiente",
                "previous":   "Anterior"
            }
        },
        drawCallback: function () {
            $("button[name='deleteBtn']").click(function () {
                var itemId = $(this).closest('tr').find('td:first-child a').text();
                $.ajax({
                    url: "/delete/" + itemId,
                    method: "DELETE",
                    success: function(response) {
                        setTimeout(function() {
                            table.ajax.reload();
                            $.notify({
                                message: JSON.parse(response).message
                            }, {
                                type: 'success'
                            });
                        }, 1000);
                    }
                })
            });
        }
    });

    $("#createBtn").click(function () {
        window.location = "/create";
    });
});