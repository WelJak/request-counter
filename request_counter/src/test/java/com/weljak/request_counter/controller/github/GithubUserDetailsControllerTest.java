package com.weljak.request_counter.controller.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.weljak.request_counter.service.external.github.exception.ExternalApiException;
import com.weljak.request_counter.service.github.GithubUserDetails;
import com.weljak.request_counter.service.github.GithubUserDetailsService;
import com.weljak.request_counter.utils.Endpoints;
import com.weljak.request_counter.webapi.github.GithubUserDetailsController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = GithubUserDetailsController.class)
public class GithubUserDetailsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GithubUserDetailsService service;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setupTests() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    }

    @AfterEach
    void teardown() {
        reset(service);
    }

    @Test
    void shouldReturnStatusOk() throws Exception {
        //given
        String testLogin = "testUser";
        var zonedDateTime = ZonedDateTime.of(2023, 1, 1, 1, 1, 1, 100, ZoneId.systemDefault());
        GithubUserDetails expectedResponse = new GithubUserDetails("", "", "", "", "", zonedDateTime, "");

        //when
        when(service.getUserDetails(testLogin)).thenReturn(expectedResponse);

        //then
        mockMvc.perform(get(Endpoints.GET_GITHUB_USER_DETAILS, testLogin)).andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void shouldReturnBadRequestWhenUserNameIsEmpty() throws Exception {
        //then
        mockMvc.perform(get(Endpoints.GET_GITHUB_USER_DETAILS, " ")).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnFailResponseWhenServiceThrowsException() throws Exception {
        //given
        String testLogin = "testUser";

        //when
        when(service.getUserDetails(testLogin)).thenThrow(ExternalApiException.class);

        //then
        mockMvc.perform(get(Endpoints.GET_GITHUB_USER_DETAILS, testLogin)).andExpect(status().isInternalServerError());
    }
}
