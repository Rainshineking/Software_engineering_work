package org.example.springboot_test_01;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "用户管理API", description = "用于管理用户信息的API")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Operation(summary = "获取用户列表", description = "返回所有用户的列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取用户列表"),
            @ApiResponse(responseCode = "404", description = "用户列表为空")
    })
    @GetMapping("/")
    public List<User> getUserList() {
        return userMapper.findAll();
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取用户信息"),
            @ApiResponse(responseCode = "404", description = "用户不存在")
    })
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userMapper.findById(id);
    }

    @Operation(summary = "创建用户", description = "创建一个新的用户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功创建用户"),
            @ApiResponse(responseCode = "400", description = "输入参数无效")
    })
    @PostMapping("/")
    public String postUser(@RequestBody User user) {
        userMapper.insert(user);
        return "success";
    }

    @Operation(summary = "更新用户信息", description = "根据用户ID更新用户信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功更新用户信息"),
            @ApiResponse(responseCode = "404", description = "用户不存在")
    })
    @PutMapping("/{id}")
    public String putUser(@PathVariable Long id, @RequestBody User user) {
        User u = userMapper.findById(id);  // 查找要更新的用户
        if (u != null) {
            // 设置新数据
            u.setName(user.getName());
            u.setAge(user.getAge());
            userMapper.update(u);  // 更新数据库中的用户
            return "success";
        } else {
            return "用户不存在";
        }
    }

    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功删除用户"),
            @ApiResponse(responseCode = "404", description = "用户不存在")
    })
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        User u = userMapper.findById(id);
        if (u != null) {
            userMapper.delete(id);
            return "success";
        } else {
            return "用户不存在";
        }
    }
}
