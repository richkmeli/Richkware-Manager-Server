import setUserNearBrand from "account.js";

function main() {
    setUserNearBrand();
    createUsersTableHeader();
    loadUsersTable();
}

function loadUsersTable() {
    $(document).ready(function () {
        $.get("UsersList", /*data,*/ function (data, status) {
            var userJSON = data;
            //    alert (userJSON);
            //device = JSON.parse(DeviceJSON);
            //document.getElementById("Email").innerHTML = Person.email;

            //status : "success", "notmodified", "error", "timeout", or "parsererror"
            if (status === "error" || status === "timeout" || status === "parsererror") {
                alert("You are not logged in. You are being redirected to the Login Page");
                window.location.href = "login.html";
            } else {
                //if you set "json" as dataType, it's already parsed, so it's a JSON object
                loadUsersJSONtoTable(userJSON);
            }

        }, "json"/*, dataType*/)
            .done(function () {
                //alert("second success");
            })
            .fail(function () {
                //alert( "error" );
                alert("You are not logged in. You are being redirected to the Login Page");
                window.location.href = "login.html";
            })
            .always(function () {
                //alert("finished");
            });
    });

    /* sc.onload = function () {
         document.getElementById("Logout").innerHTML = lang.logout;
         document.getElementById("Lang").innerHTML = lang.lang;
     };
     */
}

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

function createUsersTableHeader() {
    var usersTable = document.getElementById("usersTable");
    usersTable.innerHTML = "";

    var thead = document.createElement("thead");
    var row = document.createElement("tr");
    row.innerHTML = ( //"<th>Index</th>" +
        "<th>Email</th>" +
        "<th>Password</th>" +
        "<th>isAdmin</th>");

    thead.appendChild(row);
    usersTable.appendChild(thead);
}

function loadUsersJSONtoTable(usersListJSON) {

    var usersList = usersListJSON//jQuery.parseJSON(usersListJSON);

    var tbody = document.createElement("tbody");
    //var index = 0;
    //while (usersList[index] != null) {
    //for(var device in usersList){
    $.each(usersList, function (index, value) {
        //var user = usersList[index];
        var user = value;

        var email = user.email;
        var password = user.password;
        var isAdmin = user.isAdmin;

        var row = document.createElement("tr");
        row.id = "tableRow" + index;

        row.innerHTML = (
            //"<td>" + (index + 1) + "</td>" +
            "<td>" + email + "</td>" +
            "<td>" + password + "</td>" +
            "<td>" + isAdmin + "</td>" +
            "<td><button type=\"button\" class=\"btn btn-secondary\" onclick=\"EditDevicesTableField('" + email + "','" + password + "','" + isAdmin + "')\">Edit</button></td>" +
            "<td><button type=\"button\" class=\"btn btn-warning\" onclick=\"deleteUser('" + name + "','" + index + "')\">Remove</button></td>");

        tbody.appendChild(row);
        //      index++
    });
    usersTable.appendChild(tbody);
}

function deleteUser(user, indexTableRow){
    $.ajax({
        url: '/Richkware-Manager-Server/user?name='+user,
        type: 'DELETE',
        success: function(result) {
            $("#tableRow"+indexTableRow).remove();
        }
    });
}