package com.example.calendar;

import com.example.calendar.controller.UserController;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // 전체 애플리케이션 컨텍스트 로드
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        // Mock 객체 초기화
    }

    @Test
    public void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/api/calendar/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER") // 인증된 사용자로 테스트
    public void testShowResetPasswordPage_withValidUsername() throws Exception {
        String validUsername = "existingUser";
        
        when(userService.findUserByUsername(validUsername)).thenReturn("validUser");

        mockMvc.perform(get("/api/calendar/resetPasswordPage")
                        .param("username", validUsername))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/calendar/resetPasswordPage-success?username=" + validUsername));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER") // 인증된 사용자로 테스트
    public void testShowResetPasswordPage_withInvalidUsername() throws Exception {
        String invalidUsername = "nonExistentUser";

        when(userService.findUserByUsername(invalidUsername)).thenReturn(null);

        mockMvc.perform(get("/api/calendar/resetPasswordPage")
                        .param("username", invalidUsername))
                .andExpect(status().isOk())
                .andExpect(view().name("resetPasswordPage"))
                .andExpect(model().attribute("error", "아이디를 찾을 수 없습니다."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER") // 인증된 사용자로 테스트
    public void testProcessResetPasswordPage_withValidUsername() throws Exception {
        String validUsername = "validUser";

        when(userService.findUserByUsername(validUsername)).thenReturn("validUser");

        mockMvc.perform(post("/api/calendar/resetPasswordPage")
                        .param("username", validUsername))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/calendar/resetPasswordPage-success?username=" + validUsername));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER") // 인증된 사용자로 테스트
    public void testHandleResetPasswordSuccess_withPasswordMismatch() throws Exception {
        String username = "validUser";
        String newPassword = "newPassword123";
        String confirmPassword = "differentPassword123";

        mockMvc.perform(post("/api/calendar/resetPasswordPage-success")
                        .param("username", username)
                        .param("newPassword", newPassword)
                        .param("confirmPassword", confirmPassword))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password-success"))
                .andExpect(model().attribute("error", "비밀번호 확인이 일치하지 않습니다."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER") // 인증된 사용자로 테스트
    public void testShowResetPasswordSuccessPage() throws Exception {
        String username = "validUser";

        mockMvc.perform(get("/api/calendar/resetPasswordPage-success")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password-success"))
                .andExpect(model().attribute("username", username));
    }
}
