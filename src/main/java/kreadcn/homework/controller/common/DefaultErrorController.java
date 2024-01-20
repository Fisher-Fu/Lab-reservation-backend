/*
 * Copyright 2020-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kreadcn.homework.controller.common;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Steve Riesenberg
 * @since 1.1
 */
@Controller
public class DefaultErrorController implements ErrorController {

    @RequestMapping("/error")
    public void handleError(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String errorMessage = getErrorMessage(request);
        if (!StringUtils.hasText(errorMessage) && request.getAttribute("jakarta.servlet.error.exception") instanceof Exception e) {
            errorMessage = e.getMessage();
        }

        if (!StringUtils.hasText(errorMessage)) {
            Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
            errorMessage = "HTTP " + statusCode;
        }

        if (errorMessage != null && errorMessage.startsWith("[access_denied]")) {
            model.addAttribute("errorTitle", "Access Denied");
            model.addAttribute("errorMessage", "You have denied access.");
        } else {
            model.addAttribute("errorTitle", "Error");
            model.addAttribute("errorMessage", errorMessage);
        }

        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html><body><H2>发生错误</H2><p>错误：" + errorMessage + "<p></body></html>");
    }

    private String getErrorMessage(HttpServletRequest request) {
        return (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
    }

}
