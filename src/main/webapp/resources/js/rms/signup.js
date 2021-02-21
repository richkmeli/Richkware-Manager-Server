$(document).ready(function () {
    $("#Register").click(function () {
        var email = $("#exampleInputEmail1").val()
        var pass = $("#exampleInputPassword1").val()
        var confirmPass = $("#exampleConfirmPassword").val()

        console.log(pass + " " + confirmPass)

        var mailCheck = checkMail(email)
        var passwordCheck = checkPassword(pass, confirmPass)

        if (mailCheck == "OK" && passwordCheck == "OK") {
            console.log("checks passed")
            var send = {email: email, password: pass, channel: "webapp"}
            $.post("/SignUp", send, function (data) {
                var JSONdata = JSON.parse(data)
                console.log(JSONdata)
                if (JSONdata.statusCode == 1000) {
                    window.location.replace("/html/devices.html")
                } else {
                    alert(JSONdata.message)
                }
            })
        } else {
            if (mailCheck != "OK") {
                alert(mailCheck)
            } else if (passwordCheck != "OK") {
                alert(passwordCheck)
            } else {
                alert("Some field is wrong, please check before registration!")
            }
        }
    })
})

function checkMail(mail) {
    var pattern = new RegExp(/(\w+)(\.|-|_)*@\w+\.\w+/g);
    if (pattern.test(mail)) {
        return "OK"
    }
    return "Invalid email!"
}

function checkPassword(password, confirmPassword) {
    if (password === confirmPassword) {
        if (password.length >= 8) {
            return "OK"
        } else {
            return "Password is too short!"
        }
    } else {
        return "Passwords does not match!"
    }
}