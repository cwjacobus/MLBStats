package actions;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

import dao.DAO;
import data.MLBTeam;
import init.MLBStatsDatabase;

public class InitAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	Map<String, Object> userSession;
	String playerName;
	LinkedHashMap<String, String> battingStatsMap  = new LinkedHashMap<String, String>() {
		private static final long serialVersionUID = 1L;
	{
		put("BATTING_AVERAGE", "Batting Average");
		put("HOME_RUNS", "Home Runs");
		put("RBIS", "Runs Batted In");
		put("STOLEN_BASES", "Stolen Bases");
		put("HITS", "Hits");
		put("DOUBLES", "Doubles");
		put("TRIPLES", "Triples");
		put("WALKS", "Walks(Batter)");
		put("STRIKEOUTS", "Strikeouts(Batter)");
		put("HIT_BY_PITCH", "Hit By Pitch");
		put("RUNS", "Hits");
		put("PLATE_APPEARANCES", "Plate Appearances");
		put("CAUGHT_STEALING", "Caught Stealing");
	}};
	LinkedHashMap<String, String> pitchingStatsMap  = new LinkedHashMap<String, String>() {
		private static final long serialVersionUID = 1L;
	{
		put("EARNED_RUN_AVERAGE", "Earned Run Average");
		put("INNINGS_PITCHED", "Innings Pitched");
		put("WALKS", "Walks(Pitcher)");
		put("STRIKEOUTS", "Strikeouts(Pitcher)");
		put("RUNS_ALLOWED", "Runs Allowed");
		put("EARNED_RUNS_ALLOWED", "Earned Runs Allowed");
		put("HOME_RUNS_ALLOWED", "Home Runs Allowed");
		put("STOLEN_BASES_ALLOWED", "Stolen Bases Allowed");
		put("HIT_BATTERS", "Hit Batters");
		put("HITS_ALLOWED", "Hits Allowed");
		put("HOLDS", "Holds");
		put("SAVES", "Saves");
		put("BLOWN_SAVES", "Blown Saves");
		put("GAMES_STARTED", "Games Started");
		put("BALKS", "Balks");
		put("WILD_PITCHES", "Wild Pitches");
		put("SAC_FLIES", "Sacrifice Flies");
		put("BATTERS_FACED", "Batters Faced");
		put("WINS", "Wins");
		put("LOSSES", "Losses");
	}};

	public String execute() throws Exception {
		ValueStack stack = ActionContext.getContext().getValueStack();
	    Map<String, Object> context = new HashMap<String, Object>();
	    
	    MLBStatsDatabase bowlPoolDB = (MLBStatsDatabase)ServletActionContext.getServletContext().getAttribute("Database");  
        Connection con = bowlPoolDB.getCon();
        DAO.setConnection(con);
        try {
        	DAO.pingDatabase();
        }
        catch (CommunicationsException ce) {
        	System.out.println("DB Connection timed out - Reconnect");
        	con = bowlPoolDB.reconnectAfterTimeout();
        	DAO.setConnection(con);
        }
        
        ArrayList<MLBTeam> allMLBTeamsList = DAO.getAllMLBTeamsList();
        userSession.put("allMLBTeamsList", allMLBTeamsList);
        
	    stack.push(context);
	    userSession.put("battingStatsMap", battingStatsMap);
	    userSession.put("pitchingStatsMap", pitchingStatsMap);
	    return "success";
	}
	
	@Override
    public void setSession(Map<String, Object> sessionMap) {
        this.userSession = sessionMap;
    }

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
