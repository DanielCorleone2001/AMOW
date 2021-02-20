package com.daniel.service.impl;

import com.daniel.entity.SysDept;
import com.daniel.mapper.SysDeptMapper;
import com.daniel.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: DeptServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/2/20 11:25
 * @Description:
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public List<SysDept> selectAll() {
        List<SysDept> list = sysDeptMapper.selectAll();
        for (SysDept s:
             list) {
            SysDept parent = sysDeptMapper.selectByPrimaryKey(s.getPid());
            if ( parent != null)
                s.setPidName(parent.getName());
        }
        return list;
    }
}
