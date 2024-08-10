package com.mediusecho.particlehats.listeners;

import com.mediusecho.particlehats.ParticleHats;
import com.mediusecho.particlehats.database.type.DatabaseType;
import com.mediusecho.particlehats.database.type.yaml.YamlDatabase;
import com.mediusecho.particlehats.hooks.VanishHook;
import com.mediusecho.particlehats.managers.SettingsManager;
import com.mediusecho.particlehats.particles.Hat;
import com.mediusecho.particlehats.particles.HatReference;
import com.mediusecho.particlehats.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConnectionListener implements Listener {

	private final ParticleHats core;
	
	public ConnectionListener (final ParticleHats core)
	{
		this.core = core;
		core.getServer().getPluginManager().registerEvents(this, core);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin (PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		UUID id = event.getPlayer().getUniqueId();
		PlayerState playerState = core.getNewPlayerState(player);
		
		// Load equipped hats
		core.getDatabase().loadPlayerEquippedHats(id, (loadedHats) ->
		{
			if (loadedHats instanceof List)
			{
				@SuppressWarnings("unchecked")
				List<Hat> hats = (ArrayList<Hat>)loadedHats;				
				for (Hat hat : hats) {
					playerState.addHat(hat);
				}
			}
		});
		
		// Load purchased hats
		core.getDatabase().loadPlayerPurchasedHats(id, (purchasedHats) ->
		{
			if (purchasedHats instanceof List)
			{
				@SuppressWarnings("unchecked")
				List<HatReference> hats = (ArrayList<HatReference>)purchasedHats;
				for (HatReference hat : hats) {
					playerState.addPurchasedHat(hat);
				}
			}
		});
		
		// Load legacy purchased hats
		if (SettingsManager.CHECK_AGAINST_LEGACY_PURCHASES.getBoolean() && core.getDatabaseType() == DatabaseType.YAML)
		{
			YamlDatabase database = (YamlDatabase)core.getDatabase();
			database.loadPlayerLegacyPurchasedHats(id, (legacyHats) ->
			{
				if (legacyHats instanceof List)
				{
					@SuppressWarnings("unchecked")
					List<String> hats = (ArrayList<String>)legacyHats;
					for (String path : hats) {
						playerState.addLegacyPurchasedHat(path);
					}
				}
			}); 
		}

		VanishHook vanishHook = core.getHookManager().getVanishHook();
		if (vanishHook != null)
		{
			if (vanishHook.isVanished(player))
			{
				playerState.getActiveHats().forEach(hat -> hat.setVanished(true));
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		UUID id = event.getPlayer().getUniqueId();
		PlayerState playerState = core.getPlayerState(player);
		List<Hat> activeHats = playerState.getActiveHats();
		
		core.getDatabase().savePlayerEquippedHats(id, new ArrayList<Hat>(activeHats));
		playerState.clearActiveHats();
		
		core.removePlayerState(id);
	}
}
