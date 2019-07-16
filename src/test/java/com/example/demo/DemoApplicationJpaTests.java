package com.example.demo;

import com.example.demo.repository.jpa.PersonJpaRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationJpaTests {

	@Autowired
	WebApplicationContext applicationContext;

	@Autowired
	PersonJpaRepository personJpaRepository;

	MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
				.alwaysDo(print())
				.build();
	}

	@Test
	public void shouldReturnListOfPeopleByJpa() throws Exception {
		mockMvc.perform(get("/api/persons"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].provider").value("JPA"));
	}

	@Test //TODO Juninho
	public void shouldInsertListOfPeopleUsingCsvFile() throws Exception { //Always good to have test to make sure that everything is working
		mockMvc.perform(multipart("/api/persons/uploadCSV")
				.file(new MockMultipartFile("file", new FileInputStream("src/test/resources/people.csv"))))
				.andExpect(status().isOk())
				.andExpect(content().string("Successful!"));

		mockMvc.perform(get("/api/persons"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[*].name").value(hasItems("Rodrigo", "Test", "Juninho")));
	}

	@After
	public void tearDown() { //Good practice is always delete temporary data.
		personJpaRepository.deleteAll();
	}

}

