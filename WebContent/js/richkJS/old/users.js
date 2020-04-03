
// function accountInformation() {
//
//     $(document).ready(function () {
//         $.get("user", {channel: "webapp"}, /*data,*/ function (data, status) {
//
//             user = data[0];
//             // load name near to the brand
//             var string = " - " + user.email;
//             if (user.isAdmin == true) {
//                 string = string + " (ADMIN)";
//             }
//             document.getElementById("userNearBrand").innerHTML = string;
//
//             // manage signup, login, logout
//             document.getElementById("signupNavBar").hidden = true;
//             document.getElementById("loginNavBar").hidden = true;
//             document.getElementById("logoutNavBar").hidden = false;
//
//             // if the user is an ADMIN, then it can see users.html
//             if (user.isAdmin == true) {
//                 document.getElementById("usersNavBar").hidden = false;
//             }
//
//         }, "json"/*, dataType*/)
//             .done(function () {
//                 //alert("second success");
//             })
//             .fail(function () {
//                 //alert( "error" );
//                 // manage signup, login, logout
//                 document.getElementById("signupNavBar").hidden = false;
//                 document.getElementById("loginNavBar").hidden = false;
//                 document.getElementById("logoutNavBar").hidden = true;
//             })
//             .always(function () {
//                 //alert("finished");
//             });
//     });
//
// }


    /* sc.onload = function () {
         document.getElementById("Logout").innerHTML = lang.logout;
         document.getElementById("Lang").innerHTML = lang.lang;
     };
     */

/*
function EditDevicesTableField(name, IP, serverPort, lastConnection) {
    var editForm = document.getElementById("EditForm");
    editForm.style.display = "inline";

    var editButton = document.getElementById("AddForm");
    //editButton.style.display = "none";

    document.getElementById("name_OE").value = name;
    document.getElementById("name_E").value = name;
    document.getElementById("IP_OE").value = IP;
    document.getElementById("IP_E").value = IP;
    document.getElementById("serverPort_OE").value = serverPort;
    document.getElementById("serverPort_E").value = serverPort;
    document.getElementById("lastConnection_OE").value = lastConnection;
    document.getElementById("lastConnection_E").value = lastConnection;
    document.getElementById("encryptionKey_OE").value = lastConnection;
    document.getElementById("encryptionKey_E").value = lastConnection;

}*/
