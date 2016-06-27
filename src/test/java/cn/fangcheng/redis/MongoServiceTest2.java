package cn.fangcheng.redis;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-mongodb.xml",
		"classpath:applicationContext-redis.xml", })
public class MongoServiceTest2 {
	private static ApplicationContext context;

	static RedisTemplate<Serializable, Serializable> redisTemplate;
	static MongoTemplate mongoTemplate;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(
				new String[] { "classpath:applicationContext-mongodb.xml", "classpath:applicationContext-redis.xml", });
		;
		redisTemplate = (RedisTemplate<Serializable, Serializable>) context.getBean("redisTemplate");
		mongoTemplate = (MongoTemplate) context.getBean("mongoTemplate");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveUser() {
		redisTemplate.delete("aaa");
		redisTemplate.opsForSet().add("aaa", "a");
		redisTemplate.opsForSet().add("aaa", "b");
		// redisTemplate.opsForSet().add("aaa", "c");
		redisTemplate.opsForSet().members("aaa").clear();
		// .delete("aaa");
		// Iterator<Serializable> it =
		// redisTemplate.opsForSet().members("aaa").iterator() ;
		// while(it.hasNext()){
		// System.out.println(it.next());
		// }
		//
		redisTemplate.opsForHash().put("abc", "h1", "h1");
		redisTemplate.opsForHash().put("abc", "h2", "h2");
		redisTemplate.opsForHash().delete("abc", "h1");
		System.out.println(redisTemplate.opsForHash().get("abc", "h2"));
	}

	@Test
	public void testMallListCompare() {
		Set<Serializable> s = redisTemplate.opsForSet().difference("201509", "201510");
		System.out.println(s.size());
	}

	@Test
	public void testMallShopDis() {
		Set<Serializable> member201511  =redisTemplate.opsForSet().members("201511");
		Iterator<Serializable> it = member201511.iterator();
		while(it.hasNext()){
			String temp = it.next().toString();
//			System.out.println(temp);
			Set<Serializable> ss = redisTemplate.opsForSet().difference(temp+"201511",temp+"201509");
			if(ss.size()>0){
				System.out.println(temp+":"+ss.toString());
			}
		}
	}

}
