<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta name="author" content="Riccardo Melioli">

    <meta http-equiv="refresh" content="5; /Richkware-Manager-Server/index.html">

    <link rel="stylesheet" href="../bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="../fonts/font-awesome/css/font-awesome.css"/>
    <script type="text/javascript" src="../js/jquery/jquery.js"></script>
    <script type="text/javascript" src="../bootstrap/js/bootstrap.js"></script>

    <script src="../js/richkJS/account.js"></script>

    <title>Error</title>

</head>
<body>
<div class="container">
    <h1>Error</h1>
    Please Contact The Administrator
    <br>
    <% out.print(rmsSession.getAttribute("error")); %>
</div>
</body>
</html>