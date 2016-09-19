package zgbzhyjy.sampsystem.ehcache;


import java.io.InputStream;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhCache {

	/**
	 * 缓存配置文件
	 */
	private static String CACHE_CONFIG_FILE = "/ehcache.xml";
	
	public static String CACHE_DIMS = "CACHE_DIMS";
	
	public static String CACHE_BUDGETS = "CACHE_BUDGETS";
	
	public static String CACHE_FACT_DIMS = "CACHE_FACT_DIMS";

	/**
	 * Ehanche的缓存管理
	 */
	private static CacheManager cacheManager = null;

	/**
	 * 设置缓存配置文件，最开始设置才有效果，一旦缓存加载则不能改变
	 * 
	 * @param cacheConfigFile
	 */
	public static void setCacheConfigFile(String cacheConfigFile) {
		CACHE_CONFIG_FILE = cacheConfigFile;
	}

	/**
	 * 按缺省配置创建缓存
	 * 
	 * @param cacheName
	 */
	public static void createCache(String cacheName) {
		getCacheManager().addCache(cacheName);
	}

	/**
	 * 添加缓存
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void put(String cacheName, String key, Object value) {
		Ehcache cache = getCacheManager().getEhcache(cacheName);
		cache.put(new Element(key, value));
	}

	/**
	 * 根据缓存名与key获取值
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static Object get(String cacheName, String key) {
		Ehcache cache = getCacheManager().getEhcache(cacheName);
		Element e = cache.get(key);
		return e == null ? null : e.getObjectValue();
	}

	/**
	 * 获取缓存名
	 * 
	 * @return
	 */
	public static String[] getCacheNames() {
		return getCacheManager().getCacheNames();
	}

	/**
	 * 获取缓存的Keys
	 * 
	 * @param cacheName
	 * @return
	 */
	public static Object getKeys(String cacheName,String key) {
		Ehcache cache = getCacheManager().getEhcache(cacheName);
		Element element = cache.get(key);
		if (null == element) {
			return null;
		}
		return (Object) element.getValue();
	}

	/**
	 * 清除所有
	 */
	public static void clearAll() {
		getCacheManager().clearAll();
	}

	/**
	 * 清空指定缓存
	 * 
	 * @param cacheName
	 */
	public static void clear(String cacheName) {
		getCacheManager().getCache(cacheName).removeAll();
	}

	/**
	 * 删除指定对象
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static boolean remove(String cacheName, String key) {
		return getCacheManager().getCache(cacheName).remove(key);
	}

	/**
	 * 获取缓存大小
	 * 
	 * @param cacheName
	 * @return
	 */
	public static int getSize(String cacheName) {
		return getCacheManager().getCache(cacheName).getSize();
	}

	/**
	 * 获取CacheManager
	 * 
	 * @return
	 */
	private static CacheManager getCacheManager() {
		if (cacheManager != null) {
			return cacheManager;
		}
		try {
			// 读取配置文件
			InputStream inputStream = EhCache.class.getClassLoader().getResourceAsStream(CACHE_CONFIG_FILE);
			cacheManager = CacheManager.create(inputStream);
		} catch (RuntimeException e) {
			throw e;
		}
		return cacheManager;
	}
}