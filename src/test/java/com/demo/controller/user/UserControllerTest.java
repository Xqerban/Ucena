package com.demo.controller.user;

import com.demo.entity.User;
import com.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // --------------------- GET /signup ---------------------
    /**
     * 测试用例：/signup 页面访问（正常情况）
     * 输入：无
     * 操作：GET /signup
     * 期望输出：返回视图 "signup"
     */
    @Test
    public void testSignUp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/signup"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("signup"));
    }

    // --------------------- GET /login ---------------------
    /**
     * 测试用例：/login 页面访问（正常情况）
     * 输入：无
     * 操作：GET /login
     * 期望输出：返回视图 "login"
     */
    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    // --------------------- POST /loginCheck.do ---------------------
    /**
     * 测试用例：/loginCheck.do 用户登录（正常情况-普通用户）
     * 输入：userID、password
     * 操作：POST /loginCheck.do
     * 期望输出：返回 "/index"，同时在 session 中设置 user
     */
    @Test
    public void testLoginCheckUserSuccess() throws Exception {
        User user = new User();
        user.setUserID("user1");
        user.setPassword("pass");
        user.setIsadmin(0);
        when(userService.checkLogin("user1", "pass")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/loginCheck.do")
                        .param("userID", "user1")
                        .param("password", "pass"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("/index"));
    }

    /**
     * 测试用例：/loginCheck.do 用户登录（正常情况-管理员）
     * 输入：admin的 userID、password
     * 操作：POST /loginCheck.do
     * 期望输出：返回 "/admin_index"，同时在 session 中设置 admin
     */
    @Test
    public void testLoginCheckAdminSuccess() throws Exception {
        User admin = new User();
        admin.setUserID("admin1");
        admin.setPassword("adminpass");
        admin.setIsadmin(1);
        when(userService.checkLogin("admin1", "adminpass")).thenReturn(admin);

        mockMvc.perform(MockMvcRequestBuilders.post("/loginCheck.do")
                        .param("userID", "admin1")
                        .param("password", "adminpass"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("/admin_index"));
    }

    /**
     * 测试用例：/loginCheck.do 用户登录（异常情况）
     * 输入：错误的密码或不存在的用户
     * 操作：POST /loginCheck.do
     * 期望输出：返回 "false"
     */
    @Test
    public void testLoginCheckFailure() throws Exception {
        when(userService.checkLogin("user1", "wrongpass")).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/loginCheck.do")
                        .param("userID", "user1")
                        .param("password", "wrongpass"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    // --------------------- POST /register.do ---------------------
    /**
     * 测试用例：/register.do 用户注册（正常情况）
     * 输入：userID、userName、password、email、phone
     * 操作：POST /register.do
     * 期望输出：重定向到 "login" 页面
     */
    @Test
    public void testRegisterSuccess() throws Exception {
        // 模拟创建用户成功，返回 1
        when(userService.create(any(User.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/register.do")
                        .param("userID", "user1")
                        .param("userName", "Test User")
                        .param("password", "pass")
                        .param("email", "test@example.com")
                        .param("phone", "1234567890"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("login"));
    }


    /**
     * 测试用例：/register.do 用户注册（异常情况）
     * 输入：userID、userName、password、email、phone
     * 操作：POST /register.do，模拟 userService.create 抛出异常
     * 期望输出：抛出 RuntimeException
     */
    @Test
    public void testRegisterThrowsException() {
        Mockito.doThrow(new RuntimeException("注册失败")).when(userService).create(any(User.class));

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/register.do")
                    .param("userID", "user1")
                    .param("userName", "Test User")
                    .param("password", "pass")
                    .param("email", "test@example.com")
                    .param("phone", "1234567890"));
        });
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("注册失败", exception.getCause().getMessage());
    }

    // --------------------- GET /logout.do ---------------------
    /**
     * 测试用例：/logout.do 用户登出（正常情况）
     * 输入：session 中包含 user 对象
     * 操作：GET /logout.do
     * 期望输出：重定向到 "/index"，并移除 session 中的 user
     */
    @Test
    public void testLogoutSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/logout.do").sessionAttr("user", new User()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/index"));
    }

    // --------------------- GET /quit.do ---------------------
    /**
     * 测试用例：/quit.do 管理员退出（正常情况）
     * 输入：session 中包含 admin 对象
     * 操作：GET /quit.do
     * 期望输出：重定向到 "/index"，并移除 session 中的 admin
     */
    @Test
    public void testQuitSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/quit.do").sessionAttr("admin", new User()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/index"));
    }

    // --------------------- POST /updateUser.do ---------------------
    /**
     * 测试用例：/updateUser.do 更新用户信息（正常情况）
     * 输入：userName、userID、passwordNew、email、phone、picture 文件为空（从而跳过调用 FileUtil.saveUserFile）
     * 操作：POST /updateUser.do（multipart 方式上传）
     * 期望输出：重定向到 "user_info"，并更新 session 中的 user
     */
    @Test
    public void testUpdateUserSuccess() throws Exception {
        User user = new User();
        user.setUserID("user1");
        user.setUserName("Old Name");
        user.setPassword("oldpass");
        user.setEmail("old@example.com");
        user.setPhone("000");
        user.setPicture("");
        when(userService.findByUserID("user1")).thenReturn(user);

        // 模拟文件参数，传入空的 originalFilename，避免进入调用 FileUtil.saveUserFile 的分支
        MockMultipartFile file = new MockMultipartFile("picture", "", "image/jpeg", "test image content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/updateUser.do")
                        .file(file)
                        .param("userName", "New Name")
                        .param("userID", "user1")
                        .param("passwordNew", "newpass")
                        .param("email", "new@example.com")
                        .param("phone", "1234567890"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("user_info"));
    }

    /**
     * 测试用例：/updateUser.do 更新用户信息（异常情况）
     * 输入：userID不存在（userService.findByUserID 返回 null）
     * 操作：POST /updateUser.do
     * 期望输出：抛出 NullPointerException
     */
    @Test
    public void testUpdateUserThrowsException() {
        when(userService.findByUserID("user1")).thenReturn(null);
        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.multipart("/updateUser.do")
                    .file(new MockMultipartFile("picture", "test.jpg", "image/jpeg", "test image content".getBytes()))
                    .param("userName", "New Name")
                    .param("userID", "user1")
                    .param("passwordNew", "newpass")
                    .param("email", "new@example.com")
                    .param("phone", "1234567890"));
        });
        assertInstanceOf(NullPointerException.class, exception.getCause());
    }

    // --------------------- GET /checkPassword.do ---------------------
    /**
     * 测试用例：/checkPassword.do 校验密码（正常情况-密码匹配）
     * 输入：userID、password
     * 操作：GET /checkPassword.do
     * 期望输出：返回 true
     */
    @Test
    public void testCheckPasswordTrue() throws Exception {
        User user = new User();
        user.setUserID("user1");
        user.setPassword("pass");
        when(userService.findByUserID("user1")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/checkPassword.do")
                        .param("userID", "user1")
                        .param("password", "pass"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    /**
     * 测试用例：/checkPassword.do 校验密码（正常情况-密码不匹配）
     * 输入：userID、password
     * 操作：GET /checkPassword.do
     * 期望输出：返回 false
     */
    @Test
    public void testCheckPasswordFalse() throws Exception {
        User user = new User();
        user.setUserID("user1");
        user.setPassword("pass");
        when(userService.findByUserID("user1")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/checkPassword.do")
                        .param("userID", "user1")
                        .param("password", "wrongpass"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    // --------------------- GET /user_info ---------------------
    /**
     * 测试用例：/user_info 用户信息页面访问（正常情况）
     * 输入：无
     * 操作：GET /user_info
     * 期望输出：返回视图 "user_info"
     */
    @Test
    public void testUserInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user_info")
                        .sessionAttr("user", new User()))  // 确保 session 里有 user
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user_info"));
    }

}
