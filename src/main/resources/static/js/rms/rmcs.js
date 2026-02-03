function loadRmcsTable() {
    console.log("loadRmcsTable")
    $.get("/rmc", {channel: "webapp"}, function (data) {
        var JSONdata = JSON.parse(data)

        //device = JSON.parse(DeviceJSON);
        //document.getElementById("Email").innerHTML = Person.email;

        //status : "success", "notmodified", "error", "timeout", or "parsererror"
        if (JSONdata.statusCode == 1000) {
            var rmcs = JSONdata.message
            loadRmcsJSONtoTable(rmcs)
        } else if (JSONdata.statusCode == 2100) {
            alert("You are not logged in. You are being redirected to the Login Page");
            window.location.replace = "/html/login.html";
        } else {
            alert(JSONdata.message)
        }
    })
}

function createRmcsTableHeader() {
    console.log("createRmcsTableHeader")
    var $rmcsTable = $("#rmcsTable");
    $rmcsTable.empty();

    var $thead = $("<thead>");
    var $row = $("<tr>");
    $row.append($("<th>").text("associatedUser"))
        .append($("<th>").text("RMCID"));

    $thead.append($row);
    $rmcsTable.append($thead);
    console.log("tableHeader")
}

function loadRmcsJSONtoTable(rmcsListJSON) {
    console.log("loadRmcsJSONtoTable")
    createRmcsTableHeader();

    var rmcsList = JSON.parse(rmcsListJSON);
    var $rmcsTable = $("#rmcsTable");
    var $tbody = $("<tbody>");

    console.log("rmcsList: " + rmcsList + " type: " + typeof (rmcsList) + " length: " + rmcsList.length)

    for (var i = 0; i < rmcsList.length; ++i) {
        console.log(rmcsList[i])

        var associatedUser = rmcsList[i].associatedUser;
        var rmcId = rmcsList[i].rmcId;

        var $row = $("<tr>", {id: "tableRow" + i})
            .append($("<td>").text(associatedUser))
            .append($("<td>").text(rmcId))
            .append($("<td>").append($("<button>", {title: 'Remove', type: "button", class: "btn btn-danger"})
                .click({user: associatedUser, i: i, id: rmcId}, function (e) {
                    deleteRmc(e.data.user, e.data.i, e.data.id);
                })
                .append($("<span>", {class: "fa fa-trash"}))));

        $tbody.append($row);
    }
    $rmcsTable.append($tbody);
}

function deleteRmc(associatedUser, indexTableRow, rmcId) {
    $.ajax({
        url: '/rmc?associatedUser=' + associatedUser + '&rmcId=' + rmcId + "&channel=webapp",
        type: 'DELETE',
        success: function (result) {
            console.log("result from server: " + result)
            var JSONdata = JSON.parse(result)
            if (JSONdata.statusCode == 1000) {
                $("#tableRow" + indexTableRow).remove();
            } else {
                alert(result)
            }
        }
    });
}

$(document).ready(function () {
    loadRmcsTable();
    setInterval(loadRmcsTable, 30000);

    // $("[id*=remove]").click(function () {
    //     var id = event.target.id.split("#")[1];
    //     var index = event.target.id.split("#")[2];
    //     var account = event.target.id.split('#')[3];
    //     $.delete("/rmc", {associatedUser: account, rmcid: id}, function () {
    //         $("#tableRow" + index).remove()
    //     })
    // });

})
