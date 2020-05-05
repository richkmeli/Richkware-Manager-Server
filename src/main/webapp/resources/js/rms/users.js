function bodyOnLoad() {
    // accountInformation();

    loadUsersTable();
    window.setInterval(function () {
        loadUsersTable();
    }, 5000);
}

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


function loadUsersTable() {

    $(document).ready(function () {
        $.get("users", {channel: "webapp"}, function (data) {
            var JSONdata = JSON.parse(data)
            if (JSONdata.statusCode == 1000) {
                var users = JSONdata.message
                loadUsersJSONtoTable(users)
            } else if (JSONdata.statusCode == 2100) {
                var choice = confirm(JSONdata.message)
                if (choice)
                    window.location.replace("/Richkware-Manager-Server/index.html")
            } else {
                alert(JSONdata.message)
            }

        })
    });

    /* sc.onload = function () {
         document.getElementById("Logout").innerHTML = lang.logout;
         document.getElementById("Lang").innerHTML = lang.lang;
     };
     */
}

function createUsersTableHeader() {
    var usersTable = document.getElementById("usersTable");
    usersTable.innerHTML = "";

    var thead = document.createElement("thead");
    var row = document.createElement("tr");
    row.innerHTML = ( //"<th>Index</th>" +
        "<th>Email</th>" +
        "<th>Password</th>" +
        "<th>Admin</th>");

    thead.appendChild(row);
    usersTable.appendChild(thead);
}

function loadUsersJSONtoTable(usersListJSON) {
    createUsersTableHeader();

    var usersList = JSON.parse(usersListJSON)//jQuery.parseJSON(usersListJSON);

    var tbody = document.createElement("tbody");
    //var index = 0;
    //while (usersList[index] != null) {
    //for(var device in usersList){
    for (var i = 0; i < usersList.length; ++i) {
        var email = usersList[i].email
        var password = usersList[i].password
        var admin = usersList[i].admin

        var row = document.createElement("tr");
        row.id = "tableRow" + i;

        row.innerHTML = (
            //"<td>" + (index + 1) + "</td>" +
            "<td>" + email + "</td>" +
            "<td>" + password + "</td>" +
            "<td>" + admin + "</td>" +
            "<td><button title='Edit' type=\"button\" class=\"btn btn-primary\" onclick=\"editDevicesTableField('" + email + "','" + password + "','" + admin + "')\"><span class=\"fa fa-pencil\"></button></td>" +
            "<td><button title='Remove' type=\"button\" class=\"btn btn-danger\" onclick=\"deleteUser('" + email + "','" + i + "')\"><span class=\"fa fa-trash\"></button></td>");

        tbody.appendChild(row);
    }
    usersTable.appendChild(tbody);
}

function deleteUser(email, indexTableRow) {
    $.ajax({
        url: '/Richkware-Manager-Server/user?email=' + email + "&channel=webapp",
        type: 'DELETE',
        success: function (result) {
            console.log(result)
            var dataJSON = JSON.parse(result)
            if (dataJSON.statusCode == 1000) {
                $("#tableRow" + indexTableRow).remove();
            }
        }
    });
}
