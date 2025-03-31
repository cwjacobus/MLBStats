package actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

import dao.DAO;
import data.MLBBattingStats;
import data.MLBPitchingStats;
import data.MLBTeam;

public class GetPlayerStatsAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	Map<String, Object> userSession;
	String playerType;
	String playerName;
	Integer mlbPlayerId;
	Integer teamId;
	Integer year;
	String battingSortType;
	String pitchingSortType;

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		ValueStack stack = ActionContext.getContext().getValueStack();
	    Map<String, Object> context = new HashMap<String, Object>();
	    if (userSession == null || userSession.size() == 0) {
			context.put("errorMsg", "Session has expired!");
			stack.push(context);
			return "error";
		}
	    if (teamId == null  && playerType == null) {
        	context.put("errorMsg", "Player type is required!");
			stack.push(context);
			return "error";
        }
        if (teamId == null && (playerName == null || playerName.trim().length() == 0) && mlbPlayerId == null) {
        	context.put("errorMsg", "Player name is required!");
			stack.push(context);
			return "error";
        }
        
        if (teamId != null && (year == null || teamId == null)) {
        	context.put("errorMsg", "Year and team are required!");
			stack.push(context);
			return "error";
        }
        
		List<MLBBattingStats> mlbBattingStatsList = (List<MLBBattingStats>)userSession.get("mlbBattingStatsList");
        List<MLBPitchingStats> mlbPitchingStatsList = (List<MLBPitchingStats>)userSession.get("mlbPitchingStatsList");
        boolean batter = teamId == null && playerType.equals("batter");
        if (batter) {
        	if (battingSortType == null) {
        		mlbBattingStatsList = mlbPlayerId == null ? DAO.getMLBBattingStatsList(playerName):DAO.getMLBBattingStatsList(mlbPlayerId);
        	}
			Map<Integer, MLBBattingStats> multipleBattersMap = new HashMap<>();
			mlbBattingStatsList.stream().forEach(entry -> multipleBattersMap.put(entry.getMlbPlayerId(), entry));
			if (multipleBattersMap.size() > 1) {
				List<MLBBattingStats> multipleBattersList = new ArrayList<>(multipleBattersMap.values());
				context.put("multipleBattersList", multipleBattersList);
			}
			else if (multipleBattersMap.size() == 1) {
				if (battingSortType != null) {
					sortBattingStatsList(mlbBattingStatsList, battingSortType);
				}
				userSession.put("mlbBattingStatsList", mlbBattingStatsList);
			}
			else {
				context.put("errorMsg", "No stats found for: " + playerName);
				stack.push(context);
				return "error";
			}
		}
		else if (teamId == null && !batter) { // pitcher
			if (pitchingSortType == null) {
				mlbPitchingStatsList = mlbPlayerId == null ? DAO.getMLBPitchingStatsList(playerName):DAO.getMLBPitchingStatsList(mlbPlayerId);
			}
			Map<Integer, MLBPitchingStats> multiplePitchersMap = new HashMap<>();
			mlbPitchingStatsList.stream().forEach(entry -> multiplePitchersMap.put(entry.getMlbPlayerId(), entry));
			if (multiplePitchersMap.size() > 1) {
				List<MLBPitchingStats> multiplePitchersList = new ArrayList<>(multiplePitchersMap.values());
				context.put("multiplePitchersList", multiplePitchersList);
			}
			else if (multiplePitchersMap.size() == 1) {
				userSession.put("mlbPitchingStatsList", mlbPitchingStatsList);
			}
			else {
				context.put("errorMsg", "No stats found for: " + playerName);
				stack.push(context);
				return "error";
			}
		}
		else { // team mode
			mlbPitchingStatsList = DAO.getTeamMLBPitchingStatsListByYear(year, teamId);
			mlbBattingStatsList = DAO.getTeamMLBBattingStatsListByYear(year, teamId);
			if (mlbPitchingStatsList.size() == 0) {
				context.put("errorMsg", "No pitching stats found for: " + year + "" + "TBD Team Name") ;
				stack.push(context);
				return "error";
			}
			else if ((mlbBattingStatsList.size() == 0)) {	
				context.put("errorMsg", "No batting stats found for: " + year + "" + "TBD Team Name");
				stack.push(context);
				return "error";
			}
			if (battingSortType != null) {
				sortBattingStatsList(mlbBattingStatsList, battingSortType);
			}
			userSession.put("mlbPitchingStatsList", mlbPitchingStatsList);
			userSession.put("mlbBattingStatsList", mlbBattingStatsList);
			ArrayList<MLBTeam> allMLBTeamsList = (ArrayList<MLBTeam>)userSession.get("allMLBTeamsList");
			String teamDisplayName = null;
			Optional<MLBTeam> teamMatch = 
					allMLBTeamsList
				.stream()
				.filter((p) -> (p.getTeamId() == teamId.intValue() && p.getFirstYearPlayed().intValue() <= year.intValue()
					 && (p.getLastYearPlayed() == 0 || p.getLastYearPlayed().intValue() >= year.intValue())))
				.findAny();
			if (teamMatch.isPresent()) {
				teamDisplayName = teamMatch.get().getFullTeamName();
			}
			context.put("teamDisplayName", teamDisplayName);
			context.put("teamId", teamId);
		}
	    stack.push(context);
	    return "success";
	}
	
	public void sortBattingStatsList(List<MLBBattingStats> battersList, String type) 
    { 
			Collections.sort(battersList, new Comparator<MLBBattingStats>() { 
            public int compare(MLBBattingStats o1, MLBBattingStats o2) { 
            	if (type.equals("SB")) {
            		if (o1.getStolenBases() == o2.getStolenBases()) {
            			return 0;
            		}
            		return (o1.getStolenBases() >= o2.getStolenBases() ? -1 : 1); 
            	}
            	else if (type.equals("H")){
            		if (o1.getHits() == o2.getHits()) {
            			return 0;
            		}
            		return (o1.getHits() > o2.getHits() ? -1 : 1);
            	}
            	else if (type.equals("HR")){
            		if (o1.getHomeRuns() == o2.getHomeRuns()) {
            			return 0;
            		}
            		return (o1.getHomeRuns() > o2.getHomeRuns() ? -1 : 1);
            	}
            	else if (type.equals("RBI")){
            		if (o1.getRbis() == o2.getRbis()) {
            			return 0;
            		}
            		return (o1.getRbis() > o2.getRbis() ? -1 : 1);
            	}
            	else if (type.equals("PA")){
            		if (o1.getPlateAppearances() == o2.getPlateAppearances()) {
            			return 0;
            		}
            		return (o1.getPlateAppearances() > o2.getPlateAppearances() ? -1 : 1);
            	}
            	else if (type.equals("BA")){
            		if (o1.getBattingAverage() == o2.getBattingAverage()) {
            			return 0;
            		}
            		return (o1.getBattingAverage() > o2.getBattingAverage() ? -1 : 1);
            	}
            	/*else if (type.equals("GS")){
            		// Use innings pitched as next sort criteria if tied
            		if (o1.getValue().getMlbPitchingStats().getPitchingStats().getGamesStarted() == o2.getValue().getMlbPitchingStats().getPitchingStats().getGamesStarted()) {
            			return (o1.getValue().getMlbPitchingStats().getPitchingStats().getInningsPitched() >= o2.getValue().getMlbPitchingStats().getPitchingStats().getInningsPitched() ? -1 : 1);
            		}
            		return (o1.getValue().getMlbPitchingStats().getPitchingStats().getGamesStarted() > o2.getValue().getMlbPitchingStats().getPitchingStats().getGamesStarted() ? -1 : 1);
            	}
            	else if (type.equals("SV")){
            		if (o1.getValue().getMlbPitchingStats().getPitchingStats().getSaves() == o2.getValue().getMlbPitchingStats().getPitchingStats().getSaves()) {
            			return 0;
            		}
            		return (o1.getValue().getMlbPitchingStats().getPitchingStats().getSaves() > o2.getValue().getMlbPitchingStats().getPitchingStats().getSaves() ? -1 : 1);
            	}
            	else if (type.equals("HD")){
            		if (o1.getValue().getMlbPitchingStats().getPitchingStats().getHolds() == o2.getValue().getMlbPitchingStats().getPitchingStats().getHolds()) {
            			return 0;
            		}
            		return (o1.getValue().getMlbPitchingStats().getPitchingStats().getHolds() > o2.getValue().getMlbPitchingStats().getPitchingStats().getHolds() ? -1 : 1);
            	}
            	else if (type.equals("IP")){
            		if (o1.getValue().getMlbPitchingStats().getPitchingStats().getInningsPitched() == o2.getValue().getMlbPitchingStats().getPitchingStats().getInningsPitched()) {
            			return 0;
            		}
            		return (o1.getValue().getMlbPitchingStats().getPitchingStats().getInningsPitched() > o2.getValue().getMlbPitchingStats().getPitchingStats().getInningsPitched() ? -1 : 1);
            	} */
            	else {
            		return 0;
            	}
            } 
        }); 
    }
	
	@Override
    public void setSession(Map<String, Object> sessionMap) {
        this.userSession = sessionMap;
    }

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Integer getMlbPlayerId() {
		return mlbPlayerId;
	}

	public void setMlbPlayerId(Integer mlbPlayerId) {
		this.mlbPlayerId = mlbPlayerId;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getBattingSortType() {
		return battingSortType;
	}

	public void setBattingSortType(String battingSortType) {
		this.battingSortType = battingSortType;
	}

	public String getPitchingSortType() {
		return pitchingSortType;
	}

	public void setPitchingSortType(String pitchingSortType) {
		this.pitchingSortType = pitchingSortType;
	}

}
