package com.lcs.entity;


public class Permission {
    private Long id;

    private String permissionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }
    public Permission() {}
    public Permission(Long id, String permissionName) {
        this.id = id;
        this.permissionName = permissionName;
    }
}