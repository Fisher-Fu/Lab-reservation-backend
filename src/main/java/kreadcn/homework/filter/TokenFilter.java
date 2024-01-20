package kreadcn.homework.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import kreadcn.homework.model.CurrentUser;
import kreadcn.homework.service.TokenService;
import kreadcn.homework.utils.ThreadContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author 李洪文
 * @description
 * @date 2019/12/12 9:28
 */
@Component
@Slf4j
public class TokenFilter implements Filter {

    @Autowired
    private TokenService tokenService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestWrapper request = new RequestWrapper((HttpServletRequest) servletRequest);
        // 处理令牌
        String accessToken = request.getAccessToken();
        CurrentUser token = null;
        if (!accessToken.isEmpty()) {
            token = tokenService.getToken(accessToken);
            if (token != null) {
                token.setLastAction(new Date());
            }
        }

        ThreadContextHolder.setCurrentUser(token);
        filterChain.doFilter(request.getRequest(), servletResponse);
    }
}
