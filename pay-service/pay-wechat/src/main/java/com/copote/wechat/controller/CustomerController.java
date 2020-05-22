package com.copote.wechat.controller;

import cn.hutool.core.util.ObjectUtil;
import com.copote.common.exception.R;
import com.copote.wechat.entity.Customer;
import com.copote.wechat.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @create 2020/5/22
 * @Description:
 * @since 1.0.0
 */
@RestController
@Slf4j
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 查询客户类型
     * @return
     */
    @RequestMapping("/select")
    public R selectCustomer(@RequestParam String ccXzsxlx){
        Customer customer = customerService.getById(ccXzsxlx);
        if(ObjectUtil.isNull(customer)){
            return R.error("客户不存在："+ccXzsxlx);
        }
        return R.ok().put("data",customer);
    }
}
