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
    var rmcsTable = document.getElementById("rmcsTable");
    rmcsTable.innerHTML = "";

    var thead = document.createElement("thead");
    var row = document.createElement("tr");
    row.innerHTML = ( //"<th>Index</th>" +
        "<th>associatedUser</th>" +
        "<th>RMCID</th>");

    thead.appendChild(row);
    rmcsTable.appendChild(thead);
    console.log("tableHeader")
}

function loadRmcsJSONtoTable(rmcsListJSON) {
    console.log("loadRmcsJSONtoTable")
    createRmcsTableHeader();

    let rmcsList = JSON.parse(rmcsListJSON);
    let $tbody = $("<tbody>");
    let rmcsTable = document.getElementById("rmcsTable");

    for (let i = 0; i < rmcsList.length; ++i) {
        let associatedUser = rmcsList[i].associatedUser;
        let rmcId = rmcsList[i].rmcId;

        let $row = $("<tr>").attr("id", "tableRow" + i);

        let $userTd = $("<td>").text(associatedUser);
        let $rmcIdTd = $("<td>").text(rmcId);

        let $removeBtn = $("<button>")
            .attr("title", "Remove")
            .attr("type", "button")
            .addClass("btn btn-danger")
            .click(function() { deleteRmc(associatedUser, i, rmcId); })
            .append($("<span>").addClass("fa fa-trash"));

        let $removeTd = $("<td>").append($removeBtn);

        $row.append($userTd).append($rmcIdTd).append($removeTd);
        $tbody.append($row);
    }
    $(rmcsTable).append($tbody);
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
