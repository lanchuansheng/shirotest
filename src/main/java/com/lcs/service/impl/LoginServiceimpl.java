package com.lcs.service.impl;

import com.lcs.entity.Permission;
import com.lcs.entity.Role;
import com.lcs.entity.User;
import com.lcs.mapper.PermissionMapper;
import com.lcs.mapper.RoleMapper;
import com.lcs.mapper.UserMapper;
import com.lcs.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoginServiceimpl implements LoginService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public User getUserByName(String getMapByName) {
        return getMapByName(getMapByName);
    }
    /**
     * 数据库查询
     * @param username
     * @return
     */
    private User getMapByName(String username) {
        User user = userMapper.selectUser(username);
        Set<Role> roles = roleMapper.selectRoleByUserid(user.getId());
        for (Role role : roles) {
            Set<Permission> permissions = permissionMapper.selectPermissionByRoleid(role.getId());
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }
}
