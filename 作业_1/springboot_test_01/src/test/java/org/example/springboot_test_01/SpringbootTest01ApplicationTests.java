package org.example.springboot_test_01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringbootTest01ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // 通过反射清空 UserController 中的 users Map
        UserController.users.clear();
    }


    @Test
    public void testGetUsersListEmpty() throws Exception {
        // 测试获取用户列表，当没有数据时返回空
        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testCreateUser() throws Exception {
        // 测试创建用户
        String userJson = "{\"id\":1,\"name\":\"测试大师\",\"age\":20}";

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        mockMvc.perform(get("/users/"))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void testGetUsersListAfterAddingUser() throws Exception {
        // 测试添加一个用户后，获取用户列表
        String userJson = "{\"id\":1,\"name\":\"测试大师\",\"age\":20}";
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("测试大师")))
                .andExpect(jsonPath("$[0].age", is(20)));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // 测试更新用户
        String userJson = "{\"id\":1,\"name\":\"测试大师\",\"age\":20}";
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        // 更新用户信息
        String updatedUserJson = "{\"name\":\"测试终极大师\",\"age\":30}";
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        // 验证更新后的用户信息
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("测试终极大师")))
                .andExpect(jsonPath("$.age", is(30)));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // 测试删除用户
        String userJson = "{\"id\":1,\"name\":\"测试大师\",\"age\":20}";
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        // 删除用户
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        // 验证用户已被删除
        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
