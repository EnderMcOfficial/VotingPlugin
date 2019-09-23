package com.Ben12345rocks.VotingPlugin.Listeners;

import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.Ben12345rocks.AdvancedCore.Util.Misc.PlayerUtils;
import com.Ben12345rocks.VotingPlugin.Main;
import com.Ben12345rocks.VotingPlugin.Signs.SignHandler;
import com.Ben12345rocks.VotingPlugin.UserManager.UserManager;

// TODO: Auto-generated Javadoc
/**
 * The Class PlayerInteract.
 */
public class PlayerInteract implements Listener {

	/** The plugin. */
	private static Main plugin;

	/**
	 * Instantiates a new player interact.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public PlayerInteract(Main plugin) {
		PlayerInteract.plugin = plugin;
	}

	/**
	 * On player interact.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			// plugin.debug("Checking for sign click");
			if (event.getClickedBlock().getState() instanceof Sign) {
				// plugin.debug(player.getName() + " right clicked a sign");
				for (SignHandler sign : plugin.getSigns()) {
					if (sign.isLocationSame(event.getClickedBlock().getLocation())) {
						// plugin.debug(player.getName() +
						// " right clicked a top voter sign, sending message");
						UserManager.getInstance().getVotingPluginUser(player).sendMessage(sign.getRightClickMessage());

						if (!sign.isSkullSet()) {
							if (PlayerUtils.getInstance().hasServerPermission(event.getPlayer().getName(),
									"VotingPlugin.Sign.Create")
									|| PlayerUtils.getInstance().hasServerPermission(event.getPlayer().getName(),
											"VotingPlugin.Admin")) {
								player.sendMessage("Skull not set, click to set skull");
								PlayerUtils.getInstance().setPlayerMeta(player, "skullset", sign.getSign());
							}
						}
					}
				}

			} else if (event.getClickedBlock().getState() instanceof Skull) {
				Object ob = PlayerUtils.getInstance().getPlayerMeta(player, "skullset");
				if (ob != null) {
					String sign1 = (String) ob;
					for (SignHandler sign : plugin.getSigns()) {
						if (sign.getSign().equals(sign1)) {
							sign.setSkullLocation(event.getClickedBlock().getLocation());
							sign.storeSign();
							sign.checkSkulls();
							player.sendMessage("Skull set");
						}
					}
					PlayerUtils.getInstance().setPlayerMeta(player, "skullset", null);
				}
			}
		}
	}
}
