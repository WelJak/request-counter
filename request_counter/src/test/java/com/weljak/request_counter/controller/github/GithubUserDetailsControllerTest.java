package com.weljak.request_counter.controller.github;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = GithubUserDetailsController.class)
public class GithubUserDetailsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GithubUserDetailsRequestCountService service;

    @Test
    void shouldReturnStatusOk() throws Exception {
        //given
        String testLogin = "testUser";

        //when
        when(service.getUserDetails(testLogin)).thenReturn(new GithubUserDetails("", "", "", "", "", "", ""));

        //then
        mockMvc.perform(get("/github/user/details/", testLogin)).andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenUserNameIsEmpty() {
        //when
        when(service.getUserDetails(testLogin)).thenReturn(new GithubUserDetails("", "", "", "", "", "", ""));

        //then
        mockMvc.perform(get("/github/user/details/", testLogin)).andExpect(status().isBadRequest());
    }
}
