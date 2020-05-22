package com.copote.wechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.copote.wechat.dao.CustomerDao;
import com.copote.wechat.entity.Customer;
import com.copote.wechat.service.CustomerService;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @create 2020/5/22
 * @Description:
 * @since 1.0.0
 */
@Service("customerService")
public class CustomerServiceImpl extends ServiceImpl<CustomerDao, Customer> implements CustomerService {
}
