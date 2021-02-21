function getOutput(deviceName) {
    console.error(deviceName);
    $.get("/command", {data0: deviceName, channel: "webapp"}, function (response) {
        var JSONdata = JSON.parse(response)
        if (JSONdata.statusCode == 1000) {
            var output = JSONdata.message
            var decryptedOutput = atob(output)
            var commands = decryptedOutput.split("##")
            // console.log(commands)
            console.log("commands retrieved! " + commands)
            handleReverseCommandOutput(commands);


            // var previous = ""
            // for (var i = 0; i < commands.length; ++i) {
            //     previous = $("#text-area").val()
            //     $('#text-area').val(previous + "\n" + atob(commands[i]))
            // }
        } else {
            handleReverseCommandOutput("");
        }
        // alert("Error, retrieving reverse commands output: " + response)
    })

}
