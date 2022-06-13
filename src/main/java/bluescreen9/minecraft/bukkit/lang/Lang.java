package bluescreen9.minecraft.bukkit.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.alibaba.fastjson.JSONObject;

public class Lang {
	private String deafultLang = "en_us";
	private Plugin plugin;
	
	/**
	 * @param plugin Use the plugin to build a Lang. -使用plugin来构造Lang
	 * */
		public Lang(Plugin plugin) {
				this.plugin = plugin;
		}
	
		/**
		 * Copy language files form plugin.jar/lang to datafolder/lang -从插件jar下的lang目录复制语言文件到数据目录
		 * */
				public void copyDeafultLangFile() {
							try {
								File langFloder = new File(plugin.getDataFolder(), "lang");
								if (!langFloder.exists()) {
									langFloder.mkdirs();
								}
								if (!langFloder.isDirectory()) {
									langFloder.delete();
									langFloder.mkdirs();
								}
								
								for (Object object:LocaleUtils.availableLocaleSet()) {
									Locale locale = (Locale) object;
									String fileName = locale.toLanguageTag().toLowerCase().replaceAll("-", "_") + ".json";
									InputStream in = plugin.getResource("lang/" + fileName);
									if (in == null) {
										continue;
									}
									File file = new File(langFloder, fileName);
									
									if (file.exists()) {
										if (file.isFile()) {
											continue;
										}
									}
									
									if (!file.exists()) {
										file.createNewFile();
									}
									if (!file.isFile()) {
										file.delete();
										file.createNewFile();
									}
									
									FileOutputStream out = new FileOutputStream(file);
									out.write(in.readAllBytes());
									out.flush();
									out.close();
									in.close();
								}
								
								
							} catch (Exception e) {
								e.printStackTrace();
							}
				}
				
				private HashMap<String, JSONObject> Data = new HashMap<String, JSONObject>();
				
				/**
				 * Load language files from datafolder,so you must copy language files first.
				 * - 从数据目录加载语言文件到内存,所以你必须先复制语言文件到数据目录
				 * */
				public void loadLanguages() {
						try {
							File langFloder = new File(plugin.getDataFolder(), "lang");
							for (File f:langFloder.listFiles()) {
								if (f.isFile() && f.getName().endsWith(".json")) {
										FileInputStream in = new FileInputStream(f);
										String data = new String(in.readAllBytes(),Charset.forName("utf-8"));
										Data.put(f.getName().replaceAll(".json", ""), JSONObject.parseObject(data));
										in.close();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
				
				/**
				 * get language String from a locale.
				 * - 从一个locale获取语言字符串
				 * 
				 * @param locale Taget language -目标语言
				 * @param key language String key -目标字符串key
				 * */
				
				public String get(Locale locale,String key) {
					return Data.get(locale.toLanguageTag().replaceAll("-", "_").toLowerCase()).getString(key);
				}
				
				/**
				 * get language String from a locale.
				 * - 从一个locale获取语言字符串
				 * 
				 * @param locale Taget language -目标语言
				 * @param key language String key -目标字符串key
				 * */
				public String get(String locale,String key) {
					return Data.get(locale).getString(key);
				}
				
				/**
				 * get language String from a player.
				 * - 从一个玩家获取语言字符串
				 * 
				 * @param player Player that get language -得到语言的玩家
				 * @param key language String key -目标字符串key
				 * */
				public String get(Player player,String key) {
					String locale = player.locale().toLanguageTag().toLowerCase().replaceAll("-", "_");
					if (Data.get(locale) == null) {
						return Data.get(deafultLang).getString(key);
					}
					return Data.get(locale).getString(key);
				}
				
				/**
				 * Set default language -设定默认语言
				 * 
				 * @param locale Target language. -目标语言
				 * */
				public void setDefaultLang(String locale) {
					this.deafultLang = locale;
				}
				
				/**
				 * Get the default language
				 *- 得到默认语言
				 *
				 * @return Default language -默认语言 
				 * */
				public String getDefaultLang() {
					return this.deafultLang;
				}
				
				/**
				 * Broadcast message with language.
				 * - 从语言文件广播消息
				 * 
				 * @param server Broadcast server -要广播的服务器
				 * @param key Language String key. -语言字符串的key
				 * */
				public void broadcastMessage(Server server,String key) {
						for (Player p:server.getOnlinePlayers()) {
							p.sendMessage(get(p, key));
						}
				}
				/**
				 * Broadcast message with language.
				 * - 从语言文件广播消息
				 * 
				 * @param world Broadcast world -要广播的世界
				 * @param key  Language String key. -语言字符串的key
				 * */
				public void broadcastMessage(World world,String key) {
					for (Player p:world.getPlayers()) {
						p.sendMessage(get(p, key));
					}
				}
				/**
				 * Broadcast message with language.
				 * - 从语言文件广播消息
				 * 
				 * @param players Players to broadcast  -要广播的玩家列表
				 * @param key Language String key. -语言字符串的key
				 * */
				public void broadcastMessage(List<Player> players,String key) {
					for (Player p:players) {
						p.sendMessage(get(p, key));
					}
			}
}
