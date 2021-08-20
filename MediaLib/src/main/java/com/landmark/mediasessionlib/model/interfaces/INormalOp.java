package com.landmark.mediasessionlib.model.interfaces;


import java.util.List;

public interface INormalOp<T> {

    /**
     * 插入记录，如果表未创建，先创建表
     */
    public boolean insert(T pEntity);

    /**
     * 开启事务插入多条数据
     */
    public boolean insertMultiple(final List<T> pEntityList);

    /**
     * 修改一条数据
     */
    public boolean update(T entity);

    /**
     * 删除单条记录
     */
    public boolean delete(T entity);

    /**
     * 删除所有记录
     */
    public boolean deleteAll();

    /**
     * 查询所有记录
     */
    public List<T> queryAll();

    /**
     * 根据主键id查询记录
     */
    public T queryById(long key);

}
