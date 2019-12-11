package com.lcs.common;

import java.util.List;

/**
 * Created by lowdad on 18-1-23.
 */
public interface BaseMapper<T> {
    int deleteByPrimaryKey(String id);

    int insert(T t);

    T selectByPrimaryKey(String id);

    List<T> selectAll();

    int updateByPrimaryKey(T t);

    List<T> selectByTypeList(Integer type);
}
