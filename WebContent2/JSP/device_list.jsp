<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

<%@page import="richk.RMS.model.Device" %>
<%@page import="java.util.List" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>List of devices</title>

    <script type="text/javascript">

        function EditField(name, IP, serverPort, lastConnection) {
            var editForm = document.getElementById("EditForm")
            editForm.style.display = "inline";
            var editButton = document.getElementById("AddForm")
            buttonModifica.style.display = "none";

            document.getElementById("nameOldEdit").value = name;
            document.getElementById("nameEdit").value = name;
            document.getElementById("IPOldEdit").value = IP;
            document.getElementById("IPEdit").value = IP;
            document.getElementById("serverPortOldEdit").value = online;
            document.getElementById("serverPortEdit").value = online;
            document.getElementById("lastConnectionOldEdit").value = lastConnection;
            document.getElementById("lastConnectionEdit").value = lastConnection;

        }

    </script>
</head>
<body style="background-color: grey">

<h1>List of devices</h1>

<form action="EditDevice" method="post" id="EditForm"
      style="display: none;">
    <table style="background-color: blue">
        <tr>
            <td><input id="nameEdit" name="name" type="text" value=""/></td>
            <td><input id="IPEdit" name="IP" type="text" value=""/></td>
            <td><input id="ServerPortEdit" name="ServerPort" type="text"
                       value=""/></td>
            <td><input id="LastConnectionEdit" name="Lastconnection"
                       type="text" value=""/></td>

            <td><input id="nameOldEdit" name="nameOld" type="text" value=""/></td>
            <td><input id="IPOldEdit" name="IPOld" type="text" value=""/></td>
            <td><input id="ServerPortOldEdit" name="ServerPortOld"
                       type="text" value=""/></td>
            <td><input id="LastConnectionOldEdit" name="LastconnectionOld"
                       type="text" value=""/></td>

            <td><input id="submit" type="submit" value="EditDevice">
        </tr>
    </table>
</form>

<%
    List<Device> deviceList = (List<Device>) rmsSession.getAttribute("device");
    rmsSession.removeAttribute("device");
%>

<table>
    <tr>
        <td></td>
        <td>Name</td>
        <td>IP</td>
        <td>ServerPort</td>
        <td>LastConnection</td>
            <% for (Device device : deviceList){ %>

    <tr>
        <td></td>
        <td id="nameEdit">
            <%out.print(device.getName()); %>
        </td>
        <td id="IPEdit">
            <%out.print(device.getIP()); %>
        </td>
        <td id="serverPortEdit">
            <%out.print(device.getServerPort());%>
        </td>
        <td id="lastConnectionEdit">
            <%out.print(device.getLastConnection());%>
        </td>

        <td><input id="editButton" type="submit" value="edit"
                   onClick='EditField"<%out.print(device.getName()); %>", "<%out.print(device.getIP()); %>", "<%out.print(device.getServerPort());%>", "<%out.print(device.getLastConnection());%>")'>
        </td>

        <td><a
                href="/Richkware-Manager-Server/RemoveDevice?name=<%=device.getName() %>"
                style="color: red"> X </a></td>
    </tr>
    <% } %>
</table>
</body>
</html>