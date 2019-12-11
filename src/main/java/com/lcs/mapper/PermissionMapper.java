package com.lcs.mapper;

import com.lcs.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper
@Component
public interface PermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    Set<Permission> selectPermissionByRoleid(Long roleid);
}