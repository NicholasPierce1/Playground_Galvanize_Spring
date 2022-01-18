package com.example.demo;

import com.example.demo.model.TestBeanModel;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.boot.CommandLineRunner;
import javax.servlet.http.Cookie;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Configuration
public class DemoApplication {

	@Value("kafka.topic.id")
	String topicId;

	@Autowired
	ApplicationContext xmlApplicationContext;

	@Autowired
	ConfigurableEnvironment env; // or just env in same path

	public static final class DummyBeanClass{
		public boolean isActive(){
			return true;
		}
	}

	//@Profile(value = {"default", "dev"}) // found with default or dev
	@Profile(value = "dev") // found only if dev profile is active
	@Bean
	public DummyBeanClass getDummyBeanClass(){
		return new DemoApplication.DummyBeanClass();
	}

	public static void main(String[] args) {

		/*
		bean profiles must be set BEFORE the application context is created
		 */
		final ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		final DemoApplication demoApplication = (DemoApplication)context.getBean("demoApplication");

		final ApplicationContext applicationContext = (ApplicationContext)context.getBean("getXmlApplicationContext");

		//demoApplication.env.setActiveProfiles("dev"); // not working rn

		List<String> list = new ArrayList<String>(java.util.Arrays.asList(context.getBeanDefinitionNames()));
//		System.out.println(list.contains("MongoClient"));
//		System.out.println(list.contains("mongoClient"));
		System.out.println(list.contains("demoApplication"));
		System.out.println("is profile bean found: " + list.contains("getDummyBeanClass"));
		System.out.println("is dev dummy model bean found: " + list.contains("devProfile"));

		// keep defaults (beans which belongs to all profiles, no profile tag, will always be there)
		// set or add active profiles varying on if you're running dev, prod, or other

		System.out.println(Arrays.toString(demoApplication.env.getActiveProfiles()));
		System.out.println(Arrays.toString(demoApplication.env.getDefaultProfiles()));

		System.out.println("kafka value: " + demoApplication.topicId);

		try{
//			ApplicationContext ctx = new
//					ClassPathXmlApplicationContext( "applicationContext.xml" );
//
			final TestBeanModel  testBeanModel = (TestBeanModel)applicationContext.getBean(TestBeanModel.class);
			testBeanModel.printSuccess();
		}
		catch(Exception ex){
			System.out.println("oops: " + ex);
		}


//		ConnectionString connectionString = new ConnectionString("mongodb+srv://adminU:adminP@testcluster.elozi.mongodb.net/testDatabase?retryWrites=true&w=majority&ssl=true&authSource=admin&authMechanism=SCRAM-SHA-1");
//		MongoClientSettings settings = MongoClientSettings.builder()
//				.applyConnectionString(connectionString)
//				.build();
//		MongoClient mongoClient = MongoClients.create(settings);
//		MongoDatabase database = mongoClient.getDatabase("testDatabase");
//        final MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "testDatabase");
//        System.out.println("Collection Exists? " + mongoTemplate.collectionExists("testDatabaseCollection"));
//		System.out.println(database.getName());
//		System.out.println(database.getCollection("testDatabaseCollection").getNamespace().getCollectionName());
	}

//	public @Bean MongoClient getMongoClient(){
//		ConnectionString connectionString = new ConnectionString("mongodb+srv://adminU:adminP@testcluster.elozi.mongodb.net/testDatabase?retryWrites=true&w=majority");
//		MongoClientSettings settings = MongoClientSettings.builder()
//				.applyConnectionString(connectionString)
//				.build();
//		return MongoClients.create(settings);
//	}

//	public @Bean MongoTemplate getMongoTemplate(){
//		return new MongoTemplate(getMongoClient(), "testDatabase"); // client & database name
//	}
//
//	public static MongoTemplate getMongoTemplateStatic(){
//		return new MongoTemplate(getMongoClient(), "testDatabase"); // client & database name
//	}
//
//	public @Bean MongoDatabase getMongoDatabase(){
//		return getMongoClient().getDatabase("testDatabase");
//	}

}
