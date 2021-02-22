package com.daniel.mapper;

import com.daniel.entity.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> selectAll();

    //更新部门信息,维护层级关系
    int updateRelationCode(@Param("oldStr") String oldStr, @Param("newStr") String newStr, @Param("relationCode") String relationCode);

    //根据relationCode来查找所有叶子节点
    List<String> selectAllChildrenIdList(String relationCode);

    //删除某个部门以及它的子集部门
    int deleteDept(@Param("updateTime")Date updateTime,@Param("list")List<String> list);
}