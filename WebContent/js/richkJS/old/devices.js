

/*
function loadUsersTable() {
    createDevicesTableHeader();

    request = null;

    // microsoft explorer
    if (window.XMLHttpRequest)
        request = new XMLHttpRequest();
    else
        request = new ActiveXObject("Microsoft.XMLHTTP");

    request.onreadystatechange = newConnection;
    request.open("GET", "/Richkware-Manager-Server/DevicesList", true, null, null);

    request.send(null);
}


function newConnection() {
    if (request.readyState != 4) {
        //alert("request.readystate: "+ request.readystate)
        return;
    }
    if (request.status != 200) {
        alert("request.status :  " + request.status)
        return;
    }

    var JSON = request.responseText;
    var result;
    eval("result = " + JSON);

    loadUsersJSONtoTable(result);

}*/

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


