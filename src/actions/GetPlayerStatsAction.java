package actions;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

import dao.DAO;
import data.MLBBattingStats;
import init.MLBStatsDatabase;

public class GetPlayerStatsAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	Map<String, Object> userSession;
	String playerName;

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
		
		List<MLBBattingStats>mlbBattingStatsList = DAO.getMLBBattingStatsList(playerName);
		context.put("mlbBattingStatsList", mlbBattingStatsList);
	    stack.push(context);
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
