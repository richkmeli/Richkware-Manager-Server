$(document).ready(function() {

    $("#Login").click(function() {
        username = $("#exampleInputEmail1").val()
        password = $("#exampleInputPassword1").val()
        console.log("username: " + username + " password: " + password)
        $.post("LogIn", { email: username, password: password }, function(data) {
            console.log(data)
            var JSONdata = JSON.parse(data);
            console.log(JSONdata + ", " + JSONdata.statusCode)
            if (JSONdata.statusCode == 1000 || JSONdata.statusCode == 2101) {
                window.location.replace("/Richkware-Manager-Server/devices.html")
            } else if (JSONdata.statusCode == 2100) {
                alert("Credentials are not correct!")
            }
        })
    })

})