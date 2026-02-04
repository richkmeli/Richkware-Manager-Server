function loadDevicesTable() {
    console.log("loadDevicesTable");
    $.get("/devices", {channel: "webapp"}, function (data) {
        let JSONdata = JSON.parse(data);

        //device = JSON.parse(DeviceJSON);
        //document.getElementById("Email").innerHTML = Person.email;

        //status : "success", "notmodified", "error", "timeout", or "parsererror"
        if (JSONdata.statusCode === 1000) {
            var devices = JSONdata.message;
            loadDevicesJSONtoTable(devices)
        } else if (JSONdata.statusCode === 2100) {
            alert("You are not logged in. You are being redirected to the Login Page");
            window.location.replace = "/html/login.html";
        } else {
            alert(JSONdata.message)
        }
    })
}

function createDevicesTableHeader() {
    console.log("createDevicesTableHeader");
    let devicesTable = document.getElementById("devicesTable");
    devicesTable.innerHTML = "";

    let thead = document.createElement("thead");
    let row = document.createElement("tr");
    row.innerHTML = (
        "<th>Name</th>" +
        "<th>Last Connection</th>" +
        "<th>Associated User</th>" +
        "<th>Commands</th>" +
        "<th>Commands Output</th>" +
        "<th>Actions</th>");

    thead.appendChild(row);
    devicesTable.appendChild(thead);
    console.log("tableHeader")
}

function loadDevicesJSONtoTable(devicesListJSON) {
    console.log("loadDevicesJSONtoTable");
    createDevicesTableHeader();

    let devicesList = JSON.parse(devicesListJSON);
    let $tbody = $("<tbody>");
    let devicesTable = document.getElementById("devicesTable");

    for (let i = 0; i < devicesList.length; ++i) {
        let device = devicesList[i];
        let name = device.name;
        let IP = device.ip;
        let serverPort = device.serverPort;
        let lastConnection = device.lastConnection;
        let encryptionKey = device.encryptionKey;
        let associatedUser = device.associatedUserEmail;
        let commands = device.commands;
        let commandsOutput = device.commandsOutput;

        let timeSinceNow = timeSince(new Date(Number(lastConnection))) + " ago";

        let $row = $("<tr>").attr("id", "tableRow" + i);

        // Info Popover with escaped content
        let popoverContent = $("<div>")
            .append($("<p>").text("IP: " + IP))
            .append($("<p>").text("Port: " + serverPort))
            .append($("<p>").text("Encryption Key: " + encryptionKey))
            .html();

        let $infoBtn = $("<a>")
            .attr("tabindex", "0")
            .attr("type", "button")
            .addClass("btn btn-outline-info")
            .attr("data-toggle", "popover")
            .attr("data-html", "true")
            .attr("title", name + " Info")
            .attr("data-content", popoverContent)
            .text(name);

        $row.append($("<td>").append($infoBtn));
        $row.append($("<td>").text(timeSinceNow));
        $row.append($("<td>").text(associatedUser));
        $row.append($("<td>").text(commands));
        $row.append($("<td>").text(commandsOutput));

        let $actionsTd = $("<td>");

        let $cmdBtn = $("<button>")
            .attr("title", "Insert Commands")
            .attr("aria-label", "Insert Commands")
            .attr("type", "button")
            .addClass("btn btn-secondary")
            .click(function() { commandsM(name); })
            .append($("<span>").addClass("fa fa-terminal"));

        let $outBtn = $("<button>")
            .attr("title", "View Output")
            .attr("aria-label", "View Output")
            .attr("type", "button")
            .addClass("btn btn-primary")
            .click(function() { outputM(name); })
            .append($("<span>").addClass("fa fa-eye"));

        let $delBtn = $("<button>")
            .attr("title", "Remove Device")
            .attr("aria-label", "Remove Device")
            .attr("type", "button")
            .addClass("btn btn-danger")
            .click(function() { deleteDevice(name, i); })
            .append($("<span>").addClass("fa fa-trash"));

        $actionsTd.append($cmdBtn).append(" ").append($outBtn).append(" ").append($delBtn);
        $row.append($actionsTd);

        $tbody.append($row);
    }
    $(devicesTable).append($tbody);
    popInfo();
}

function timeSince(date) {
    var seconds = Math.floor((new Date() - date) / 1000);
    var interval = seconds / 31536000;

    if (interval > 1) {
        return Math.floor(interval) + " years";
    }
    interval = seconds / 2592000;
    if (interval > 1) {
        return Math.floor(interval) + " months";
    }
    interval = seconds / 86400;
    if (interval > 1) {
        return Math.floor(interval) + " days";
    }
    interval = seconds / 3600;
    if (interval > 1) {
        return Math.floor(interval) + " hours";
    }
    interval = seconds / 60;
    if (interval > 1) {
        return Math.floor(interval) + " minutes";
    }
    return Math.floor(seconds) + " seconds";
}

function infoDev(devL) {
    return '<table class="table" style="padding-left:50px;">' +
        '<tr>' +
        '<td>IP:Port</td>' +
        '<td>' + devL.ip + ':' + devL.serverPort + '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>EncryptionKey:</td>' +
        '<td>' + devL.encryptionKey + '</td>' +
        '</tr>' +
        '</table>';
}

function popInfo() {
    $("[data-toggle=popover]").popover();
}

//Open CommandsModal
function commandsM(device) {
    console.log(device);
    $('#ModalCommandTitle').text("Device: " + device);
    $('#ModalCommandTitle').val(device);
    $('#text-area').prop('readonly', false);
    $('#text-area').val("")
    $('#submit-commands').show();
    $('#ModalCommand').modal('show');
}

//Open CommandOutputModal
function outputM(device) {
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
        url: '/device?name=' + device,
        type: 'DELETE',
        success: function (result) {
            $("#tableRow" + indexTableRow).remove();
        }
    });
}

$(document).ready(function () {
    console.log("get devices from server")
    loadDevicesTable();
    setInterval(loadDevicesTable, 30000);

    $('#devicesTable tbody').on('click', 'button.btn', function () {
        var tr = $(this).closest('tr');
        var row = table.row(tr);

        if (row.child.isShown()) {
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
        $.delete("/device", {name: dev}, function () {
            $("#tableRow" + index).remove()
        })
    });

    $("[id*=manage]").click(function () {
        console.log("element clicked -> redirecting")
        var name = event.target.id.split("#")[1]
        window.location.replace = "/html/reverse-commands.html?device=" + name;
    });

    $("#refreshButton").click(function () {
        console.log("clicked refresh button")
        loadDevicesTable()
    })


});
