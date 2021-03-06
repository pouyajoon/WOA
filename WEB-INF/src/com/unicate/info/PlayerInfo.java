package com.unicate.info;

import vnd.players.Player;
import vnd.woaobject.children.Queen;

public class PlayerInfo 
{
	private Player player = null;
	public ColonyInfo ci = null;

	public PlayerInfo(Player _player)
	{
		player = _player;
		for (Queen q : player.queens)
		{
			ci = q.getColony().getColonyInfo();
			System.out.println("exporting colony info:" + ci.toString());
		}
	}
}

