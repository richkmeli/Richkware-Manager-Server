<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <link rel="stylesheet" href="/Richkware-Manager-Server/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/Richkware-Manager-Server/bootstrap/css/bootstrap-theme.min.css"/>
    <script type="text/javascript" src="/Richkware-Manager-Server/JavaScript/jquery.min.js"></script>
    <script type="text/javascript" src="/Richkware-Manager-Server/bootstrap/js/bootstrap.min.js"></script>

    <title>Error</title>
</head>
<body>
    <div class="container">
        <h1>Error</h1>
        Please Contact The Administrator
        <br>
        <% out.print(session.getAttribute("error")); %>
    </div>
</body>
</html>