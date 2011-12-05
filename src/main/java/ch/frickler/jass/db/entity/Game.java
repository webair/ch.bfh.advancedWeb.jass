package ch.frickler.jass.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: Game
 * 
 */
@Entity
@Table(name = "GAME")
public class Game implements Serializable {

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	private static final long serialVersionUID = 1L;
	public static final int MAXUSER = 4;
	public static final int TEAMAMOUNT = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_GAME")
	private long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_OWNER")
	private User owner;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TEAM1", nullable = true)
	private Team team1;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TEAM2", nullable = true)
	private Team team2;

	@OneToOne
	@JoinColumn(name = "CURRENT_ROUND")
	private Round currentRound;

	@Column(name = "WINPOINTS", nullable = false)
	private Integer winPoints;

	@Column(name = "CALLER", nullable = false)
	private int callerId;

	@Enumerated(EnumType.STRING)
	@Column(name = "GameState", nullable = false)
	private GameState gameState = GameState.WaitForPlayers;

	public static enum GameState {
		WaitForPlayers, Play, Ansage, AnsageGschobe, Terminated, RediForPlay, WaitForCards
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", nullable = false)
	private Date startDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USER", nullable = true)
	private User nextAnnouncer;

	public Game() {
		super();
	}

	public Game(String name, User owner, Team team1, Team team2, int winPoints) {
		super();
		this.name = name;
		this.owner = owner;
		this.team1 = team1;
		this.team2 = team2;
		this.nextAnnouncer = owner;
		this.winPoints = winPoints;
		this.startDate = new Date();
		this.gameState = Game.GameState.WaitForPlayers;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	public Integer getWinPoints() {
		return winPoints;
	}

	public void setWinPoints(Integer winPoints) {
		this.winPoints = winPoints;
	}

	public Round getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(Round currentRound) {
		this.currentRound = currentRound;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public User getNextAnnouncer() {
		return nextAnnouncer;
	}

	public void setNextAnnouncer(User nextAnnouncer) {
		this.nextAnnouncer = nextAnnouncer;
	}

	public int getCallerId() {
		return callerId;
	}

	public void setCallerId(int callerId) {
		this.callerId = callerId;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Team getTeam(int i) {
		return getTeams().get(i);
	}

	public int getTeamAmount() {
		return getTeams().size();
	}

	public void addTeam(Team team) {
		if (team1 == null)
			setTeam1(team);
		else if (team2 == null)
			setTeam2(team);
		else
			throw new RuntimeException("not possible to add third team");
	}

	public List<Team> getTeams() {

		List<Team> teams = new ArrayList<Team>();
		if (team1 != null)
			teams.add(team1);
		if (team2 != null)
			teams.add(team2);
		return teams;
	}

	// TODO see later
	public boolean isAcceptingPlayers() {
		return true;
	}

}
