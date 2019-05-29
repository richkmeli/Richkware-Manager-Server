function accountInformation() {

    $(document).ready(function () {

        $("#Login").click(function() {
            username = $("#exampleInputEmail1").val()
            password = $("#exampleInputPassword1").val()
            console.log("username: " + username + " password: " + password)
            $.post("LogIn", { email: username, password: password }, function(data) {
                if (data.includes("OK")) {
                    window.location.replace("/Richkware-Manager-Server/devices.html")
                } else {
                    alert("Credentials are not correct!")
                }
            })
        })

     });

}