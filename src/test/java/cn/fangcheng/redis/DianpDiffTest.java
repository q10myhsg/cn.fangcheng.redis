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

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-mongodb.xml",
		"classpath:applicationContext-redis.xml", })
public class DianpDiffTest {
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
	public void top1w() {		
		 DBCollection dc = mongoTemplate.getCollection("city_2_dynamic");
		 DBCursor dd =dc.find().sort( new BasicDBObject().append("shopId", 1));
		 int max=10000;
		 int count=0;
		
		 while(dd.hasNext()){
			 count++;
			 DBObject d = dd.next();
			
		 }
		
		
		
		
		
		
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
