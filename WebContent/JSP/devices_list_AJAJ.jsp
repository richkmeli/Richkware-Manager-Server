<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@page import="java.util.List"%>
<%@page import="richk.RMS.model.Device"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List of Devices</title>

<script type="text/javascript">

	function EditField(name,IP,serverPort,lastConnection){ 
		var editForm = document.getElementById("EditForm")
		editForm.style.display = "inline";
		var editButton = document.getElementById("AddForm")
		buttonModifica.style.display = "none";
		
		document.getElementById("nameOldEdit").value = name;
		document.getElementById("nameEdit").value = name;
		document.getElementById("IPOldEdit").value = IP;
		document.getElementById("IPEdit").value = IP;
		document.getElementById("serverPortOldEdit").value = serverPort;
		document.getElementById("serverPortEdit").value = serverPort;
		document.getElementById("lastConnectionOldEdit").value = lastConnection;
		document.getElementById("lastConnectionEdit").value = lastConnection;
						
	}
	
	
	function loadDevicesTable(){
		var devicesTable = document.getElementById("devicesTable");
		devicesTable.innerHTML = "";
		
		var row = document.createElement("tr");
		row.innerHTML=(	"<td>name</td>"+
						"<td>IP</td>"+
						"<td>serverPort</td>"+
						"<td>lastConection</td>"+
						"<td></td>"+
						"<td></td>"+
						"<td></td>"+
						"<td></td>")
		devicesTable.appendChild(row)
		
		request = null;
		
		// microsoft explorer
		if(window.XMLHttpRequest) 
			request = new XMLHttpRequest();
		else
			request = new ActiveXObject("Microsoft.XMLHTTP");
		
		request.onreadystatechange = newConnection;
		request.open("GET", "/Richkware-Manager-Server/DevicesListServletAJAJ", true, null, null);
		
		request.send(null);
	}
	
	
	function newConnection(){
		if (request.readyState != 4){
			//alert("request.readystate: "+ request.readystate)
			return;
		}
		if (request.status != 200){
			alert("request.status :  " + request.status)
			return;
		}

		var JSON = request.responseText;
		eval("result = " + JSON);
	
		var index = 0
		while(result[index] != null){
			var device = result[index]
			var name = device["name"]
			var IP = device["IP"]
			var serverPort = device["serverPort"]
			var lastConnection = device["lastConnection"]
			
			var row = document.createElement("tr")
			row.innerHTML=("<td>"+name+"</td>"+
					"<td>"+IP+"</td>"+
					"<td>"+serverPort+"</td>"+
					"<td>"+lastConnection+"</td>"+
							"<td><a href=\"javascript:EditField('"+name+"','"+IP+"','"+serverPort+"','"+lastConnection+"')\">Edit</a></td>"+
							"<td><a href=\"/Richkware-Manager-Server/RemoveDeviceServlet?name="+name+"\"style='color: red'>X</a></td>")
			devicesTable.appendChild(row);
			index++
		}
		
	}	
	
</script>


</head>

<body style="background-color: white"
	  onload="loadDevicesTable()">
<h1>List of Devices</h1>
<a href="javascript:loadDevicesTable()">Reload List</a><br>

<form id = "AddForm"
	  action="AddDevice" 
	  method="post">
      <table style="background-color: green">
      	<tr> 
			<td>name: <input id = "name" name = "name" type = "text"value = ""/></td>  
			<td>IP: <input id = "IP" name = "IP" type = "text"value = ""/></td>  
			<td>serverPort: <input id = "serverPort" name = "serverPort" type = "text"value = ""/></td>  
			<td>lastConnection: <input id = "lastConnection" name = "lastConnection" type = "text"value = ""/></td>  
							
			<td><input id = "submit"
					type = "submit"
					value = "Add Device">        
      	</tr>
      </table>
	  </form>
	 <br>
	 
<form action="EditDevice" 
	method="post"
	id="EditForm"
	style="display: none;">
		      <table style="background-color: blue">
		      	<tr> 
					<td><input id = "nameEdit" name = "name" type = "text"	value = ""/></td>
					<td><input id = "nameOLDEdit" name = "OLDname"	type = "text" value = "" style="display: none"/></td>
					<td><input id = "IPEdit" name = "IP" type = "text"	value = ""/></td>
					<td><input id = "IPOLDEdit" name = "OLDIP"	type = "text" value = "" style="display: none"/></td>
					<td><input id = "serverPortEdit" name = "serverPort" type = "text"	value = ""/></td>
					<td><input id = "serverPortOLDEdit" name = "OLDserverPort"	type = "text" value = "" style="display: none"/></td>
					<td><input id = "lastConnectionEdit" name = "lastConnection" type = "text"	value = ""/></td>
					<td><input id = "lastConnectionOLDEdit" name = "OLDlastConnection"	type = "text" value = "" style="display: none"/></td>
										
					<td><input id = "submit"
							type = "submit"
							value = "Edit Device">        
		      	</tr>
		      </table>
  		</form>	


	<table class = "devicesTable"
		   id = "devicesTable">
	</table>
	
</body>
</html>