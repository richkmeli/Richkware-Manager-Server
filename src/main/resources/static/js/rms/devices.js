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

    let devicesList = JSON.parse(devicesListJSON);//jQuery.parseJSON(devicesListJSON);
    let tbody = document.createElement("tbody");

    console.log("devicesList: " + devicesList + " type: " + typeof (devicesList) + " length: " + devicesList.length);

    for (let i = 0; i < devicesList.length; ++i) {
        let device = devicesList[i];
        let name = device.name;
        let timeSinceNow = timeSince(new Date(Number(device.lastConnection))) + " ago";

        let $row = $("<tr>").attr("id", "tableRow" + i);

        // Safely build popover content
        let $popoverContent = $("<div>")
            .append($("<p>").text("IP: " + device.ip))
            .append($("<p>").text("Port: " + device.serverPort))
            .append($("<p>").text("Encryption Key: " + device.encryptionKey));

        let $infoA = $("<a>")
            .attr({
                "tabindex": "0",
                "type": "button",
                "data-html": "true",
                "class": "btn btn-outline-info",
                "data-toggle": "popover",
                "title": name + " Info",
                "data-content": $popoverContent.html()
            })
            .text(name);

        $row.append($("<td>").append($infoA));
        $row.append($("<td>").text(timeSinceNow));
        $row.append($("<td>").text(device.associatedUserEmail));
        $row.append($("<td>").text(device.commands));
        $row.append($("<td>").text(device.commandsOutput));

        let $actionsTd = $("<td>");

        $actionsTd.append($("<button>")
            .attr({"title": "Insert Commands", "aria-label": "Insert Commands", "type": "button", "class": "btn btn-secondary"})
            .click(() => commandsM(name))
            .append($("<span>").addClass("fa fa-terminal")));

        $actionsTd.append(" ").append($("<button>")
            .attr({"title": "View Output", "aria-label": "View Output", "type": "button", "class": "btn btn-primary"})
            .click(() => outputM(name))
            .append($("<span>").addClass("fa fa-eye")));

        $actionsTd.append(" ").append($("<button>")
            .attr({"title": "Remove Device", "aria-label": "Remove Device", "type": "button", "class": "btn btn-danger"})
            .click(() => deleteDevice(name, i))
            .append($("<span>").addClass("fa fa-trash")));

        $row.append($actionsTd);
        $(tbody).append($row);
    }
    devicesTable.appendChild(tbody);
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
