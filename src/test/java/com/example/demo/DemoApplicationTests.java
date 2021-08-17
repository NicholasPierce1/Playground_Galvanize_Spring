package com.example.demo;

//import com.example.demo.domain.JsonMapperTestPackage.ClassB;
//import com.example.demo.domain.JsonMapperTestPackage.ClassC;
//import com.example.demo.domain.JsonMapperTestPackage.JsonMapperTest;
import com.example.demo.domain.JsonMapperTestPackage.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.h2.util.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.json.JSONObject;


@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	static class Caller{

		private void init(){
			System.out.println("I");
		}

		private void start(){
			init();
			System.out.println("S");
		}

	}

	@Test
	void testThis(){
		Caller caller = new Caller();
		caller.start();
		caller.init();
	}

	@Autowired
	private DateTimeFormatter dateTimeFormatter;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {

		System.out.println(StringUtils.pad("11" ,2,"0", false));

		LocalDate localDate = LocalDate.parse("2010-10-25", dateTimeFormatter);
		// string format to pad calendar's integer representations
		// then append to date time formatter

		System.out.println(localDate);

	}

	@Test
	public void testModifyTableQuery(){

		final String[] tableNames = new String[] {"table1", "table2"};

		final String query1Input = "Select t1.* From t1";

		final String query1Output = "Select table1.* From table1";

		final String query2Input = "Select t1.*, t2.* From t1, t2 Where t1.id = t2.childId";

		final String query2Output = "Select table1.*, table2.* From table1, table2 Where table1.id = table2.childId";

		Assertions.assertEquals(
				query1Output,
				insertTableNamesIntoQuery(query1Input, tableNames[0])
				);

		Assertions.assertEquals(
				query2Output,
				insertTableNamesIntoQuery(query2Input, tableNames)
		);

	}

	@Test
	public void testObjectMapperCustom() throws Exception{

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("test_1", "someValue");
		jsonObject.put("my_test_2", 3);

		final String jsonString = jsonObject.toString();

		final JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("test1", "someValue");
		jsonObject1.put("myTest2", 3);

		final java.sql.Date date = new java.sql.Date(System.currentTimeMillis());

		System.out.println(date);

		final HashMap<String, Object> objectHashMap = new HashMap<>();
		objectHashMap.put("test_1", "someValue");
		objectHashMap.put("my_test_2", 3);
		objectHashMap.put("test_3", null);
		objectHashMap.put("test_date", String.valueOf(date));
		objectHashMap.put("sub_class",
				new HashMap<String, Object>(){{
					put("my_string", "some sub-string");
					put("my_string_list", new ArrayList<String>(){{add("hello"); add("me!");}});
				}});
		objectHashMap.put("sub_class_list",
				new ArrayList<HashMap<String, Object>>(){{
					add(
							new HashMap<String, Object>(){{
								put("my_string", "some sub-string");
								put("my_string_list", new ArrayList<String>(){{add("hello"); add("me!");}});
							}}
					);
					add(
							new HashMap<String, Object>(){{
								put("my_string", "some sub-string");
								put("my_string_list", new ArrayList<String>(){{add("hello"); add("me!");}});
							}}
					);
				}}
				);
		objectHashMap.put(
				"class_a",
						new HashMap<String,Object>(){{
							put("x", 3);
							put("y", "someString");
							put("z", true);
							put("type", "ClassB");
						}}

		);
		objectHashMap.put(
				"class_a_list",
				new ArrayList<HashMap<String,Object>>(){{
					add(new HashMap<String,Object>(){{
						put("x", 3);
						put("y", "someString");
						put("z", true);
						put("type", "ClassB");
					}});
					add(new HashMap<String,Object>(){{
						put("x", 3);
						put("y", "someString");
						put("d", 5.5);
						put("type", "ClassC");
					}});
				}}
		);


		final String jsonString1 = jsonObject1.toString();

		final JsonMapperTest jsonMapperTest = this.objectMapper.convertValue(objectHashMap, JsonMapperTest.class);

		Assertions.assertTrue(jsonMapperTest.getClassAList().get(0) instanceof ClassB);
		Assertions.assertTrue(jsonMapperTest.getClassAList().get(1) instanceof ClassC);


		//final LocalDate localDate = jsonMapperTest.getDate().toLocalDate();

		//final JsonMapperTest jsonMapperTest = this.objectMapper.convertValue(jsonString, JsonMapperTest.class);

		System.out.println(jsonMapperTest.getTest1());
		System.out.println(jsonMapperTest.getMyTest2());
		System.out.println(jsonMapperTest.getDate());
		System.out.println(jsonMapperTest.getSubClass().getMyString());
		System.out.println(jsonMapperTest.getSubClass().getMyStringList());
		System.out.println(jsonMapperTest.getSubClassList());
		System.out.println("subtype " + jsonMapperTest.getClassA().getX());
		System.out.println("subtype " + jsonMapperTest.getClassA().getY());
		System.out.println("subtype " + ((ClassB)jsonMapperTest.getClassA()).getZ());
		System.out.println(this.objectMapper.writeValueAsString(jsonMapperTest.getClassAList()));


	}

	@Test
	public void testRemovePrefixFromString(){

		final String prefixStringCompoundNumeric = "PREFIX10_value_id_id";
		final String prefixStringCompound = "PREFIX_value_id_id";
		final String prefixStringSimple = "PREFIX_value";
		final String expectedSimple = "value";
		final String expectedCompoundAndNumeric = expectedSimple.concat("_id_id");

		String actualStringCompoundNumeric =
				Arrays.stream(prefixStringCompoundNumeric.split("_"))
						.reduce(
								"",
								(accumulator, value) -> value.startsWith("PREFIX") ?
										""
										:
										accumulator.equals("") ?
												value
												:
												accumulator.concat("_").concat(value)
								// EX:
								// "" + PREFIX = ""
								// "" + value = value
								// value + id = value_id
						);

		String actualStringCompound =
				Arrays.stream(prefixStringCompound.split("_"))
						.reduce(
								"",
								(accumulator, value) -> value.startsWith("PREFIX") ?
										""
										:
										accumulator.equals("") ?
												value
												:
												accumulator.concat("_").concat(value)
								// EX:
								// "" + PREFIX = ""
								// "" + value = value
								// value + id = value_id
						);

		String actualStringSimple =
				Arrays.stream(prefixStringSimple.split("_"))
						.reduce(
								"",
								(accumulator, value) -> value.startsWith("PREFIX") ?
										""
										:
										accumulator.equals("") ?
												value
												:
												accumulator.concat("_").concat(value)
								// EX:
								// "" + PREFIX = ""
								// "" + value = value
								// value + id = value_id
						);

		Assertions.assertEquals(
				expectedCompoundAndNumeric,
				actualStringCompoundNumeric
		);

		Assertions.assertEquals(
				expectedCompoundAndNumeric,
				actualStringCompound
		);

		Assertions.assertEquals(
				expectedSimple,
				actualStringSimple
		);

		// solution 2 -- regex

		actualStringCompoundNumeric = prefixStringCompoundNumeric.replaceFirst("PREFIX[0-9]*_", "");
		actualStringCompound = prefixStringCompound.replaceFirst("PREFIX[0-9]*_", "");
		actualStringSimple = prefixStringSimple.replaceFirst("PREFIX[0-9]*_", "");

		Assertions.assertEquals(
				expectedCompoundAndNumeric,
				actualStringCompoundNumeric
		);

		Assertions.assertEquals(
				expectedCompoundAndNumeric,
				actualStringCompound
		);

		Assertions.assertEquals(
				expectedSimple,
				actualStringSimple
		);

	}

	@Test
	public void testTypeParam(){

		final Consumer<List<String>> myStringConsumer = (list) -> list.forEach(System.out::println);

		final List<String> myStringList = new ArrayList<String>(){{
			add("hello");
			add("world!");
		}};

		getType(myStringConsumer, myStringList, List.class);

	}

	@Test
	public void testLocalDateISOParse(){
		final String date =
				LocalDate
				.parse("2014-10-30")
				.format(DateTimeFormatter.ISO_DATE);
		// local date is only local date compatible & vice versa
		double x = 3f;
		System.out.println(date);
	}

	@Test
	public void testError(){
		final ArrayList<Integer> list = new ArrayList<>();
		try{
//			while(1 != 1){
//				//list.add("a");
//			}
			list.add(null);
			list.remove(null);
			list.add(1);
			list.add(2);
			list.add(3);
			list.remove(1); // index
			list.remove(Integer.valueOf(3)); // value
			System.out.println(list);

			list.addAll(Arrays.asList(4,7,6,2));

//			list.sort(
//					(a,b) ->
//							a < b ?
//									1 :
//									a > b ?
//											-1 :
//											0
//			);
			list.sort(Integer::compareTo);
			// ascending order (what is greatest or '1' goes last)
			// here, the greatest value is the smallest so smallest goes fast

			System.out.println(list);

			if(list.size() != 3)
				throw new IOException();
			// runtime exceptions do not need to be explicitly thrown (or invoke method that has it thrown)
			// checked exceptions must be thrown or invokes method that can throw checked Exception

		}
		catch(RuntimeException | IOException r){ // multi-catch cannot be subclasses
			System.out.println("r");
		}
		catch(Exception e){
			System.out.println("e");
		}
		System.out.println("we out");
	}

	@Test
	public void testStringBuilder(){
		try {
			final StringBuilder stringBuilder = new StringBuilder("hello");
			final int length = stringBuilder.length();
			stringBuilder.delete(0, length);
			stringBuilder.append("abc");
			System.out.println(stringBuilder);
			final StringBuilder stringBuilder1 = new StringBuilder("hello");
			stringBuilder1.append(stringBuilder1); // hellohello
			stringBuilder1.delete(0, 5); //hello
			System.out.println(stringBuilder1);
			stringBuilder1.insert(stringBuilder1.length(), " sir!");
			System.out.println(stringBuilder1);
			testException();
		}
		catch(Exception e ){
			System.out.println("message");
		}
		System.out.println("end"); // still occurs
	}

	@Test
	public void testFloat(){
		float x = 123_333 < 1 ? 1: 1_3;
		float y = 3.3f;
		int num = (int)y; // splice
		System.out.println(num);
		double z = 3_3;
		// 3.3 double (3.3)
		// 3_3 float (33)
		// 3_3.3 double (33.3)
		// '_' means NOTHING
		System.out.println(x);
		System.out.println(System.getProperty("user.home"));
		System.out.println(Files.exists(Paths.get(System.getProperty("user.home"))));
		System.out.println(Files.exists(Path.of(System.getProperty("user.home"))));

	}

	@Test
	public void testLoop(){
		final int[] arr = {1,2,3,4};
		int i = 0;
		do{
			System.out.print(arr[i] + " ");
			i++;
		}while(i < arr.length - 1);

		final boolean[] b_arr = new boolean[1];
		System.out.println(b_arr[0]);

	}

	public static void testException(){
		throw new RuntimeException();
	}

	private static <T extends List<?>> void getType(Consumer<T> someConsumer, T consumes, Class<?> type){

		someConsumer.accept(consumes);
	}

	private static @Nullable String convertDateToDateString(@Nullable java.util.Date date){

		if(date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1); // 0 index so add one
		return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "-" +
				String.valueOf(calendar.get(Calendar.MONTH)) + "-" +
				String.valueOf(calendar.get(Calendar.YEAR));
	}

	public static @NotNull
	String insertTableNamesIntoQuery(
			@NotNull String query,
			@NotNull final String... tableNames){

		for(int i = 0; i < tableNames.length; i++){
			final String tableStringId = String.format("t%d", i + 1);
			query = query.replaceAll(tableStringId, tableNames[i]);
		}

		return query;
	}

	 abstract class A{
		protected void doSomething(){}
		 abstract void doThis();
		protected abstract void noDoThis();

		public int x;
		public A(int x){
			this.x = x;
		}
	}

	class B extends A{
		@Override
		public void doSomething() { // can be same access level or higher
			super.doSomething();
		}

		//@Override
		void doThis(){ // Package private or up

		}

		@Override
		public void noDoThis() { // public or protected

		}

		int y;

		public B(int y){
			this(y, 3);
			this.y = y;
			// can be done in implicit constructor but super is always called first
		}

		public B(int y, int x){
			super(x);
			//this.y = y;
		}
	}

	class C{

		int x;

		public C(int x){
			this.x = x;
		}

		public C(){}

	}

	class D extends C{

		public D(){
			//this.x =3;
			super(3); // if no-arg const exists then must call super
		}

	}

	interface Readable{
		void doSomething();
	}

	abstract class A_Abstract implements Readable{
	}

	 class B_Abstract extends A_Abstract{
		public void doSomething(){}
	 }

}
