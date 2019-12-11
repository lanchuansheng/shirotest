package com.lcs.entity;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

public class Role {
    private Long id;

    private String roleName;

    private Set<Permission> permissions;

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Role(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
}