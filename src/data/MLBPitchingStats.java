package data;

public class MLBPitchingStats {
	private int mlbPlayerId;
	private String playerName;
	private String pos;
	private String teamName;
	private int year;
	private double inningsPitched;
	private int walks;
	private int strikeouts;
	private int runsAllowed;
	private int earnedRunsAllowed;
	private int homeRunsAllowed;
	private int stolenBasesAllowed;
	private int hitBatters;
	private int hitsAllowed;
	private int holds;
	private int saves;
	private int blownSaves;
	private int gamesStarted;
	private int balks;
	private int wildPitches;
	private int sacrificeFlies;
	private int battersFaced;
	private int wins;
	private int losses;
	
	public MLBPitchingStats() {
	}
	
	public MLBPitchingStats(int mlbPlayerId, String playerName, String pos, String teamName, int year, double inningsPitched, int earnedRunsAllowed, 
			int runsAllowed, int walks, int strikeouts, int homeRunsAllowed, int stolenBasesAllowed, int hitBatters, int hitsAllowed, int holds, int saves, int blownSaves, int gamesStarted, int balks,
			int wildPitches, int sacrificeFlies, int battersFaced, int wins, int losses) {
		this.mlbPlayerId = mlbPlayerId;
		this.pos = pos;
		this.playerName = playerName;
		this.teamName = teamName;
		this.year = year;
		this.inningsPitched = inningsPitched;
		this.earnedRunsAllowed = earnedRunsAllowed;
		this.runsAllowed = runsAllowed;
		this.walks = walks;
		this.strikeouts = strikeouts;
		this.homeRunsAllowed = homeRunsAllowed;
		this.stolenBasesAllowed = stolenBasesAllowed;
		this.hitBatters = hitBatters;
		this.hitsAllowed = hitsAllowed;
		this.holds = holds;
		this.saves = saves;
		this.blownSaves = blownSaves;
		this.gamesStarted = gamesStarted;
		this.balks = balks;
		this.wildPitches = wildPitches;
		this.sacrificeFlies = sacrificeFlies;
		this.battersFaced = battersFaced;
		this.wins = wins;
		this.losses = losses;
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

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
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

	public double getInningsPitched() {
		return inningsPitched;
	}

	public void setInningsPitched(double inningsPitched) {
		this.inningsPitched = inningsPitched;
	}

	public int getRunsAllowed() {
		return runsAllowed;
	}

	public void setRunsAllowed(int runsAllowed) {
		this.runsAllowed = runsAllowed;
	}

	public int getEarnedRunsAllowed() {
		return earnedRunsAllowed;
	}

	public void setEarnedRunsAllowed(int earnedRunsAllowed) {
		this.earnedRunsAllowed = earnedRunsAllowed;
	}

	public double getEarnedRunAverage() {
		return getOuts() > 0 ? (double)(earnedRunsAllowed*9)/(getOuts()/3.0) : 0.0;
	}

	public int getWalks() {
		return walks;
	}

	public void setWalks(int walks) {
		this.walks = walks;
	}

	public int getStrikeouts() {
		return strikeouts;
	}

	public void setStrikeouts(int strikeouts) {
		this.strikeouts = strikeouts;
	}

	public int getHomeRunsAllowed() {
		return homeRunsAllowed;
	}

	public void setHomeRunsAllowed(int homeRunsAllowed) {
		this.homeRunsAllowed = homeRunsAllowed;
	}

	public int getStolenBasesAllowed() {
		return stolenBasesAllowed;
	}

	public void setStolenBasesAllowed(int stolenBasesAllowed) {
		this.stolenBasesAllowed = stolenBasesAllowed;
	}

	public int getHitBatters() {
		return hitBatters;
	}

	public void setHitBatters(int hitBatters) {
		this.hitBatters = hitBatters;
	}

	public int getHitsAllowed() {
		return hitsAllowed;
	}

	public void setHitsAllowed(int hitsAllowed) {
		this.hitsAllowed = hitsAllowed;
	}

	public int getHolds() {
		return holds;
	}

	public void setHolds(int holds) {
		this.holds = holds;
	}

	public int getSaves() {
		return saves;
	}

	public void setSaves(int saves) {
		this.saves = saves;
	}

	public int getBlownSaves() {
		return blownSaves;
	}

	public void setBlownSaves(int blownSaves) {
		this.blownSaves = blownSaves;
	}

	public int getGamesStarted() {
		return gamesStarted;
	}

	public void setGamesStarted(int gamesStarted) {
		this.gamesStarted = gamesStarted;
	}

	public int getBalks() {
		return balks;
	}

	public void setBalks(int balks) {
		this.balks = balks;
	}

	public int getWildPitches() {
		return wildPitches;
	}

	public void setWildPitches(int wildPitches) {
		this.wildPitches = wildPitches;
	}
	
	public int getSacrificeFlies() {
		return sacrificeFlies;
	}

	public void setSacrificeFlies(int sacrificeFlies) {
		this.sacrificeFlies = sacrificeFlies;
	}

	public int getBattersFaced() {
		return battersFaced;
	}

	public void setBattersFaced(int battersFaced) {
		this.battersFaced = battersFaced;
	}
	
	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}
	
	public int getOuts() {
		int outs = 0;
		String iPString = Double.toString(inningsPitched);
		if (iPString != null && iPString.length() > 0 && iPString.contains(".")) {
			outs = (Integer.parseInt(iPString.split("\\.")[0]) * 3) + Integer.parseInt(iPString.split("\\.")[1]);
		}
		return outs;
	}
	
	public String toString() {
		return "playerName=" + playerName + " teamName=" + teamName;
	}
	
}
