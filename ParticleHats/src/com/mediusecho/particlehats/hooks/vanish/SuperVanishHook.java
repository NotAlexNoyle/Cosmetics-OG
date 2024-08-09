package com.mediusecho.particlehats.hooks.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.mediusecho.particlehats.ParticleHats;
import com.mediusecho.particlehats.hooks.VanishHook;
import com.mediusecho.particlehats.particles.Hat;
import com.mediusecho.particlehats.player.PlayerState;

import de.myzelyam.api.vanish.PlayerHideEvent;
import de.myzelyam.api.vanish.PlayerShowEvent;
import de.myzelyam.api.vanish.VanishAPI;

public class SuperVanishHook implements Listener, VanishHook {

	private final ParticleHats core;
	
	public SuperVanishHook (final ParticleHats core)
	{
		this.core = core;
		core.getServer().getPluginManager().registerEvents(this, core);
	}
	
	@Override
	public boolean isVanished(Player player) {
		return VanishAPI.isInvisible(player);
	}

	@Override
	public void unregister() 
	{
		PlayerHideEvent.getHandlerList().unregister(this);
		PlayerShowEvent.getHandlerList().unregister(this);
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerHide (PlayerHideEvent event)
	{
		if (!event.isCancelled())
		{
			PlayerState playerState = core.getPlayerState(event.getPlayer());
			for (Hat hat : playerState.getActiveHats()) {
				hat.setVanished(true);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerShow (PlayerShowEvent event)
	{
		if (!event.isCancelled())
		{
			PlayerState playerState = core.getPlayerState(event.getPlayer());
			for (Hat hat : playerState.getActiveHats())
			{
				hat.setVanished(false);
				if (hat.isVanished()) {
					hat.unequip(event.getPlayer());
				}
			}
		}
	}
}
