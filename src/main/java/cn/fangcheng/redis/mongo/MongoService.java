package cn.fangcheng.redis.mongo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.fangcheng.util.SerializeUtil;

public class MongoService {

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
		
		context = new ClassPathXmlApplicationContext(new String[] { "classpath:applicationContext-mongodb.xml","classpath:applicationContext-redis.xml", });
		@SuppressWarnings("unchecked")
		RedisTemplate<Serializable, Serializable> redisTemplate = (RedisTemplate<Serializable, Serializable>) context
				.getBean("redisTemplate");
		
		MongoTemplate mongoTemplate=(MongoTemplate) context.getBean("mongoTemplate");
		
		DBCollection  dc = mongoTemplate.getCollection("city_2_page_bak");

		BasicDBObject bo = new BasicDBObject();
		
		bo.append("shops.0", new BasicDBObject("$exists",1));
		DBCursor dtemp = dc.find(bo);
		System.out.println("================start=======================");
		while(dtemp.hasNext()){
			DBObject d = dtemp.next();
			int  id= (Integer) d.get("shopId");
			redisTemplate.opsForSet().add("201509", id+"");
			@SuppressWarnings("unchecked")
			List<String> arr=  (List<String>) d.get("shops");
			Iterator<String> it = arr.iterator();
			while(it.hasNext()){
				redisTemplate.opsForSet().add(id+".201509", it.next());
			}			
		}
		System.out.println("================end=======================");
		
	
		
		
		
		
//		
//		User u = new User();
//		u.setId(1);
//		u.setName("123");
//		rs.saveUser(u, redisTemplate);
//		User uu = rs.getUser(1, redisTemplate);
//		System.out.println(uu.getName());
//		redisTemplate.opsForSet().add("aaa", 1);
//		redisTemplate.opsForSet().add("aaa", 2);
//		redisTemplate.opsForSet().add("aaa", u);
//		redisTemplate.opsForSet().members("aaa");
		
	}
}
