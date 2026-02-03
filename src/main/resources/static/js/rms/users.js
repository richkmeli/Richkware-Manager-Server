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
//         $.get("/user", {channel: "webapp"}, /*data,*/ function (data, status) {
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
        $.get("/users", {channel: "webapp"}, function (data) {
            var JSONdata = JSON.parse(data)
            if (JSONdata.statusCode == 1000) {
                var users = JSONdata.message
                loadUsersJSONtoTable(users)
            } else if (JSONdata.statusCode == 2100) {
                var choice = confirm(JSONdata.message)
                if (choice)
                    window.location.replace("/html/index.html")
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
    var $usersTable = $("#usersTable");
    $usersTable.empty();

    var $thead = $("<thead>");
    var $row = $("<tr>");
    $row.append($("<th>").text("Email"))
        .append($("<th>").text("Password"))
        .append($("<th>").text("Admin"));

    $thead.append($row);
    $usersTable.append($thead);
}

function loadUsersJSONtoTable(usersListJSON) {
    createUsersTableHeader();

    var usersList = JSON.parse(usersListJSON);
    var $usersTable = $("#usersTable");
    var $tbody = $("<tbody>");

    for (var i = 0; i < usersList.length; ++i) {
        var email = usersList[i].email
        var password = usersList[i].password
        var admin = usersList[i].admin

        var $row = $("<tr>", {id: "tableRow" + i})
            .append($("<td>").text(email))
            .append($("<td>").append($("<a>", {
                type: "button",
                class: "btn btn-outline-secondary",
                "data-toggle": "popover",
                tabindex: "0",
                title: "Password",
                "data-content": password
            }).text("Show Password")))
            .append($("<td>").text(admin))
            .append($("<td>")
                .append($("<button>", {title: 'Edit', type: "button", class: "btn btn-primary"})
                    .click({email: email, password: password, admin: admin}, function (e) {
                        if (typeof editDevicesTableField === "function") {
                            editDevicesTableField(e.data.email, e.data.password, e.data.admin);
                        } else {
                            console.error("editDevicesTableField is not defined");
                        }
                    })
                    .append($("<span>", {class: "fa fa-pencil"})))
                .append(" ")
                .append($("<button>", {title: 'Remove', type: "button", class: "btn btn-danger"})
                    .click({email: email, i: i}, function (e) {
                        deleteUser(e.data.email, e.data.i);
                    })
                    .append($("<span>", {class: "fa fa-trash"}))));

        $tbody.append($row);
    }
    $usersTable.append($tbody);
    popInfo();
}

function deleteUser(email, indexTableRow) {
    $.ajax({
        url: '/user?email=' + email + "&channel=webapp",
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

function popInfo() {
    $("[data-toggle=popover]").popover();
}
