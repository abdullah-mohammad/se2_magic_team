
package de.haw.eborrow;

import de.haw.eborrow.models.Item;
import de.haw.eborrow.models.User;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpPostTest {

	@LocalServerPort
	private int port = 8081;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void itemsHttpPostTest () throws Exception {
		User user = new User("user", "$2a$10$MeWhrWoE2yvclNulZyCZb.IDxlJIhQDMbm8pOVhH3DRGFaz7ZpaaS", "User 1", "isLastaName", "user@using.com", "w", new Date(111), "pic.jpg");
		Item item = new Item(	"Rasenmaeher","Gutes Geraet, ich liebe es, liebst du es auch?", "maeh.jpg",	true, user);

		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/items",
				String.class)).contains(item.getDescription());
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/items",
				String.class)).doesNotContain("lalalalala");
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/items",
				String.class)).contains("Rasenmaeher");
	}
}
