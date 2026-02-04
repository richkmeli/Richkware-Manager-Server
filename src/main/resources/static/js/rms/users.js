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

    let usersList = JSON.parse(usersListJSON);
    let $tbody = $("<tbody>");
    let usersTable = document.getElementById("usersTable");

    for (let i = 0; i < usersList.length; ++i) {
        let email = usersList[i].email
        let password = usersList[i].password
        let admin = usersList[i].admin

        let $row = $("<tr>").attr("id", "tableRow" + i);

        let $emailTd = $("<td>").text(email);

        let $passwordBtn = $("<a>")
            .attr("type", "button")
            .addClass("btn btn-outline-secondary")
            .attr("data-toggle", "popover")
            .attr("tabindex", "0")
            .attr("title", "Password")
            .attr("data-content", password)
            .text("Show Password");

        let $passwordTd = $("<td>").append($passwordBtn);

        let $adminTd = $("<td>").text(admin);

        let $editBtn = $("<button>")
            .attr("title", "Edit")
            .attr("type", "button")
            .addClass("btn btn-primary")
            .click(function() { editDevicesTableField(email, password, admin); })
            .append($("<span>").addClass("fa fa-pencil"));

        let $editTd = $("<td>").append($editBtn);

        let $removeBtn = $("<button>")
            .attr("title", "Remove")
            .attr("type", "button")
            .addClass("btn btn-danger")
            .click(function() { deleteUser(email, i); })
            .append($("<span>").addClass("fa fa-trash"));

        let $removeTd = $("<td>").append($removeBtn);

        $row.append($emailTd).append($passwordTd).append($adminTd).append($editTd).append($removeTd);
        $tbody.append($row);
    }
    $(usersTable).append($tbody);
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
