package org.example.springboot_test_01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any; // 确保导入 ArgumentMatchers
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringbootTest01ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserMapper userMapper; // 模拟 UserMapper

    @InjectMocks
    private UserController userController; // 注入到控制器

    @BeforeEach
    public void setUp() {
        // 初始化模拟对象
        MockitoAnnotations.openMocks(this);

        // 设置模拟数据
        User user1 = new User();
        user1.setId(1L);
        user1.setName("测试大师");
        user1.setAge(20);

        // 模拟 UserMapper 的行为
        when(userMapper.findById(1L)).thenReturn(user1);
        when(userMapper.findAll()).thenReturn(List.of(user1));
    }

    @Test
    public void testGetUsersListEmpty() throws Exception {
        // 测试获取用户列表，当没有数据时返回空
        when(userMapper.findAll()).thenReturn(List.of()); // 模拟返回空列表

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

        // 确认 UserMapper 的 insert 方法被调用
        verify(userMapper, times(1)).insert(any(User.class)); // 使用 ArgumentMatchers.any() 进行参数匹配
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

        // 确认 UserMapper 的 insert 方法被调用，模拟更新操作
        verify(userMapper, times(1)).insert(any(User.class)); // 插入时也会调用 insert
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

        // 验证 UserMapper 的 delete 方法被调用
        verify(userMapper, times(1)).delete(1L); // 确认调用了删除方法一次
    }
}
