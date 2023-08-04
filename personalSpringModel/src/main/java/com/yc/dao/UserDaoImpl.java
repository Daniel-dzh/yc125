package com.yc.dao;

import org.ycframework.annotation.YcRepository;

@YcRepository
public class UserDaoImpl implements UserDao{

    @Override
    public void add(String uname) {
        System.out.println("UserDao添加了新用户:"+uname);
    }
}
