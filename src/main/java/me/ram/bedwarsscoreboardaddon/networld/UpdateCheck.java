package me.ram.bedwarsscoreboardaddon.networld;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import me.ram.bedwarsscoreboardaddon.Main;
import me.ram.bedwarsscoreboardaddon.config.Config;
import me.ram.bedwarsscoreboardaddon.utils.URLUtil;
import me.ram.bedwarsscoreboardaddon.utils.UnicodeUtil;

public class UpdateCheck implements Listener {

	private static String version;
	private static String post;
	private static String[] update;

	public UpdateCheck() {
		if (Config.update_check) {
			new BukkitRunnable() {
				@Override
				public void run() {
					String document = URLUtil.getDocumentAt(
							"https://raw.githubusercontent.com/13527698822/BedwarsScoreBoardAddon/master/plugin.yml");
					if (document != null && !document.equals("")) {
						String[] info = document.split(",");
						version = info[0];
						post = info[1];
						update = info[2].split(";");
					}
					new BukkitRunnable() {
						@Override
						public void run() {
							if (Config.update_check && version != null && post != null && update != null
									&& !version.equals(Main.getVersion())) {
								sendInfo(Bukkit.getConsoleSender(), version, post, update);
							}
						}
					}.runTaskLater(Main.getInstance(), 100L);
				}
			}.runTaskLaterAsynchronously(Main.getInstance(), 5L);
		}
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		if (Config.update_check && version != null && post != null && update != null) {
			if (e.getPlayer().hasPermission("bedwarsscoreboardaddon.updatecheck")
					&& !version.equals(Main.getVersion())) {
				sendInfo(e.getPlayer(), version, post, update);
			}
		}
	}

	public static void upCheck(CommandSender sender) {
		sender.sendMessage("��b��lBWSBA ��f>> ��a���ڼ�����...");
		new BukkitRunnable() {
			@Override
			public void run() {
				String document = URLUtil.getDocumentAt(
						"https://raw.githubusercontent.com/13527698822/BedwarsScoreBoardAddon/master/plugin.yml");
				if (document != null && !document.equals("")) {
					String[] info = document.split(",");
					if (info[0].equals(Main.getVersion())) {
						sender.sendMessage("��b��lBWSBA ��f>> ��a��ʹ�õ��������°汾��");
					} else {
						sendInfo(sender, info[0], info[1], info[2].split(";"));
					}
				} else {
					sender.sendMessage("��b��lBWSBA ��f>> ��c������ʧ�ܣ�����������������ӣ�");
				}
			}
		}.runTaskAsynchronously(Main.getInstance());
	}

	private static void sendInfo(CommandSender sender, String ver, String p, String[] u) {
		new BukkitRunnable() {
			@Override
			public void run() {
				sender.sendMessage("��f===========================================================");
				sender.sendMessage("");
				sender.sendMessage("                  ��aBedwarsScoreBoardAddon");
				sender.sendMessage("");
				sender.sendMessage("  ��e��⵽һ�����õİ汾���£�");
				sender.sendMessage("");
				sender.sendMessage("  ��f��ǰ�汾: ��a" + Main.getVersion());
				sender.sendMessage("  ��f���°汾: ��a" + ver);
				sender.sendMessage("  ��f��������: ");
				for (int i = 0; i < u.length; i++) {
					sender.sendMessage("      ��f" + (i + 1) + ".��e" + UnicodeUtil.unicodeToCn(u[i]));
				}
				sender.sendMessage("  ��f���µ�ַ: ��b��n" + UnicodeUtil.unicodeToCn(p));
				sender.sendMessage("");
				sender.sendMessage("��f===========================================================");
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 5L);
	}
}
