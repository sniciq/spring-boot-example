package com.ac.start.ctrl;

import com.ac.common.util.JsonResult;
import com.ac.start.SessionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class AppCtrl {

    @RequestMapping("/")
    public String app(HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmSSS");
        request.setAttribute("sysVersion", df.format(new Date()));
        return "views/app";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmSSS");
        request.setAttribute("sysVersion", df.format(new Date()));
        return "views/login";
    }

    @ResponseBody
    @RequestMapping(value="/sys/login")
    public JsonResult login(@RequestBody Map<String, String> paraMap) {
        String userNmae = paraMap.get("username");
        SessionUtil.login(userNmae);
        return JsonResult.success();
    }

    @RequestMapping(value="/sys/logout")
    public String login() {
        SessionUtil.logout();
        return "redirect:/login";
    }

    @ResponseBody
    @RequestMapping("/sys/route")
    public List<Map<String, String>> route(HttpServletRequest request) throws Exception {
        List<Map<String, String>> routeList = Lists.newArrayList();
        Map<String, String> route = null;

        route = Maps.newHashMap();
        routeList.add(route);
        route.put("name", "客户管理");
        route.put("ctrl", "CustomerCtrl");
        route.put("path", "/admin/Customer");
        route.put("templateUrl", request.getContextPath() + "/modules/admin/CustomerListTpl.html?v=");
        route.put("files", request.getContextPath() + "/modules/admin/CustomerCtrl.js");

        route = Maps.newHashMap();
        routeList.add(route);
        route.put("name", "员工客户统计");
        route.put("ctrl", "UserCustomerCtrl");
        route.put("path", "/module/UserCustomer");
        route.put("templateUrl", request.getContextPath() + "/modules/module/UserCustomerListTpl.html?v=");
        route.put("files", request.getContextPath() + "/modules/module/UserCustomerCtrl.js");

        route = Maps.newHashMap();
        routeList.add(route);
        route.put("name", "员工管理");
        route.put("ctrl", "StaffCtrl");
        route.put("path", "/admin/staff");
        route.put("templateUrl", request.getContextPath() + "/modules/admin/StaffListTpl.html?v=");
        route.put("files", request.getContextPath() + "/modules/admin/StaffCtrl.js");
        return routeList;
    }

    @ResponseBody
    @RequestMapping("/sys/menus")
    public List<Map<String, Object>> ngMenus() throws Exception {
        List<Map<String, Object>> menuList = Lists.newArrayList();
        Map<String, Object> menu = null;
        Map<String, String> subMenu = null;
        List<Map<String, String>> subMenuList = null;

        menu = Maps.newHashMap();
        menuList.add(menu);
        menu.put("name", "后台管理");
        menu.put("type", "toggle");
        menu.put("showTip", "false");
        menu.put("icon", "fa-folder");

        subMenuList = Lists.newArrayList();
        menu.put("subMenus", subMenuList);

        subMenu = Maps.newHashMap();
        subMenu.put("name", "客户管理");
        subMenu.put("type", "link");
        subMenu.put("showTip", "false");
        subMenu.put("icon", "fa-file-o");
        subMenu.put("path", "/admin/Customer");
        subMenuList.add(subMenu);

        subMenu = Maps.newHashMap();
        subMenu.put("name", "员工管理");
        subMenu.put("type", "link");
        subMenu.put("showTip", "false");
        subMenu.put("icon", "fa-file-o");
        subMenu.put("path", "/admin/staff");
        subMenuList.add(subMenu);


        menu = Maps.newHashMap();
        menuList.add(menu);
        menu.put("name", "员工客户统计");
        menu.put("type", "link");
        menu.put("showTip", "false");
        menu.put("icon", "fa-file-o");
        menu.put("path", "/module/UserCustomer");


        return menuList;
    }
}
