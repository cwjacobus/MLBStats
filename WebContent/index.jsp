<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>MLB Stats</title>
</head>
<body>
	<h1>MLB Stats</h1>
   	<form action="getPlayerStats">
      	<table>
      		<tr><td>Player Name</td><td><input type="text" name="playerName" size="10"/></td></tr>
      		<tr><td><input type="submit" value="Get Stats"/></td></tr>
		</table>
  	 </form>
</body>
</html>