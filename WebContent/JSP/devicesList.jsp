<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@page import="java.util.List"%>
<%@page import="richk.RMS.model.Device"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Devices List</title>

<script type="text/javascript">

	function EditField(ID,nome,IP,online,lastConnection){ 
		var editForm = document.getElementById("EditForm")
		editForm.style.display = "inline";
		var editButton = document.getElementById("AddForm")
		buttonModifica.style.display = "none";
		
		document.getElementById("IDOldEdit").value = ID;
		document.getElementById("IDEdit").value = ID;
		document.getElementById("nameOldEdit").value = name;
		document.getElementById("nameEdit").value = name;
		document.getElementById("IPOldEdit").value = IP;
		document.getElementById("IPEdit").value = IP;
		document.getElementById("onlineOldEdit").value = online;
		document.getElementById("onlineEdit").value = online;
		document.getElementById("lastConnectionOldEdit").value = lastConnection;
		document.getElementById("lastConnectionEdit").value = lastConnection;
						
	}
	
	
	function loadDevicesTable(){
		var devicesTable = document.getElementById("devicesTable");
		devicesTable.innerHTML = "";
		
		var row = document.createElement("tr");
		row.innerHTML=("<td>ID</td>"+
						"<td>name</td>"+
						"<td>IP</td>"+
						"<td>online</td>"+
						"<td>lastConection</td>"+
						"<td></td>"+
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
		request.open("GET", "DevicesList", true, null, null);
		
		request.send(null);
	}
	
	
	function newConnection(){
		if (richiesta.readyState != 4){
			//alert("request.readystate: "+ request.readystate)
			return;
		}
		if (richiesta.status != 200){
			alert("request.status :  " + request.status)
			return;
		}

		var JSON = request.responseText;
		eval("result = " + JSON);
		
		+ "'ID' : '"+device.getID()+"', "
		+ "'name' : '"+device.getName()+"', "
		+ "'IP' : '"+device.getIP()+"', "
		+ "'online' : '"+device.getOnline()+"', "
		+ "'lastConnection' : '"+device.getLastConnection()+"'}";
		
		var index = 0
		while(result[index] != null){
			var device = result[index]
			var ID = device["ID"]
			var name = device["name"]
			var IP = device["IP"]
			var online = device["online"]
			var lastConnection = device["lastConnection"]
			
			var row = document.createElement("tr")
			row.innerHTML=("<td>"+ID+"</td>"+
					"<td>"+name+"</td>"+
					"<td>"+IP+"</td>"+
					"<td>"+online+"</td>"+
					"<td>"+lastConnection+"</td>"+
							"<td><a href=\"javascript:EditField('"+ID+"','"+name+"','"+IP+"','"+online+"','"+lastConnection+"')\">Edit</a></td>"+
							"<td><a href=\"RemoveDevice?ID='"+ID+"\"style='color: red'>X</a></td>")
			devicesTable.appendChild(row);
			index++
		}
		
	}
	
	
	
</script>


</head>

<body style="background-color: white"
	  onload="loadDevicesTable()">
<h1>Devices List</h1>
<a href="javascript:loadDevicesTable()">Reload List</a><br>

<form id = "AddForm"
	  action="AddDevice" 
	  method="post">
      <table style="background-color: green">
      	<tr> 
			<td>ID: <input id = "ID"
							name = "ID"
							type = "text"
							value = ""/></td> 
		<!--...--></td>  
							
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
	style="display: none;"
  			>
		      <table style="background-color: blue">
		      	<tr> 
					<td><input id = "IDEdit"
									name = "ID"
									type = "text"
									value = ""/></td> 
					<td><input id = "IDOLDEdit"
									name = "OLDID"
									type = "text"
									value = ""
									style="display: none"
									/></td>
					<!-- ... -->
					
					<td><input id = "submit"
							type = "submit"
							value = "Edit Device"
							>        
		      	</tr>
		      </table>
  		</form>	


	<table class = "devicesTable"
		   id = "devicesTable">
	</table>
	
</body>
</html>