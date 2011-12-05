package ch.frickler.jass.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

	private String name;
	private Player owner;
	private List<Player> players = new ArrayList<Player>();
	private int winPoints;
	private long id;

	public Game(long id, String name, Player owner, int winPoints) {
		this.name = name;
		this.owner = owner;
		this.winPoints = winPoints;
	}

	public String getName() {
		return name;
	}

	public void addPlayer(Player p) {
		if (players.size() > 3)
			throw new RuntimeException("wtf? more than 4 players?");
		else if (!players.contains(p))
			players.add(p);
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}

	public long getId() {
		return id;
	}

	public Player getOwner() {
		return owner;
	}

	public boolean isAcceptingPlayers() {
		return players.size() < 4;
	}
}
