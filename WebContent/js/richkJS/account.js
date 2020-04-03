$(document).ready(function () {

    $("#logoutNavBar").hide();
   /*$("#menuDevices").hide();
    $("#menuUsers").hide();
    $("#menuRMCs").hide();*/

    $.get("user", {channel: "webapp"}, function (data) {
        console.log(data);
        let JSONdata = JSON.parse(data);
        if (JSONdata.statusCode === 1000) {
            console.log(JSONdata.message + " " + typeof (JSONdata.message));

            $("#signupNavBar").hide();
            $("#loginNavBar").hide();
            $("#logoutNavBar").show();

            let path = window.location.pathname;
            let page = (path.split("/").pop()).split(".")[0];
            switch (page) {
                case "devices":
                    $("#menuRMCs").show();
                    break;
                case "users":
                    $("#menuDevices").show();
                    $("#menuRMCs").show();
                    break;
                case "rmcs":
                    $("#menuDevices").show();
                    break;
            }

            let JSONmessage = JSON.parse(JSONdata.message);
            if (JSONmessage.admin === true) {
                //$("#userNearBrand").html(" - " + JSONmessage.user + " (ADMIN)")
                $("#userNearBrand").html("").append($("<i></i>").text(JSONmessage.user + " (ADMIN)"));
                if (page!=="users") {
                    $("#menuUsers").show();
                }
            } else {
                //$("#userNearBrand").html(" - " + JSONmessage.user)
                $("#menuUsers").hide();
                $("#userNearBrand").html("").append($("<i></i>").text(JSONmessage.user))
            }

            append($("<p></p>").text(response.description))
        } else if (JSONdata.statusCode === 2100) {
            $("#logoutNavBar").hide();
            $("#menuDevices").hide();
            $("#menuUsers").hide();
            $("#menuRMCs").hide();
            let JSONmessage = JSON.parse(JSONdata.message);
            let choice = confirm(JSONmessage);
            if (choice) {
                window.location.replace("/Richkware-Manager-Server/index.html");
            }
        }
    }).fail(function () {
        $("#signupNavBar").show();
        $("#loginNavBar").show();
        $("#menuUsers").hide();
        $("#menuRMCs").hide();
    });

    $("#logoutNavBar").click(function () {
        console.log("logout clicked");
        $.get("LogOut", {channel: 'webapp'}, function (data) {
            let JSONdata = JSON.parse(data);
            if (JSONdata.statusCode === 1000) {
                window.location.replace("/Richkware-Manager-Server/index.html")
            } else {
                alert(JSONdata.message)
            }
        })
    })
});