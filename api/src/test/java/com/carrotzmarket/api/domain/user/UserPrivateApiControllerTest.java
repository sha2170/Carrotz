package com.carrotzmarket.api.domain.user;

import com.carrotzmarket.api.domain.user.controller.model.UserUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@SpringBootTest
@AutoConfigureMockMvc
public class UserPrivateApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void updateUserTest() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("newPassword", "newImage.jpg", "newEmail@example.com", "010-1234-5678", 1L);

        mockMvc.perform(put("/private-api/user/update")
                        .param("loginId", "userLogin123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/private-api/user/delete")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }
}

