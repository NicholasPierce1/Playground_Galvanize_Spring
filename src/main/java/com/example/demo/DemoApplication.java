package com.example.demo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Configuration
public class DemoApplication {

	public static void main(String[] args) {

		final ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		List<String> list = new ArrayList<String>(java.util.Arrays.asList(context.getBeanDefinitionNames()));
		System.out.println(list.contains("MongoClient"));
		System.out.println(list.contains("mongoClient"));


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
