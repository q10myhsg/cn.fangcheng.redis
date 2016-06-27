package cn.fangcheng.redis;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import cn.fangcheng.util.SerializeUtil;

public class RedisService {

	private static ApplicationContext context;

	public void saveUser(final User user, final RedisTemplate<Serializable, Serializable> redisTemplate) {
		// redisTemplate.execute(new RedisCallback<Object>() {
		// public Object doInRedis(RedisConnection con) throws
		// DataAccessException {
		//
		// con.set(SerializeUtil.serialize(user.getId()),
		// SerializeUtil.serialize(user));
		// redisTemplate.opsForValue().set("aaa", "asdfasdfasdf");
		// redisTemplate.opsForValue().get("aaa");
		//
		// return null;
		// }
		// });
		redisTemplate.opsForValue().set(user.getId(), user.getName());
	}

	public User getUser(final int id, final RedisTemplate<Serializable, Serializable> redisTemplate) {
		return redisTemplate.execute(new RedisCallback<User>() {
			public User doInRedis(RedisConnection con) throws DataAccessException {
				User u = (User) SerializeUtil.unserialize(con.get(SerializeUtil.serialize(id)));
				System.out.println(u.getName());
				return u;
			}
		});
	}

	public static void main(String[] args) {
		System.out.println("ddd");
		context = new ClassPathXmlApplicationContext(new String[] { "classpath:applicationContext.xml", });
		@SuppressWarnings("unchecked")
		RedisTemplate<Serializable, Serializable> redisTemplate = (RedisTemplate<Serializable, Serializable>) context
				.getBean("redisTemplate");
		RedisService rs = new RedisService();
		User u = new User();
		u.setId(1);
		u.setName("123");
		rs.saveUser(u, redisTemplate);
		User uu = rs.getUser(1, redisTemplate);
		System.out.println(uu.getName());

		redisTemplate.opsForSet().add("aaa", 1);
		redisTemplate.opsForSet().add("aaa", 2);
		redisTemplate.opsForSet().add("aaa", u);
		redisTemplate.opsForSet().members("aaa");
		
	}
}
