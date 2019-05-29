function accountInformation() {

    $(document).ready(function () {

        //listener su click invio in input del form
        //se clicca key 13 (invio) simula click su #Login
//        $("input").

        $("#loginForm").submit(function() {
            $.post("LogIn", function(data) {
                alert(data)
                window.location.replace("/Richkware-Manager-Server/devices.html")
            })

        });
//        $.get("user", /*data,*/ function (data, status) {
//
//            user = data[0];
//            // load name near to the brand
//            var string = " - " + user.email;
//            if (user.isAdmin == true) {
//                string = string + " (ADMIN)";
//            }
//            document.getElementById("userNearBrand").innerHTML = string;
//
//            // manage signup, login, logout
//            document.getElementById("signupNavBar").hidden = true;
//            document.getElementById("loginNavBar").hidden = true;
//            document.getElementById("logoutNavBar").hidden = false;
//
//            // if the user is an ADMIN, then it can see users.html
//            if (user.isAdmin == true) {
//                document.getElementById("usersNavBar").hidden = false;
//            }
//
//        }, "json"/*, dataType*/)
//            .done(function () {
//                //alert("second success");
//            })
//            .fail(function () {
//                //alert( "error" );
//                // manage signup, login, logout
//                document.getElementById("signupNavBar").hidden = false;
//                document.getElementById("loginNavBar").hidden = false;
//                document.getElementById("logoutNavBar").hidden = true;
//            })
//            .always(function () {
//                //alert("finished");
//            });
    });

}