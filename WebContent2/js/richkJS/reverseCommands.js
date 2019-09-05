$(document).ready(function () {
    var search = window.location.search
    var device = search.split("=")[1]

    $("#deviceId").html("Device: " + device)

    $("#submit-commands").click(function () {
        var commands = $("#text-area").val().split("\n");
        for (var i = 0; i < commands.length; ++i) {
            commands[i] = encryptCommand(commands[i])
        }
        var encryptedString = encryptCommand(commands.join("##"))
        var devicesArray = new Array()
        devicesArray.push(device)
        console.log(devicesArray)
        $.ajax({
            url: '/Richkware-Manager-Server/command',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({devices: [device], commands: encryptedString}),
            success: function (result) {
                // alert(result)
                window.location.replace("/Richkware-Manager-Server/devices.html")
            }
        });
        // $.put("command", {device: dev, commands: encryptedString}, function(data) {
        //     console.log(data)
        //     window.location.replace("/Richkware-Manager-Server/devices.html")
        // })
    })
})

function encryptCommand(string) {
    return btoa(string)
}