$(document).ready(function() {
    $("#Register").click(function() {
        var email = $("#exampleInputEmail1").val()
        var pass = $("#exampleInputPassword1").val()
        var confirmPass = $("#exampleConfirmPassword").val()

        if (checkMail(email) && checkPassword(pass, confirmPass)) {
            $.post("SignUp", function(data) {
                var JSONdata = JSON.parse(data)
                if (JSONdata.statusCode == 1000) {
                    window.location.replace("/Richkware-Manager-Server/devices.html")
                } else {
                    //chiama la pagina di errore
                }
            })
        }
    })
})

function checkMail(mail) {

//\w+([.\w+]*)@\w+.\w+

}