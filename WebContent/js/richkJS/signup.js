$(document).ready(function() {
    $("#Register").click(function() {
        var email = $("#exampleInputEmail1").val()
        var pass = $("#exampleInputPassword1").val()
        var confirmPass = $("#exampleConfirmPassword").val()

        console.log(pass + " " + confirmPass)

        if (checkMail(email) && checkPassword(pass, confirmPass)) {
            console.log("checks passed")
            var send = {email: email, password: pass}
            $.post("SignUp", send, function(data) {
                var JSONdata = JSON.parse(data)
                console.log(JSONdata)
                if (JSONdata.statusCode == 1000) {
                    window.location.replace("/Richkware-Manager-Server/devices.html")
                } else {
                    alert(JSONdata.message)
                }
            })
        } else {
            alert("Some field is wrong, please check before registration!")
        }
    })
})

function checkMail(mail) {
    var pattern = new RegExp(/(\w+)(.|-|_)*@\w+.\w+/g);
    if (pattern.test(mail)) {
        return true
    }
    console.log("Invalid mail")
    return false
}

//TODO: fare controllo lunghezza password
function checkPassword(password, confirmPassword) {
    if (password == confirmPassword) {
        if (password.length >= 8) {
            return true
        }
    }
    console.log("Passwords not matching")
    return false
}