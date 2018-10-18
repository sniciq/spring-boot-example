package com.ac.dao.mapper.base;


import java.util.List;

import com.ac.common.util.Limit;
import org.apache.ibatis.annotations.Param;

public interface BaseMapper<T> {

    /**
     * 插入
     * @param t
     * @return
     */
    Long insert(T t);

    /**
     * 更新
     * @param t
     * @return
     */
    Long updateById(T t);

    /**
     * 按ID删除
     * @param id
     */
    void deleteById(Long id);

    /**
     * 按ID查询
     * @param id
     * @return
     */
    T selectById(Long id);

    /**
     * 按ID查询多个
     * @param ids
     * @return
     */
    List<T> selectByIds(List<Long> ids);

    /**
     * 按实体查询
     * @param t
     * @return
     */
    List<T> selectByEntity(T t);

    /**
     * 查询记录数，可为分页查询
     * @param t
     * @return
     */
    Long selectCount(T t);

    /**
     * 按分页查询
     * @param t
     * @param limit
     * @return
     */
    List<T> selectByLimit(@Param("ety") T t, @Param("limit") Limit limit);
}
