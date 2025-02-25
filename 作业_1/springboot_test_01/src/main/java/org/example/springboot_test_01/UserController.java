package org.example.springboot_test_01;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")     // 通过这里配置使下面的映射都在/users下
@Tag(name = "用户管理API", description = "用于管理用户信息的API")
public class UserController {

    // 创建线程安全的Map，模拟users信息的存储
    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    /**
     * 处理"/users/"的GET请求，用来获取用户列表
     *
     * @return
     */
    @Operation(summary = "获取用户列表", description = "返回所有用户的列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取用户列表"),
            @ApiResponse(responseCode = "404", description = "用户列表为空")
    })
    @GetMapping("/")
    public List<User> getUserList() {
        // 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递
        List<User> r = new ArrayList<User>(users.values());
        return r;
    }

    /**
     * 处理"/users/"的POST请求，用来创建User
     *
     * @param user
     * @return
     */
    @Operation(summary = "创建用户", description = "创建一个新的用户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功创建用户"),
            @ApiResponse(responseCode = "400", description = "输入参数无效")
    })
    @PostMapping("/")
    public String postUser(@RequestBody @Schema(description = "用户对象", required = true) User user) {
        // @RequestBody注解用来绑定通过http请求中application/json类型上传的数据
        users.put(user.getId(), user);
        return "success";
    }

    /**
     * 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
     *
     * @param id
     * @return
     */
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取用户信息"),
            @ApiResponse(responseCode = "404", description = "用户不存在")
    })
    @GetMapping("/{id}")
    public User getUser(@PathVariable @Parameter(name = "id", description = "用户ID", required = true) Long id) {
        // url中的id可通过@PathVariable绑定到函数的参数中
        return users.get(id);
    }

    /**
     * 处理"/users/{id}"的PUT请求，用来更新User信息
     *
     * @param id
     * @param user
     * @return
     */
    @Operation(summary = "更新用户信息", description = "根据用户ID更新用户信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功更新用户信息"),
            @ApiResponse(responseCode = "404", description = "用户不存在")
    })
    @PutMapping("/{id}")
    public String putUser(@PathVariable @Parameter(name = "id", description = "用户ID", required = true) Long id,
                          @RequestBody @Schema(description = "用户对象", required = true) User user) {
        User u = users.get(id);
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.put(id, u);
        return "success";
    }

    /**
     * 处理"/users/{id}"的DELETE请求，用来删除User
     *
     * @param id
     * @return
     */
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功删除用户"),
            @ApiResponse(responseCode = "404", description = "用户不存在")
    })
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable @Parameter(name = "id", description = "用户ID", required = true) Long id) {
        users.remove(id);
        return "success";
    }
}

class UserControllerTest {
    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户姓名", example = "张三")
    private String name;

    @Schema(description = "用户年龄", example = "25")
    private Integer age;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
