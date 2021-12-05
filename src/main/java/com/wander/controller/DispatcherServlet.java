package com.wander.controller;

import com.wander.controller.frontend.MainPageController;
import com.wander.controller.superadmin.HeadLineOperationController;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        String reqMethod = req.getMethod();
        log.debug("servlet path = {} request method = {}", servletPath, reqMethod);

        if (req.getServletPath().equals("/frontend/main") && req.getMethod().equals("GET")) {
            new MainPageController().getMainPageInfo(req, resp);
        } else if (req.getServletPath().equals("/super/admin/headline/add") && req.getMethod().equals("GET")) {
            new HeadLineOperationController().addHeadLine(req, resp);
        }

    }
}
