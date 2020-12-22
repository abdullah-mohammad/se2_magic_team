
package de.haw.eborrow.controller;

import com.jayway.jsonpath.JsonPath;
import de.haw.eborrow.models.Item;
import de.haw.eborrow.models.User;
import de.haw.eborrow.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//These are not UNIT tests! These are more of integration tests

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
//@WebMvcTest(ItemController.class)
@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired


	@Test
	void getAllItems() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/items");
		User user = new User("user", "$2a$10$MeWhrWoE2yvclNulZyCZb.IDxlJIhQDMbm8pOVhH3DRGFaz7ZpaaS", "User 1", "isLastaName", "user@using.com", "w", new Date(111), "pic.jpg");
		Item item = new Item("Rasenmaeher", "Gutes Geraet, ich liebe es, liebst du es auch?", "maeh.jpg", true, user);

		MvcResult result = mvc.perform(request)
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].title", is(item.getTitle())))
				.andExpect(jsonPath("$[0].user.id", is(1)))
				.andExpect(jsonPath("$[0].user.username", is(item.getUser().getUsername())))
				.andReturn();

		assertNull(result.getResponse().getErrorMessage());

		assertEquals(200, result.getResponse().getStatus());
	}

	//ToDo: DAS HIER IST EIN BEISPIEL
	@Test
	void getItemById() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/items/1");
		//TESTCASE 1: and Expect - pruefen, ob id wirklich 1 ist
		MvcResult result = mvc.perform(request).andExpect(jsonPath("$.id", is(1))).andReturn();

		//TESTCASE 2:assert - pruefen, ob error message gleich null ist --> keine Fehler auftreten
		assertNull(result.getResponse().getErrorMessage());

		//TESTCASE 3:assert - pruefen, ob http status 200 ist : erfolgreich
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	void createItem() throws Exception {

		//erstelle Konto fuer einen neuen Nutzer
		MvcResult ungesignedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signup")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "ALAA")
				.param("password", "password")
				.param("firstname", "fritz")
				.param("lastname", "tester")
				.param("email", "user@domain.com")
				.param("gender", "w")
				.param("birthdate", "26-07-1996")
				.accept(MediaType.APPLICATION_JSON)
		)
				.andReturn();
		assertEquals(201, ungesignedUser.getResponse().getStatus());
		assertNull(ungesignedUser.getResponse().getErrorMessage());

		//Login von erstellten Konto
		String loginData = "{\"username\":\"ALAA\",\"password\":\"password\"}";
		MvcResult ungeloggedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(loginData)
		)
				.andReturn();

		assertEquals(200, ungeloggedUser.getResponse().getStatus());
		assertNull(ungeloggedUser.getResponse().getErrorMessage());

		//hier extrahieren wir den AccessToken von dem Response - wir brauchen das, weil der Nutzer darf leider nicht einfach so
		//neuen Werkzeug erstellen - unser System brauch dafuer einen Token

		String accessToken = JsonPath.read(ungeloggedUser.getResponse().getContentAsString(), "$.accessToken");

		//auch extrahieren wir den Id von dem Nutzer, der automatisch generiert wurde
		String userId = Integer.toString(JsonPath.read(ungeloggedUser.getResponse().getContentAsString(), "$.id"));

		//jetzt erzeugt der Nutzer einen neune Werkzeug

		MvcResult neuenWerkzeug = mvc.perform(MockMvcRequestBuilders.post("/items")
				.header("Authorization", "Bearer " + accessToken)
				.param("title", "TEst")
				.param("description", "test")
				.param("available", "true")
				.param("user", "3")
		)
				.andReturn();

		assertEquals(201, neuenWerkzeug.getResponse().getStatus());
		assertNull(neuenWerkzeug.getResponse().getErrorMessage());

	}

	@Test
	void updateTutorial() {
	}

	@Test
	void deleteItem() throws Exception {

		//erstelle Konto fuer einen neuen Nutzer
		MvcResult ungesignedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signup")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "ALAA2") // ALAA2 weil sonst 2 user mit gleichem namen existieren können
				.param("password", "password")
				.param("firstname", "fritz")
				.param("lastname", "tester")
				.param("email", "user@domain.com")
				.param("gender", "w")
				.param("birthdate", "26-07-1996")
				.accept(MediaType.APPLICATION_JSON)
		)
				.andReturn();
		assertEquals(201, ungesignedUser.getResponse().getStatus());

		String loginData = "{\"username\":\"ALAA2\",\"password\":\"password\"}";
		MvcResult unloggedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(loginData)
		)
				.andReturn();

		String accessToken = JsonPath.read(unloggedUser.getResponse().getContentAsString(), "$.accessToken");
		String userId = Integer.toString(JsonPath.read(unloggedUser.getResponse().getContentAsString(), "$.id"));

		MvcResult neuenWerkzeug = mvc.perform(MockMvcRequestBuilders.post("/items")
				.header("Authorization", "Bearer " + accessToken)
				.param("title", "Test")
				.param("description", "test")
				.param("available", "true")
				.param("user", userId)
		)
				.andReturn();

		MvcResult allItems = mvc.perform(MockMvcRequestBuilders.get("/items")).andReturn();
		String itemsJsonasString = allItems.getResponse().getContentAsString();
		Pattern pattern = Pattern.compile("id\":(\\d),\"title\":\"Test");
		Matcher matcher = pattern.matcher(itemsJsonasString);
		String itemId = null;

		if (matcher.find()) {
			itemId = matcher.group(1);
		}

		MvcResult deletingItem = mvc.perform(MockMvcRequestBuilders.delete("/items/{id}", itemId)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON)
		).andReturn();

		assertEquals(204, deletingItem.getResponse().getStatus());
		assertNull(deletingItem.getResponse().getErrorMessage());
	}

	@Test
	void deleteAllItems() {
	}

	@Test
	void findByAvailable() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/items/available");
		//hier pruefe ich, ob ALLE Elemente, die ich mit dem json kriege, den Parameter "available" gleich true haben
		MvcResult result = mvc.perform(request)
				.andExpect(jsonPath("$[*].available").value(Matchers.not(false)))
				.andReturn();
		assertNull(result.getResponse().getErrorMessage());
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	void getImageWithMediaTyp() {
	}
}

