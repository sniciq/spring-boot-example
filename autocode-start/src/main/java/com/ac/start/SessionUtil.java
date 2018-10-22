package com.ac.start;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static void login(String userName) {
        getSession().setAttribute("SessionUser", userName);
    }

    public static void logout() {
        getSession().removeAttribute("SessionUser");
        getSession().invalidate();
    }

    public static String getLoginUser() {
        String userName = (String) getSession().getAttribute("SessionUser");
        return userName;
    }

    public static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession();
    }
}
