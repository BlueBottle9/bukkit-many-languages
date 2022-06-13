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
	 * @param plugin Use the plugin to build a Lang. -ʹ��plugin������Lang
	 * */
		public Lang(Plugin plugin) {
				this.plugin = plugin;
		}
	
		/**
		 * Copy language files form plugin.jar/lang to datafolder/lang -�Ӳ��jar�µ�langĿ¼���������ļ�������Ŀ¼
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
				 * - ������Ŀ¼���������ļ����ڴ�,����������ȸ��������ļ�������Ŀ¼
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
				 * - ��һ��locale��ȡ�����ַ���
				 * 
				 * @param locale Taget language -Ŀ������
				 * @param key language String key -Ŀ���ַ���key
				 * */
				
				public String get(Locale locale,String key) {
					return Data.get(locale.toLanguageTag().replaceAll("-", "_").toLowerCase()).getString(key);
				}
				
				/**
				 * get language String from a locale.
				 * - ��һ��locale��ȡ�����ַ���
				 * 
				 * @param locale Taget language -Ŀ������
				 * @param key language String key -Ŀ���ַ���key
				 * */
				public String get(String locale,String key) {
					return Data.get(locale).getString(key);
				}
				
				/**
				 * get language String from a player.
				 * - ��һ����һ�ȡ�����ַ���
				 * 
				 * @param player Player that get language -�õ����Ե����
				 * @param key language String key -Ŀ���ַ���key
				 * */
				public String get(Player player,String key) {
					String locale = player.locale().toLanguageTag().toLowerCase().replaceAll("-", "_");
					if (Data.get(locale) == null) {
						return Data.get(deafultLang).getString(key);
					}
					return Data.get(locale).getString(key);
				}
				
				/**
				 * Set default language -�趨Ĭ������
				 * 
				 * @param locale Target language. -Ŀ������
				 * */
				public void setDefaultLang(String locale) {
					this.deafultLang = locale;
				}
				
				/**
				 * Get the default language
				 *- �õ�Ĭ������
				 *
				 * @return Default language -Ĭ������ 
				 * */
				public String getDefaultLang() {
					return this.deafultLang;
				}
				
				/**
				 * Broadcast message with language.
				 * - �������ļ��㲥��Ϣ
				 * 
				 * @param server Broadcast server -Ҫ�㲥�ķ�����
				 * @param key Language String key. -�����ַ�����key
				 * */
				public void broadcastMessage(Server server,String key) {
						for (Player p:server.getOnlinePlayers()) {
							p.sendMessage(get(p, key));
						}
				}
				/**
				 * Broadcast message with language.
				 * - �������ļ��㲥��Ϣ
				 * 
				 * @param world Broadcast world -Ҫ�㲥������
				 * @param key  Language String key. -�����ַ�����key
				 * */
				public void broadcastMessage(World world,String key) {
					for (Player p:world.getPlayers()) {
						p.sendMessage(get(p, key));
					}
				}
				/**
				 * Broadcast message with language.
				 * - �������ļ��㲥��Ϣ
				 * 
				 * @param players Players to broadcast  -Ҫ�㲥������б�
				 * @param key Language String key. -�����ַ�����key
				 * */
				public void broadcastMessage(List<Player> players,String key) {
					for (Player p:players) {
						p.sendMessage(get(p, key));
					}
			}
}
