package com.wenx.demo.study.config.daoutils;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2020/1/13 20:32
 * @Modified By：
 */
@Mapper
@Repository
public interface TestMapper {

    /**
     * 查询table_test表记录
     *
     * @param id ID
     * @return TestDO
     */
    @Select("select id,name from table_test where id = #{id}")
    TestDO selectTestById(@Param("id") Integer id);

}
