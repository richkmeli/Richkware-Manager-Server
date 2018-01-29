<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <link rel="stylesheet" href="/Richkware-Manager-Server/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/Richkware-Manager-Server/bootstrap/css/bootstrap-theme.min.css"/>
    <script type="text/javascript" src="/Richkware-Manager-Server/JavaScript/jquery.min.js"></script>
    <script type="text/javascript" src="/Richkware-Manager-Server/bootstrap/js/bootstrap.min.js"></script>

    <script type="text/javascript" src="/Richkware-Manager-Server/JavaScript/tableAJAJ.js"></script>

    <title>List of Devices</title>

</head>

<body style="background-color: white" onload="loadUsersTable()">

<div class="jumbotron text-center">
    <h1>List of Devices</h1>
</div>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-body">
            <button type="button" class="btn btn-default btn-sm pull-right" onclick="loadUsersTable()">
                <span class="glyphicon glyphicon-refresh"></span>
            </button>
            <!-- <form id = "AddForm"
              action="addDevice"
              method="post">
              <table style="background-color: green">
                  <tr>
                    <td>name: <input id = "name" name = "name" type = "text"value = ""/></td>
                    <td>IP: <input id = "IP" name = "IP" type = "text"value = ""/></td>
                    <td>serverPort: <input id = "serverPort" name = "serverPort" type = "text"value = ""/></td>
                    <td>lastConnection: <input id = "lastConnection" name = "lastConnection" type = "text"value = ""/></td>

                    <td><input id = "submit"
                            type = "submit"
                            value = "Add device">
                  </tr>
              </table>
              </form>
             <br>
            -->

            <form action="EditDevice" method="post" id="EditForm"
                  style="display: none;">
                <table class="table">
                    <tr>
                        <td><input id="name_E" name="name" type="text" value=""/></td>
                        <td><input id="name_OE" name="OLDname" type="text" value=""
                                   style="display: none"/></td>
                        <td><input id="IP_E" name="IP" type="text" value=""/></td>
                        <td><input id="IP_OE" name="OLDIP" type="text" value=""
                                   style="display: none"/></td>
                        <td><input id="serverPort_E" name="serverPort" type="text"
                                   value=""/></td>
                        <td><input id="serverPort_OE" name="OLDserverPort"
                                   type="text" value="" style="display: none"/></td>
                        <td><input id="lastConnection_E" name="lastConnection"
                                   type="text" value=""/></td>
                        <td><input id="lastConnection_OE" name="OLDlastConnection"
                                   type="text" value="" style="display: none"/></td>
                        <td><input id="encryptionKey_E" name="encryptionKey"
                                   type="text" value=""/></td>
                        <td><input id="encryptionKey_OE" name="OLDencryptionKey"
                                   type="text" value="" style="display: none"/></td>

                        <td><input id="submit" type="submit" value="Edit Device">
                    </tr>
                </table>
            </form>

            <div class="table-responsive">
                <table class="table table-hover" id="devicesTable">
                </table>
            </div>
        </div>
    </div>
</div>

</body>
</html>