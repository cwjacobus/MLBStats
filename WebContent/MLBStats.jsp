<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
	<title>MLB Stats</title>
	<style>
		body {
  			font-family: Arial, serif;
  			background-color: lightgrey;
  			color: darkred;
		}
	</style>
	<script type="text/javascript">
		battingStatsMap = ${sessionScope.battingStatsMapJSON};
    	pitchingStatsMap = ${sessionScope.pitchingStatsMapJSON};
    	
		function addDropDown(key, value, ddElement) {
			var option = document.createElement('option');
			option.text = value;
			option.value = key;
			ddElement.add(option);
		}
		
		function populateStatDropDown(batter) {
			statSeasonLeaders = document.getElementById("statSeasonLeaders");
			removeAllFromDropDown(statSeasonLeaders);
			if (batter) {
				for (const [key, value] of Object.entries(battingStatsMap)) {
					  //console.log(key, value);
					  addDropDown(key, value, statSeasonLeaders);
				}
			}
			else {
				for (const [key, value] of Object.entries(pitchingStatsMap)) {
					  addDropDown(key, value, statSeasonLeaders);
				}
			}
		}
		
		function removeAllFromDropDown(ddElement) {
			var len = ddElement.length;
			for (i=0; i < len;  i++) {
				ddElement.remove(0);
			}
		}
	</script>
</head>
<body>
	<h1>MLB Stats</h1>
   	<form action="getPlayerStats">
      	<table>
      		<tr><td>Type</td><td>
      			<input type="radio" name="playerType" value="batter">Batter
      			<input type="radio" name="playerType" value="pitcher">Pitcher</td></tr>
      		<tr><td>Player Name</td><td><input type="text" name="playerName" size="10"/></td></tr>
		</table>
		<input type="submit" value="Get Player Stats"/>
  	 </form>
  	 <br><br>
  	 <form action="getPlayerStats">
      	<table>
      		<tr><td>Year</td><td><input type="number" name="year" min="1900" max="2025" style="width: 60px;"/></td></tr>
      		<tr><td>Team</td><td><select name="teamId" id="teamId" style="width: 275px;">
      		<c:forEach items="${sessionScope.allMLBTeamsList}" var="mlbTeam">
      			<c:set var="lastYearPlayed" value="${mlbTeam.lastYearPlayed}"/>
      			<c:if test="${lastYearPlayed == ''}">
      				<c:set var="lastYearPlayed" value="Present"/>
      			</c:if>
  				<option value="${mlbTeam.teamId}">${mlbTeam.fullTeamName} - ${mlbTeam.shortTeamName} (${mlbTeam.firstYearPlayed}-${lastYearPlayed})</option>	
			</c:forEach>
      		</select></td></tr>
		</table>
		<input type="hidden" name="teamMode" value="true"/>
		<input type="submit" value="Get Team Player Stats"/>
  	 </form>
  	 <br><br>
  	 <form action="getSeasonLeaders">
      	<table>
      		<tr><td>Year</td><td><input type="number" name="year" min="1900" max="2025" style="width: 60px;"/></td></tr>
      		<tr><td>League</td><td><select name="league" id="league" style="width: 80px;">
  				<option value="AL">AL Only</option>	
  				<option value="NL">NL Only</option>
  				<option value="">All MLB</option>
      		</select></td></tr>
      		<tr><td>Stat Type</td><td>
      			<input type="radio" name="statType" value="batter" onchange="populateStatDropDown(true)">Batter
      			<input type="radio" name="statType" value="pitcher" onchange="populateStatDropDown(false)">Pitcher</td></tr>
      		<tr><td>Stat</td><td><select name="statSeasonLeaders" id="statSeasonLeaders" style="width: 80px;">
      		</select></td></tr>
		</table>
		<input type="submit" value="Get Season Leaders"/>
  	 </form>
</body>
</html>