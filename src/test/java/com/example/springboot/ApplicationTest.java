package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    //scenario: a user posts a string and then deletes it. After accessing the history, the string cannot be found.
    @Test
    void scenario() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=Test_String")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=Test_String"))
                .andExpect(content().string(containsString("has been deleted")));//making sure it actually deleted before accessing history
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(not(containsString("Test_String"))));

        //This scenario is true.
    }


    //test to check if the delete functionality is case-sensitive.
    @Test
    void caseSensitive() throws Exception{
        //posting a string and will try to delete it in lowercase
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=Hello")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=hello"))
                .andExpect(content().string(containsString("does not exist")));

        //The delete is case-sensitive.
    }

}