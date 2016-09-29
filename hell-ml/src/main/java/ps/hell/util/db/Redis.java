package ps.hell.util.db;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

public class Redis implements CursorInter {
	/**
	 * redis 线程池
	 */
	public JedisPool pool = null;
	public String ip = null;
	public int port = 6379;

	public String keyName = null;

	public Redis findCursor(String collection) {
		keyName = collection;
		return this;
	}

	@Override
	public String nextString() {
		// TODO Auto-generated method stub
		String value = null;
		while (true) {
			try {
				value = lpop(keyName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (value != null) {
				break;
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}

	@Override
	public String getDB() {
		// TODO Auto-generated method stub
		return "Redis";
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return this.ip + ":" + port + "/table=" + keyName;
	}
	

	@Override
	public void insertString(String insertString) {
		// TODO Auto-generated method stub
		try {
			this.lpush(keyName,insertString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param maxActive
	 *            最大活跃数
	 * @param minIdle
	 *            初始化最小连接数 要小于maxActive
	 * @param PoolTimeOut 超时时间
	 */
	public Redis(String ip, int maxActive, int minIdle, int timeout,
			int PoolTimeOut) {
		if (minIdle > maxActive) {
			System.exit(1);
		}
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(maxActive);
		config.setMaxIdle(minIdle);
		config.setMaxWait(timeout);
		// 127.0.0.1:6379
		pool = new JedisPool(config, ip.split(":")[0], Integer.parseInt(ip
				.split(":")[1]), PoolTimeOut);

	}

	public Redis(String ip) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(1000);
		config.setMaxIdle(1000);
		config.setMaxWait(10000);
		// 127.0.0.1:6379
		pool = new JedisPool(config, ip.split(":")[0], Integer.parseInt(ip
				.split(":")[1]), 100000);
	}

	public Redis(String ip, int port) {
		this.ip = ip;
		this.port = port;
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(1000);
		config.setMaxIdle(1000);
		config.setMaxWait(10000);
		// 127.0.0.1:6379
		pool = new JedisPool(config, ip, port, 100000);
	}

	public String getInfo() throws Exception{
		Jedis jedis = pool.getResource();
		String set = null;
		try {
			set = jedis.info();
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return set;
	}
	
	public Set<String> keys() throws Exception {
		Jedis jedis = pool.getResource();
		Set<String> set = null;
		try {
			set = jedis.keys("*");
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return set;
	}

	public void del(String key) throws Exception {
		Jedis jedis = pool.getResource();

		try {
			jedis.del(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * set方法
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		try {
			jedis.set(key, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 设置value的同时设置留存时间
	 * 
	 * @param key
	 * @param seconds
	 * @param value
	 */
	public void setex(String key, Integer seconds, String value)
			throws Exception {

		// 获取
		Jedis jedis = pool.getResource();
		try {
			jedis.setex(key, seconds, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 设置key值如果key存在则不操作 否则赋值
	 * 
	 * @param key
	 * @param value
	 * @return 如果为1则成功，如果为0表示key存在，为失败
	 */
	public long setnx(String key, String value) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.setnx(key, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * get方法
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		String re = null;
		try {
			re = jedis.get(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		// 打印线程池状态
		return re;
	}

	/**
	 * 队列左侧进入
	 * 
	 * @param key
	 * @param value
	 * @return 1为正确
	 */
	public long lpush(String key, String value) throws Exception {

		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.lpush(key, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		// 打印线程池状态
		// pool.print();
		return re;
	}

	/**
	 * 获取key的长度
	 * 
	 * @param key
	 * @return
	 */
	public long strlen(String key) throws Exception {

		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.strlen(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 队列左侧压出
	 * 
	 * @param key
	 * @return
	 */
	public String lpop(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		String re = null;
		try {
			re = jedis.lpop(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 右侧压入队列
	 * 
	 * @param key
	 * @param value
	 * @return 1为正确
	 */
	public long rpush(String key, String value) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.rpush(key, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 右侧压出队里
	 * 
	 * @param key
	 * @return
	 */
	public String rpop(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		String re = null;
		try {
			re = jedis.rpop(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 元素集合添加
	 * 
	 * @param key
	 * @param value
	 * @return 1为成功
	 */
	public long sadd(String key, String value) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.sadd(key, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 元素集合获取
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> smembers(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		Set<String> re = null;
		try {
			re = jedis.smembers(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 
	 * @param key1
	 * @param key2
	 *            key1 存在 但key2不存在 如果key1 和key2在一个片中则有效 否则任意一个不再一个片中，则第二个为nil
	 * @return
	 */
	public Set<String> sdiff(String key1, String key2) throws Exception {

		// 获取
		Jedis jedis = pool.getResource();
		Set<String> re = null;
		try {
			re = jedis.sdiff(key1, key2);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 获取元素集合大小
	 * 
	 * @param key
	 * @return
	 */
	public long scard(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.scard(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 不存在方法 key序列化 key可以是任意类型
	 * 
	 * @param key
	 * @return
	 */
	// public String dump(String key)
	// {
	//
	// }
	/**
	 * key 为 key类型 获取从start开始到end结束的字节
	 */
	public String getrange(String key, long startOffset, long endOffset)
			throws Exception {
		Jedis jedis = pool.getResource();
		String re = null;
		try {
			re = jedis.getrange(key, startOffset, endOffset);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 设置key 为 value 并返回key的旧值，如果之前key不存在则会返回null
	 * 
	 * @param key
	 * @param value
	 * @return 空为null
	 */
	public String getSet(String key, String value) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		String re = null;
		try {
			re = jedis.getSet(key, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 是否存在key
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(String key) throws Exception {

		// 获取
		Jedis jedis = pool.getResource();
		boolean re = false;
		try {
			re = jedis.exists(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 给key设置留存时间
	 * 
	 * @param key
	 * @param seconds
	 *            秒
	 * @return 返回1为成功
	 */
	public long expire(String key, int seconds) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.expire(key, seconds);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 获取留存时间 如果返回为-1则表示已经失效 如果返回为正整数则表示 还剩下的留存时间 秒
	 * 
	 * @param key
	 * @return
	 */
	public long ttl(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.ttl(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 移除key的留存时间，并将key设为持久化保存
	 * 
	 * @param key
	 * @return 1表示移除成功
	 */
	public long persist(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.persist(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 设置留存时间 的时间戳
	 * 
	 * @param key
	 * @param unixTime
	 * @return
	 */
	public long expireat(String key, long unixTime) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.expireAt(key, unixTime);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 获取key的类型
	 * 
	 * @param key
	 * @return
	 */
	public String type(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		String re = null;
		try {
			re = jedis.type(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 返回key的long值-1的结果 如果key不存在则 value-1 为 -1 如果不为value不为 long则会报错
	 * 
	 * @param key
	 * @return
	 */
	public long decr(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.decr(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 对value，long值-一个integer值 返回最终值
	 * 
	 * @param key
	 * @param integer
	 * @return
	 */
	public long decrby(String key, Integer integer) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.decrBy(key, integer);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 返回key的long值+1的结果 如果key不存在则 value+1 为 1 如果不为value不为 long则会报错
	 * 
	 * @param key
	 * @return
	 */
	public long incr(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.incr(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 对value，long值+一个integer值 返回最终值
	 * 
	 * @param key
	 * @param integer
	 * @return
	 */
	public long incrby(String key, Integer integer) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.incrBy(key, integer);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 对key中value尾部添加value 返回长度
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long append(String key, String value) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.append(key, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 删除hash结构的field
	 * 
	 * @param key
	 * @param field
	 * @return 1为成功 0 失败也就是 不存在该field
	 */
	public long hdel(String key, String field) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.hdel(key, field);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * hash结构是否存在field
	 * 
	 * @param key
	 * @param field
	 * @return true 有 否则为否
	 */
	public boolean hexists(String key, String field) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		boolean re = false;
		try {
			re = jedis.hexists(key, field);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * hash结构获取field的值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		String re = null;
		try {
			re = jedis.hget(key, field);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * hash结构 返回为field对应的value
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		Map<String, String> re = null;
		try {
			re = jedis.hgetAll(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * hash结构 对field中value添加值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return 1成功，0失败，或者异常（key中field原本的value不能被转化为long）
	 */
	public long hincrby(String key, String field, long value) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.hincrBy(key, field, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * hash 结构 获取所有的field
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		Set<String> re = null;
		try {
			re = jedis.hkeys(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * hash 结构 获取field的长度
	 * 
	 * @param key
	 * @return
	 */
	public long hlen(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.hlen(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * hash结构 获取 对应的多个fields的值
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<String> hmget(String key, String... fields) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		List<String> re = null;
		try {
			re = jedis.hmget(key, fields);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * hash 结构 对多个fields 赋值
	 * 
	 * @param key
	 * @param hash
	 */
	public void hmset(String key, Map<String, String> hash) throws Exception {

		// 获取
		Jedis jedis = pool.getResource();
		try {
			jedis.hmset(key, hash);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * hash 结构 设置field的value
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return 1 成功 0 失败
	 */
	public long hset(String key, String field, String value) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.hset(key, field, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * hash 结构 设置field的value当且仅当field是返回1成功 否则不修改原来field的值 返回0
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public long hsetnx(String key, String field, String value) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.hset(key, field, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * hash 结构 所有fields下的values
	 * 
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		List<String> re = null;
		try {
			re = jedis.hvals(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 从左侧开始，对于list 找到地index个值并返回 如果超出则返回为null
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public String lindex(String key, long index) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		String re = null;
		try {
			re = jedis.lindex(key, index);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 获取list的长度
	 * 
	 * @param key
	 * @return
	 */
	public long llen(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.llen(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 队列获取内容
	 * 
	 * @param key
	 * @param start
	 *            开始index
	 * @param end
	 *            结束end
	 * @return
	 */
	public List<String> lrange(String key, long start, long end)
			throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		List<String> re = null;
		try {
			re = jedis.lrange(key, start, end);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 移除list中 和value相同的值
	 * 
	 * @param key
	 * @param count
	 *            >0 从表头到表位搜索 移除count个 <0从表尾 向表头搜索 移除count个 =0 移除所有和value相同的值
	 * @param value
	 * @return
	 */
	public long lrem(String key, long count, String value) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.lrem(key, count, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 对队列中第index个元素修改
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	public void lset(String key, long index, String value) throws Exception {

		// 获取
		Jedis jedis = pool.getResource();
		try {
			jedis.lset(key, index, value);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 对队列中只保留从左侧开始从 start到end之间的数据
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public void ltrim(String key, long start, long end) throws Exception {

		// 获取
		Jedis jedis = pool.getResource();
		try {
			jedis.ltrim(key, start, end);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 将srckey 中的右侧第一个添加入dstkey 左侧第一个
	 * 
	 * @param srckey
	 * @param dstkey
	 */
	public void rpoplpush(String srckey, String dstkey) throws Exception {

		// 获取
		Jedis jedis = pool.getResource();
		try {
			jedis.rpoplpush(srckey, dstkey);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 获取 keys中第一个和第二个中 第一个存在第二个不存在的数据并存储到dstkey（覆盖）
	 * 
	 * @param dstkey
	 * @param keys
	 * @return 1成功 0 失败
	 */
	public long sdiffStorage(String dstkey, String... keys) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.sdiffstore(dstkey, keys);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 返回交集
	 * 
	 * @param keys
	 * @return
	 */
	public Set<String> sinter(String... keys) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		Set<String> re = null;
		try {
			re = jedis.sinter(keys);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 获取 交集并存储到dstkey中（覆盖）
	 * 
	 * @param dstkey
	 * @param keys
	 * @return
	 */
	public long sinterStorage(String dstkey, String... keys) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.sinterstore(dstkey, keys);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 判断集合key中是否存在member
	 * 
	 * @param key
	 * @param member
	 * @return 1存在 0 不存在
	 */
	public boolean sismember(String key, String member) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		boolean re = false;
		try {
			re = jedis.sismember(key, member);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 将srckey中的member移动到dstkey中
	 * 
	 * @param srckey
	 * @param dstkey
	 * @param member
	 * @return 1 成功 0 失败
	 */
	public long smove(String srckey, String dstkey, String member)
			throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.smove(srckey, dstkey, member);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 从集合key中随机获取一个元素
	 * 
	 * @param key
	 * @return 返回元素
	 */
	public String srandommember(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		String re = null;
		try {
			re = jedis.srandmember(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 移除集合key中的一个随机元素
	 * 
	 * @param key
	 * @return 返回元素
	 */
	public String spop(String key) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		String re = null;
		try {
			re = jedis.spop(key);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 移除集合key中的member元素
	 * 
	 * @param key
	 * @param member
	 */
	public void srem(String key, String member) throws Exception {

		// 获取
		Jedis jedis = pool.getResource();
		try {
			jedis.srem(key, member);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 获取集合的并集
	 * 
	 * @param keys
	 * @return
	 */
	public Set<String> sunion(String... keys) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		Set<String> re = null;
		try {
			re = jedis.sunion(keys);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 获取集合的并集 存入dstkey中
	 * 
	 * @param keys
	 * @return 1成功 0 失败(不存在集合)
	 */
	public long sunionStorage(String dstkey, String... keys) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.sunionstore(dstkey, keys);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 对带有排序的value添加 zset结构
	 * 
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public long zadd(String key, double score, String member) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.zadd(key, score, member);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * zset结构 获取在min和max之间的所有值的数量
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public long zcount(String key, double min, double max) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.zcount(key, min, max);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * zset结构 对member添加score 并返回修改后的值
	 * 
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public double zincrby(String key, double score, String member)
			throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		double re = 0.0;
		try {
			re = jedis.zincrby(key, score, member);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * zset结构 返回从start到end之间的数据 value值不带评分
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public Set<String> zrange(String key, int start, int end) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		Set<String> re = null;
		try {
			re = jedis.zrange(key, start, end);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * zset结构 返回评分在start和end之间的数据集合
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrangeByScore(String key, double start, double end)
			throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		Set<String> re = null;
		try {
			re = jedis.zrangeByScore(key, start, end);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * zset 返回序号从start到end之间de 带有评分的数据集合
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<Tuple> zrangeWithScore(String key, int start, int end)
			throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		Set<Tuple> re = null;
		try {
			re = jedis.zrangeWithScores(key, start, end);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 返回 zset中元素member的从大到小的排名
	 * 
	 * @param keys
	 * @param member
	 */
	public long zrank(String key, String member) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.zrank(key, member);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 返回 zset中元素member的从小到大的排名
	 * 
	 * @param key
	 * @param member
	 */
	public long zrevRank(String key, String member) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.zrevrank(key, member);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 移除zset中的member元素
	 * 
	 * @param key
	 * @param member
	 * @return 1成功 0 失败（不存在member）
	 */
	public long zrem(String key, String member) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		long re = 0L;
		try {
			re = jedis.zrem(key, member);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;

	}

	/**
	 * 逆向获取zset中start到end之间的值
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrevRange(String key, int start, int end)
			throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		Set<String> re = null;
		try {
			re = jedis.zrevrange(key, start, end);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * 逆向获取zset中评分在start到end之间的值
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrevRangeByScore(String key, double start, double end)
			throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		Set<String> re = null;
		try {
			re = jedis.zrangeByScore(key, start, end);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	/**
	 * zset结构中 获取指定member的评分
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public double zscore(String key, String member) throws Exception {
		// 获取
		Jedis jedis = pool.getResource();
		double re = 0.0;
		try {
			re = jedis.zscore(key, member);
			// 如果不报异常则回收
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnResource(jedis);
		}
		return re;
	}

	public static void main(String[] args) {
		// int threadCount = 1;
		// Redis[] redis=new Redis[threadCount];
		// for(int i=0;i<threadCount;i++)
		// {
		// redis[i]=new Redis();
		// }
		// Redis redis = new Redis("10.1.32.7:22121", 1000, 30, 300);
		// Redis redis = new Redis("127.0.0.1:6379", 50, 30, 300, 10000);
		// WriteThread[] wts = new WriteThread[threadCount];
		// for (int i = 0; i < threadCount; i++) {
		// wts[i] = new WriteThread(redis);
		// }
		// Thread[] thread = new Thread[threadCount];
		// for (int i = 0; i < threadCount; i++) {
		// thread[i] = new Thread(wts[i], Integer.toString(i));
		// thread[i].start();
		// }
		// for (int i = 0; i < threadCount; i++) {
		// try {
		// thread[i].join();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		
		Redis redis=new Redis("192.168.17.128:6379",1000,30,20,1000);
		try {
			System.out.println(redis.get("a"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
