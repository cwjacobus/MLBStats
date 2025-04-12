package data;

public class MLBSeasonLeaderStats {
	private int mlbPlayerId;
	private String playerName;
	private String teamName;
	private int year;
	private String statType;
	private Integer intStat;
	private Double doubleStat;

	public MLBSeasonLeaderStats(int mlbPlayerId, String playerName, String teamName, int year, String statType, Integer intStat, Double doubleStat) {
		this.mlbPlayerId = mlbPlayerId;
		this.playerName = playerName;
		this.teamName = teamName;
		this.year = year;
		this.statType = statType;
		this.intStat = intStat;
		this.doubleStat = doubleStat;
	}
	
	public int getMlbPlayerId() {
		return mlbPlayerId;
	}

	public void setMlbPlayerId(int mlbPlayerId) {
		this.mlbPlayerId = mlbPlayerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getStatType() {
		return statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}

	public Integer getIntStat() {
		return intStat;
	}

	public void setIntStat(Integer intStat) {
		this.intStat = intStat;
	}

	public Double getDoubleStat() {
		return doubleStat;
	}

	public void setDoubleStat(Double doubleStat) {
		this.doubleStat = doubleStat;
	}

	public String toString() {
		return "playerName=" + playerName + " teamName=" + teamName + " statType=" + statType + " intStat=" + intStat + " doubleStat=" + doubleStat;
	}

}
