package com.example.demo.controllertest;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.repositories.ItemRepository;

/**
 * The Class ItemControllerTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TestItemController {

	/** The item controller. */
	@InjectMocks
	private ItemController itemController;

	/** The item repo. */
	@Mock
	private ItemRepository itemRepo;

	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	/**
	 * Gets the all item test.
	 *
	 * @return the all item test
	 * @throws Exception the exception
	 */
	@Test
	public void getAllItemTest() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/item").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertNotNull(mvcResult.getResponse().getContentAsString());
	}

	/**
	 * Gets the item by name.
	 *
	 * @return the item by name
	 * @throws Exception the exception
	 */
	@Test
	public void getItemByName() throws Exception {
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/item/name/{name}", "Round Widget").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertNotNull(mvcResult.getResponse().getContentAsString());

		mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/item/name/{name}", "Toys").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

	}

	/**
	 * Gets the item by id.
	 *
	 * @return the item by id
	 * @throws Exception the exception
	 */
	@Test
	public void getItemById() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/item/{id}", 2).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertNotNull(mvcResult.getResponse().getContentAsString());

		mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/item/{id}", 23).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
	}
}
