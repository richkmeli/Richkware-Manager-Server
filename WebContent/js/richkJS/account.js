$(document).ready(function () {

    $.get("user", function(data) {
        console.log(data)
        var JSONdata = JSON.parse(data)
        if (JSONdata.statusCode == 1000) {
            console.log(JSONdata.message + " " + typeof(JSONdata.message))

            $("#signupNavBar").hide()
            $("#loginNavBar").hide()

            var JSONmessage = JSON.parse(JSONdata.message)
            if (JSONmessage.admin == true) {
                $("#userNearBrand").html(" - " + JSONmessage.user + " (ADMIN)")
                $("#usersNavBar").show()
            } else {
                $("#userNearBrand").html(" - " + JSONmessage.user)
            }
        }
    })

 });