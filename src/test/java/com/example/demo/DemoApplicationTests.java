package com.example.demo;

import com.sun.istack.NotNull;
import org.h2.util.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

}
