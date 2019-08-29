package com.example.demo.controllertest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class CreateLoginTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TestLoginCreate {

    /** The user controller. */
    @InjectMocks
    private UserController userController;

    /** The user repo. */
    @Mock
    private UserRepository userRepo;

    /** The cart repo. */
    @Mock
    private CartRepository cartRepo;

    /** The encoder. */
    @Mock
    private BCryptPasswordEncoder encoder;

    /** The mock mvc. */
    @Autowired
    private MockMvc mockMvc;

    /** The request. */
    @Autowired
    private MockHttpServletRequest request;

    @Autowired
    private ObjectMapper objectMapper;

    /** The user request. */
    CreateUserRequest userRequest;

    /**
     * Inits the.
     */
    @BeforeEach
    public void init() {
	userRequest = new CreateUserRequest();
	userRequest.setUsername("ManmeetTest");
	userRequest.setPassword("password");
	userRequest.setConfirmPassword("password");
    }

    /**
     * Test create user happy scenario.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Test method to check the happy scenario of user creation with correct details")
    public void testCreateUserHappyScenario() throws Exception {

	mockMvc.perform(
		MockMvcRequestBuilders.post("/api/user/create").content(objectMapper.writeValueAsString(userRequest))
			.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.username").exists())
		.andReturn();
    }

    /**
     * Test create user login scenario.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Test method to test check login of user with correc(happy) and incorrect(unhappy) scenario")
    public void testCreateUserLoginScenario() throws Exception {

	userRequest.setUsername("ManmeetTest1");
	mockMvc.perform(
		MockMvcRequestBuilders.post("/api/user/create").content(objectMapper.writeValueAsString(userRequest))
			.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.username").exists())
		.andReturn();

	MvcResult result = mockMvc
		.perform(MockMvcRequestBuilders.post("/login").content(objectMapper.writeValueAsString(userRequest)))
		.andExpect(status().isOk()).andReturn();
	request.addParameter("Authorization", result.getResponse().getHeader("Authorization"));
	assertNotNull(request.getParameter("Authorization"));

	userRequest.setUsername("testValue");
	mockMvc.perform(MockMvcRequestBuilders.post("/login").content(objectMapper.writeValueAsString(userRequest)))
		.andExpect(status().isUnauthorized()).andReturn();

	userRequest.setUsername("testValue");
	userRequest.setPassword("testPass");
	mockMvc.perform(MockMvcRequestBuilders.post("/login").content(objectMapper.writeValueAsString(userRequest)))
		.andExpect(status().isUnauthorized()).andReturn();
    }

    /**
     * Test create user unhappy scenario 1.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Test method to test unhappy scenario i.e. request with null username")
    public void testCreateUserUnhappyScenario1() throws Exception {
	userRequest.setUsername(null);
	mockMvc.perform(
		MockMvcRequestBuilders.post("/api/user/create").content(objectMapper.writeValueAsString(userRequest))
			.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest()).andReturn();

    }

    /**
     * Test create user unhappy scenario 2.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Test method to test unhappy scenario i.e. request with less length password")
    public void testCreateUserUnhappyScenario2() throws Exception {
	userRequest.setPassword("test");
	userRequest.setConfirmPassword("test");
	mockMvc.perform(
		MockMvcRequestBuilders.post("/api/user/create").content(objectMapper.writeValueAsString(userRequest))
			.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest()).andReturn();
    }

    /**
     * Test create user unhappy scenario 3.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Test method to test unhappy scenario i.e. request with incorrect confirm password")
    public void testCreateUserUnhappyScenario3() throws Exception {
	userRequest.setConfirmPassword("test");
	mockMvc.perform(
		MockMvcRequestBuilders.post("/api/user/create").content(objectMapper.writeValueAsString(userRequest))
			.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest()).andReturn();
    }

    /**
     * Test create user unhappy scenario 4.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Test method to test unhappy scenario i.e. request with empty username")
    public void testCreateUserUnhappyScenario4() throws Exception {
	userRequest.setUsername("");
	mockMvc.perform(
		MockMvcRequestBuilders.post("/api/user/create").content(objectMapper.writeValueAsString(userRequest))
			.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest()).andReturn();
    }
}
