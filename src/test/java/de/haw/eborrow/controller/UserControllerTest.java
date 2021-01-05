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
import org.springframework.test.annotation.DirtiesContext;
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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
@DirtiesContext
class UserControllerTest {

	@Autowired
	private MockMvc mvc;


	@Test
	void signInFalseCredentialsFails() throws Exception {
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

		String loginData = "{\"username\":\""+String.valueOf(name)+"\",\"password\":\""+String.valueOf(pass)+"\"}";

		MvcResult unloggedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(loginData)
		)
				.andReturn();

		assertNotEquals(200, unloggedUser.getResponse().getStatus());
		assertEquals(403, unloggedUser.getResponse().getStatus());
		assertNotNull(unloggedUser.getResponse().getErrorMessage());
	}

	@Test
	void signUpSuccessful() throws Exception {
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

		MvcResult unsignedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signup")
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
		assertEquals(201, unsignedUser.getResponse().getStatus());
		assertNull(unsignedUser.getResponse().getErrorMessage());
	}

	//TODO - BUG FOUND: I can sign in with a short password
	@Test
	void signUpInvalidPasswordFails() throws Exception {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder name = new StringBuilder(5);
		Random randomN = new Random();
		for (int i = 0; i < 5; i++) {
			char c = chars[randomN.nextInt(chars.length)];
			name.append(c);
		}
		StringBuilder pass = new StringBuilder(4);
		Random randomP = new Random();
		for (int i = 0; i < 4; i++) {
			char c = chars[randomP.nextInt(chars.length)];
			pass.append(c);
		}

		MvcResult unsignedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signup")
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
		System.out.println(unsignedUser.getResponse().getStatus());
		System.out.println(unsignedUser.getResponse().getErrorMessage());
		assertNotEquals(201, unsignedUser.getResponse().getStatus());
		assertEquals(400, unsignedUser.getResponse().getStatus());
		assertNull(unsignedUser.getResponse().getErrorMessage());

	}

	@Test
	void signUpEmptyPasswordFails() throws Exception {

		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder name = new StringBuilder(5);
		Random randomN = new Random();
		for (int i = 0; i < 5; i++) {
			char c = chars[randomN.nextInt(chars.length)];
			name.append(c);
		}

		MvcResult unsignedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signup")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", String.valueOf(name))
				.param("password", "")
				.param("firstname", "fritz")
				.param("lastname", "tester")
				.param("email", "user@domain.com")
				.param("gender", "w")
				.param("birthdate", "26-07-1996")
				.accept(MediaType.APPLICATION_JSON)
		)
				.andReturn();
		System.out.println(unsignedUser.getResponse().getStatus());
		System.out.println(unsignedUser.getResponse().getErrorMessage());
		assertNotEquals(201, unsignedUser.getResponse().getStatus());
		assertEquals(400, unsignedUser.getResponse().getStatus());
		assertNull(unsignedUser.getResponse().getErrorMessage());

	}

	@Test
	void signInAndGetUserSuccessful() throws Exception {

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

		MvcResult unsignedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signup")
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

		MvcResult unloggedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(loginData)
		)
				.andReturn();

		String accessToken = JsonPath.read(unloggedUser.getResponse().getContentAsString(), "$.accessToken");
		int idVal = 2;

		MvcResult getUser = mvc.perform(MockMvcRequestBuilders.get("/users/user/"+idVal)
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(idVal)))
				.andExpect(jsonPath("$.username", is("user2")))
				.andReturn();

		assertNull(getUser.getResponse().getErrorMessage());
		assertEquals(200, getUser.getResponse().getStatus());
	}

	@Test
	void unsignedGetUserFails() throws Exception {
		int idVal = 2;

		MvcResult getUser = mvc.perform(MockMvcRequestBuilders.get("/users/user/"+idVal)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		assertNotNull(getUser.getResponse().getErrorMessage());
		assertNotEquals(200, getUser.getResponse().getStatus());
		assertEquals(403, getUser.getResponse().getStatus());
	}

	@Test
	void signInExistantUserSucessful() throws Exception {
		Thread.sleep(2000);

		String loginData = "{\"username\":\"user\",\"password\":\"password\"}";
		MvcResult unloggedUser = mvc.perform(MockMvcRequestBuilders.post("/users/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(loginData)
		)
				.andReturn();

		assertEquals(200, unloggedUser.getResponse().getStatus());
		assertNull(unloggedUser.getResponse().getErrorMessage());

	}

}