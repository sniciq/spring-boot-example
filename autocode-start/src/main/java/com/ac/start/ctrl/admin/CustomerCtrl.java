package com.ac.start.ctrl.admin;

import com.ac.common.util.CommonForm;
import com.ac.common.util.JsonResult;
import com.ac.common.util.Limit;
import com.ac.dao.entity.CustomerEty;
import com.ac.dao.mapper.demo.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 客户管理
 */
@Controller
@RequestMapping("/admin/CustomerCtrl/")
public class CustomerCtrl {

    @Autowired
    private CustomerMapper customerMapper;


    /**
     * 查询
     */
    @ResponseBody
    @RequestMapping(value="search")
    public JsonResult search(@RequestBody CommonForm<CustomerEty> form) throws Exception {
        int count = customerMapper.selectCount(form.getData());
        List<CustomerEty> list = customerMapper.selectByLimit(form.getData(), form.getExtLimit());
        return JsonResult.pager(list, count);
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequestMapping(value="save")
    public JsonResult save(@RequestBody CustomerEty customerEty) throws Exception {
        if(customerEty.getId() == null) {
            customerMapper.insert(customerEty);
        }
        else {
            customerMapper.updateById(customerEty);
        }
        return JsonResult.success();
    }

    /**
     * 删除
     */
    @ResponseBody
    @RequestMapping(value="delete")
    public JsonResult delete(@RequestParam("id") long id) {
        customerMapper.deleteById(id);
        return JsonResult.success();
    }

    /**
     * 得到详细信息
     */
    @ResponseBody
    @RequestMapping(value="getDetailInfo")
    public JsonResult getDetailInfo(@RequestParam("id") long id) throws Exception {
        CustomerEty customerEty = customerMapper.selectById(id);
        return JsonResult.success(customerEty);
    }

}