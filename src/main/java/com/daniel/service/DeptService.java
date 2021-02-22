package com.daniel.service;

import com.daniel.entity.SysDept;
import com.daniel.vo.request.dept.DeptAddVO;
import com.daniel.vo.response.dept.DeptRespVO;

import java.util.List;

/**
 * @Package: com.daniel.service
 * @ClassName: DeptService
 * @Author: daniel
 * @CreateTime: 2021/2/20 11:24
 * @Description:
 */
public interface DeptService {

    List<SysDept> selectAll();

    List<DeptRespVO> deptTreeList();

    SysDept addDept(DeptAddVO deptAddVO);
}
