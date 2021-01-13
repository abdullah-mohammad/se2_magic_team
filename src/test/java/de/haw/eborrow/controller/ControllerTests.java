
package de.haw.eborrow.controller;

import com.jayway.jsonpath.JsonPath;
import de.haw.eborrow.models.Address;
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
import java.util.Random;
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

	@Test
	void getAllItems() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/items");
		User user = new User("user", "$2a$10$MeWhrWoE2yvclNulZyCZb.IDxlJIhQDMbm8pOVhH3DRGFaz7ZpaaS", "User 1", "isLastaName", "user@using.com", "w", new Date(111),
				"pic.jpg", new Address("Berliner Tor", "1", 20099, "Hamburg", "Germany"));

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


	@Test
	void getItemById() throws Exception {
		int id = 1;
		RequestBuilder request = MockMvcRequestBuilders.get("/items/"+id);

		MvcResult result = mvc.perform(request).andExpect(jsonPath("$.id", is(id))).andReturn();

		assertNull(result.getResponse().getErrorMessage());
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	void createItem() throws Exception {

		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder name = new StringBuilder(5);
		Random randomN = new Random();
		for (int i = 0; i < 5; i++) {
			char c = chars[randomN.nextInt(chars.length)];
			name.append(c);
		}
		StringBuilder pass = new StringBuilder(5);
		Random randomP = new Random();
		for (int i = 0; i < 5; i++) {
			char c = chars[randomP.nextInt(chars.length)];
			pass.append(c);
		}

		MvcResult ungesignedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signup")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", String.valueOf(name))
				.param("password", String.valueOf(pass))
				.param("firstname", "fritz")
				.param("lastname", "tester")
				.param("email", "user@domain.com")
				.param("gender", "w")
				.param("birthdate", "26-07-1996")
				.accept(MediaType.APPLICATION_JSON)
		)
				.andReturn();


		String loginData = "{\"username\":\""+String.valueOf(name)+"\",\"password\":\""+String.valueOf(pass)+"\"}";
		MvcResult ungeloggedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(loginData)
		)
				.andReturn();


		String accessToken = JsonPath.read(ungeloggedUser.getResponse().getContentAsString(), "$.accessToken");

		String userId = Integer.toString(JsonPath.read(ungeloggedUser.getResponse().getContentAsString(), "$.id"));

		char[] chars1 = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder nameItem = new StringBuilder(5);
		Random randomI = new Random();
		for (int i = 0; i < 5; i++) {
			char c = chars[randomI.nextInt(chars.length)];
			nameItem.append(c);
		}

		MvcResult neuenWerkzeug = mvc.perform(MockMvcRequestBuilders.post("/items")
				.header("Authorization", "Bearer " + accessToken)
				.param("title", String.valueOf(nameItem))
				.param("description", "test")
				.param("available", "true")
				.param("user", userId)
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

		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder name = new StringBuilder(5);
		Random randomN = new Random();
		for (int i = 0; i < 5; i++) {
			char c = chars[randomN.nextInt(chars.length)];
			name.append(c);
		}
		StringBuilder pass = new StringBuilder(5);
		Random randomP = new Random();
		for (int i = 0; i < 5; i++) {
			char c = chars[randomP.nextInt(chars.length)];
			pass.append(c);
		}

		MvcResult ungesignedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signup")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", String.valueOf(name))
				.param("password", String.valueOf(pass))
				.param("firstname", "fritz")
				.param("lastname", "tester")
				.param("email", "user@domain.com")
				.param("gender", "w")
				.param("birthdate", "26-07-1996")
				.accept(MediaType.APPLICATION_JSON)
		)
				.andReturn();


		String loginData = "{\"username\":\""+String.valueOf(name)+"\",\"password\":\""+String.valueOf(pass)+"\"}";
		MvcResult ungeloggedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(loginData)
		)
				.andReturn();


		String accessToken = JsonPath.read(ungeloggedUser.getResponse().getContentAsString(), "$.accessToken");

		String userId = Integer.toString(JsonPath.read(ungeloggedUser.getResponse().getContentAsString(), "$.id"));

		char[] chars1 = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder nameItem = new StringBuilder(5);
		Random randomI = new Random();
		for (int i = 0; i < 5; i++) {
			char c = chars[randomI.nextInt(chars.length)];
			nameItem.append(c);
		}

		MvcResult neuenWerkzeug = mvc.perform(MockMvcRequestBuilders.post("/items")
				.header("Authorization", "Bearer " + accessToken)
				.param("title", String.valueOf(nameItem))
				.param("description", "test")
				.param("available", "true")
				.param("user", userId)
		)
				.andReturn();

		MvcResult allItems = mvc.perform(MockMvcRequestBuilders.get("/items")).andReturn();
		String itemsJsonasString = allItems.getResponse().getContentAsString();
		Pattern pattern = Pattern.compile("id\":(\\d),\"title\":\""+String.valueOf(nameItem)+"\"");
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

