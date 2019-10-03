function loadRmcsTable() {
    console.log("loadRmcsTable")
    $.get("rmc", {channel: "webapp"}, function (data) {
        var JSONdata = JSON.parse(data)

        //device = JSON.parse(DeviceJSON);
        //document.getElementById("Email").innerHTML = Person.email;

        //status : "success", "notmodified", "error", "timeout", or "parsererror"
        if (JSONdata.statusCode == 1000) {
            var rmcs = JSONdata.message
            loadRmcsJSONtoTable(rmcs)
        } else if (JSONdata.statusCode == 2100) {
            alert("You are not logged in. You are being redirected to the Login Page");
            window.location.replace = "/Richkware-Manager-Server/login.html";
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
        "<th>Account</th>" +
        "<th>RMCID</th>");

    thead.appendChild(row);
    rmcsTable.appendChild(thead);
    console.log("tableHeader")
}

function loadRmcsJSONtoTable(rmcsListJSON) {
    console.log("loadRmcsJSONtoTable")
    createRmcsTableHeader();

    var rmcsList = JSON.parse(rmcsListJSON)//jQuery.parseJSON(devicesListJSON);

    var tbody = document.createElement("tbody");

    console.log("rmcsList: " + rmcsList + " type: " + typeof (rmcsList) + " length: " + rmcsList.length)
    //var index = 0;
    //while (devicesList[index] != null) {
    //for(var device in devicesList){
    for (var i = 0; i < rmcsList.length; ++i) {
//        $.each(devicesList, function (index, value) {
        //var device = devicesList[index];
//            var device = value;
        console.log(rmcsList[i])

        var user = rmcsList[i].user;
        var rmcId = rmcsList[i].rmcId;

        var row = document.createElement("tr");
        row.id = "tableRow" + i;

        row.innerHTML = (
            //"<td>" + (index + 1) + "</td>" +
            "<td>" + user + "</td>" +
            "<td>" + rmcId + "</td>" +
            "<td><button type=\"button\" class=\"btn btn-warning\" onclick=\"deleteRmc('" + user + "', '" + i + "', '" + rmcId + "')\">Remove</button></td>");

        //        "<td><button type=\"button\" class=\"btn btn-warning\" onclick=\"location.href=\'/Richkware-Manager-Server/device?name=" + name + "\';\">Remove</button></td>");

        tbody.appendChild(row);
        //      index++
    }
    rmcsTable.appendChild(tbody);
}

function deleteRmc(user, indexTableRow, rmcId) {
    $.ajax({
        url: '/Richkware-Manager-Server/rmc?user=' + user + '&rmcId=' + rmcId,
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
    //     $.delete("rmc", {user: account, rmcid: id}, function () {
    //         $("#tableRow" + index).remove()
    //     })
    // });

})