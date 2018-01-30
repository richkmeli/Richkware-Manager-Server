import setUserNearBrand from "account.js";

function main() {
    serUserNearBrand();
    createDevicesTableHeader();
    loadDevicesTable();
}

function loadDevicesTable() {
    createDevicesTableHeader();

    $(document).ready(function () {
        $.get("DevicesList", /*data,*/ function (data, status) {
            var deviceJSON = data;
            //    alert (deviceJSON);
            //device = JSON.parse(DeviceJSON);
            //document.getElementById("Email").innerHTML = Person.email;

            //status : "success", "notmodified", "error", "timeout", or "parsererror"
            if (status === "error" || status === "timeout" || status === "parsererror") {
                alert("You are not logged in. You are being redirected to the Login Page");
                window.location.href = "login.html";
            } else {
                //if you set "json" as dataType, it's already parsed, so it's a JSON object
                loadDevicesJSONtoTable(deviceJSON);
            }

        }, "json"/*, dataType*/)
            .done(function () {
                //alert("second success");
            })
            .fail(function () {
                //alert( "error" );
                alert("You are not logged in. You are being redirected to the Login Page");
                window.location.href = "login.html";
            })
            .always(function () {
                //alert("finished");
            });
    });

    /* sc.onload = function () {
         document.getElementById("Logout").innerHTML = lang.logout;
         document.getElementById("Lang").innerHTML = lang.lang;
     };
     */
}

/*
function EditDevicesTableField(name, IP, serverPort, lastConnection) {
    var editForm = document.getElementById("EditForm");
    editForm.style.display = "inline";

    var editButton = document.getElementById("AddForm");
    //editButton.style.display = "none";

    document.getElementById("name_OE").value = name;
    document.getElementById("name_E").value = name;
    document.getElementById("IP_OE").value = IP;
    document.getElementById("IP_E").value = IP;
    document.getElementById("serverPort_OE").value = serverPort;
    document.getElementById("serverPort_E").value = serverPort;
    document.getElementById("lastConnection_OE").value = lastConnection;
    document.getElementById("lastConnection_E").value = lastConnection;
    document.getElementById("encryptionKey_OE").value = lastConnection;
    document.getElementById("encryptionKey_E").value = lastConnection;

}*/

function createDevicesTableHeader() {
    var devicesTable = document.getElementById("devicesTable");
    devicesTable.innerHTML = "";

    var thead = document.createElement("thead");
    var row = document.createElement("tr");
    row.innerHTML = ( //"<th>Index</th>" +
        "<th>Name</th>" +
        "<th>IP</th>" +
        "<th>Server Port</th>" +
        "<th>Last Connection</th>" +
        "<th>Encryption Key</th>" +
        "<th>User Associated</th>");

    thead.appendChild(row);
    devicesTable.appendChild(thead);
}

function loadDevicesJSONtoTable(devicesListJSON) {

    var devicesList = devicesListJSON//jQuery.parseJSON(devicesListJSON);

    var tbody = document.createElement("tbody");
    //var index = 0;
    //while (devicesList[index] != null) {
    //for(var device in devicesList){
    $.each(devicesList, function (index, value) {
        //var device = devicesList[index];
        var device = value;

        var name = device.name;
        var IP = device["IP"];
        var serverPort = device["serverPort"];
        var lastConnection = device["lastConnection"];
        var encryptionKey = device["encryptionKey"];
        var userAssociated = device["userAssociated"];

        var row = document.createElement("tr");
        row.id = "tableRow" + index;

        row.innerHTML = (
            //"<td>" + (index + 1) + "</td>" +
            "<td>" + name + "</td>" +
            "<td>" + IP + "</td>" +
            "<td>" + serverPort + "</td>" +
            "<td>" + lastConnection + "</td>" +
            "<td>" + encryptionKey + "</td>" +
            "<td>" + userAssociated + "</td>" +
            "<td><button type=\"button\" class=\"btn btn-secondary\" onclick=\"EditDevicesTableField('" + name + "','" + IP + "','" + serverPort + "','" + lastConnection + "')\">Edit</button></td>" +
            "<td><button type=\"button\" class=\"btn btn-warning\" onclick=\"deleteDevice('" + name + "','" + index + "')\">Remove</button></td>");

        //        "<td><button type=\"button\" class=\"btn btn-warning\" onclick=\"location.href=\'/Richkware-Manager-Server/device?name=" + name + "\';\">Remove</button></td>");

        tbody.appendChild(row);
        //      index++
    });
    devicesTable.appendChild(tbody);
}

function deleteDevice(device, indexTableRow){
    $.ajax({
        url: '/Richkware-Manager-Server/device?name='+device,
        type: 'DELETE',
        success: function(result) {
            $("#tableRow"+indexTableRow).remove();
        }
    });
}

/*
function loadUsersTable() {
    createDevicesTableHeader();

    request = null;

    // microsoft explorer
    if (window.XMLHttpRequest)
        request = new XMLHttpRequest();
    else
        request = new ActiveXObject("Microsoft.XMLHTTP");

    request.onreadystatechange = newConnection;
    request.open("GET", "/Richkware-Manager-Server/DevicesList", true, null, null);

    request.send(null);
}


function newConnection() {
    if (request.readyState != 4) {
        //alert("request.readystate: "+ request.readystate)
        return;
    }
    if (request.status != 200) {
        alert("request.status :  " + request.status)
        return;
    }

    var JSON = request.responseText;
    var result;
    eval("result = " + JSON);

    loadUsersJSONtoTable(result);

}
*/