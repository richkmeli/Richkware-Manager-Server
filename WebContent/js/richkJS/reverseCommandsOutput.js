$(document).ready(function () {
    var search = window.location.search
    var output = search.split("=\"")[1]
    console.log(search + "\n")
    console.log(output)

    var decryptedOutput = atob(output)
    var commands = decryptedOutput.split("##")

    for (var i = 0; i < commands.length; ++i) {
        $('#text-area').val(function (i, text) {
            return text + commands[i] + "\n";
        });
    }


})