package com.weljak.request_counter.controller.github;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.weljak.request_counter.service.github.GithubUserDetails;
import com.weljak.request_counter.service.github.GithubUserDetailsService;
import com.weljak.request_counter.utils.Endpoints;
import com.weljak.request_counter.webapi.github.GithubUserDetailsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.ZonedDateTime;

@ActiveProfiles("test")
@WebMvcTest(controllers = GithubUserDetailsController.class)
public class GithubUserDetailsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GithubUserDetailsService service;

    @Test
    void shouldReturnStatusOk() throws Exception {
        //given
        String testLogin = "testUser";

        //when
        when(service.getUserDetails(testLogin)).thenReturn(new GithubUserDetails("", "", "", "", "", ZonedDateTime.now(), ""));

        //then
        mockMvc.perform(get(Endpoints.GET_GITHUB_USER_DETAILS, testLogin)).andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenUserNameIsEmpty() throws Exception {

//        //when
//        when(service.getUserDetails(testLogin)).thenReturn(new GithubUserDetails("", "", "", "", "", ZonedDateTime.now(), ""));

        //then
        mockMvc.perform(get(Endpoints.GET_GITHUB_USER_DETAILS, " ")).andExpect(status().isBadRequest());
    }
}
