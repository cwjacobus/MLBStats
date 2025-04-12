package actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

import dao.DAO;
import data.MLBSeasonLeaderStats;

public class GetSeasonLeaderStatsAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	Map<String, Object> userSession;
	Integer year;
	String statType;
	String league;

	//@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		ValueStack stack = ActionContext.getContext().getValueStack();
	    Map<String, Object> context = new HashMap<String, Object>();
	    if (userSession == null || userSession.size() == 0) {
			context.put("errorMsg", "Session has expired!");
			stack.push(context);
			return "error";
	    }
	    
	    List<MLBSeasonLeaderStats> mlbSeasonLeaderStatsList;
	    mlbSeasonLeaderStatsList = DAO.getSeasonLeaderMLBStatsListByYear(year, statType, 50);
	    context.put("mlbSeasonLeaderStatsList", mlbSeasonLeaderStatsList);
	    stack.push(context);
	    return "success";
	}
	
	@Override
    public void setSession(Map<String, Object> sessionMap) {
        this.userSession = sessionMap;
    }

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getStatType() {
		return statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}


}
