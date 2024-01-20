package kreadcn.homework.controller.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.NeedNoPrivilege;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.dto.UpdateSelfDTO;
import kreadcn.homework.model.CurrentUser;
import kreadcn.homework.service.SecretAuditService;
import kreadcn.homework.service.TokenService;
import kreadcn.homework.utils.ThreadContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
@BackendModule
public class AuthenticationController {
    @Autowired
    private TokenService tokenService;


    @Autowired
    private SecretAuditService secretAuditService;

    @GetMapping("getCurrentUser")
    @Privilege
    public CurrentUser getCurrentUser() {
        return ThreadContextHolder.getCurrentUser();
    }

    @PostMapping("login")
    @NeedNoPrivilege
    public CurrentUser login(String userId, String password, HttpServletRequest request, HttpServletResponse response) {
        String ipAddress = request.getRemoteAddr();
        ipAddress = ipAddress.replace("[", "").replace("]", "");
        CurrentUser token;
        secretAuditService.checkAbnormal(userId, ipAddress);
        try {
            token = tokenService.login(userId, password, ipAddress);
        } catch (Exception ex) {
            secretAuditService.addErrorLogin(userId, ipAddress);
            throw new RuntimeException(ex);
        }
        Cookie cookie = new Cookie("accessToken", token.getAccessToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return token;
    }

    @PostMapping("updateSelf")
    @Privilege
    public void updateSelf(@RequestBody UpdateSelfDTO selfDTO) {
        tokenService.updateSelf(selfDTO);
    }

    @GetMapping("logout")
    @NeedNoPrivilege
    public void logout() {
        CurrentUser currentUser = ThreadContextHolder.getCurrentUserWithoutAssert();
        if (currentUser == null) {
            return;
        }
        tokenService.logout(currentUser.getAccessToken());
    }
}
