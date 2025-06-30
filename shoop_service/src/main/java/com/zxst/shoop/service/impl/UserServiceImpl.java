package com.zxst.shoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxst.shoop.entity.User;
import com.zxst.shoop.mapper.UserMapper;
import com.zxst.shoop.service.UserService;
import com.zxst.shoop.service.ex.PasswordNotMatchException;
import com.zxst.shoop.service.ex.UserNotFoundException;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean checkUserName(String uname) {
        User user = userMapper.getUserByUserName(uname);
        if (user == null) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean checkoldVal(String oldval, Integer userId) {
        User user = userMapper.findUserById(userId);
        if (user != null) {
            String md5Password = getMd5Password(oldval, user.getSalt());
            if (md5Password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean register(User user) {
       // username   password
        String md5Password = getMd5Password(user.getPassword(), user.getUsername());
        user.setPassword(md5Password);
        user.setSalt(user.getUsername());
        user.setIsDelete(0);
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        user.setModifiedTime(new java.util.Date());
        user.setCreatedTime(new java.util.Date());
        return  userMapper.register(user)>0;
    }

    @Override
    public User login(User user) {
        //需要使用用户名作为条件查询当前用户是否存在
        String username = user.getUsername();
        User one = userMapper.getUserByUserName(username);
        if (one == null) {
            throw new UserNotFoundException("用户不存在");
        }else if (one.getIsDelete() == 1){
            throw new UserNotFoundException("该账号已经冻结");
        }else if (!getMd5Password(user.getPassword(), one.getSalt()).equals(one.getPassword())){
            throw new PasswordNotMatchException("密码输入错误");
        }
        return one;
    }

    @Override
    public boolean modifiedPass(String ps, Integer userId) {
        User user = userMapper.findUserById(userId);
        if (user != null) {
            user.setPassword(getMd5Password(ps, user.getSalt()));
            user.setModifiedUser(user.getUsername());
            user.setModifiedTime(new java.util.Date());
           return userMapper.updateInfo(user)>0;
        }
        return false;
    }

    @Override
    public User showOneUser(Integer userId) {
        return userMapper.findUserById(userId);
    }

    @Override
    public boolean eeditUserInfo(User user) {
        return userMapper.updateInfo(user)>0;
    }

    //使用md5对密码进行盐值加密
    private  String getMd5Password(String password, String salt) {
         //三次加密
        for (int i = 0; i < 3; i++) {
            String newPs = salt + password + salt;
            //md5的16进制方式加密
            password = DigestUtils.md5DigestAsHex(newPs.getBytes()).toUpperCase();
        }
        return password;
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        //分页操作
        PageHelper.startPage(currentPage,pageSize);
        List<User> list = userMapper.findPage(queryString);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }
}
