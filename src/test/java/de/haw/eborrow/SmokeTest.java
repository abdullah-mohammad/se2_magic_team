
package de.haw.eborrow;

import static org.assertj.core.api.Assertions.assertThat;

import de.haw.eborrow.controller.ItemController;
import de.haw.eborrow.controller.UserController;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

	@Autowired
	private ItemController itemController;

	@Autowired
	private UserController userController;

	@Test
	public void contextLoadsItem() throws Exception {
		assertThat(itemController).isNotNull();
	}
	@Test
	public void contextLoadsUser() throws Exception {
		assertThat(userController).isNotNull();
	}
}
