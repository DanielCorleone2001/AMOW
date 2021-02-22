package com.daniel.service;

import com.daniel.entity.SysDept;
import com.daniel.vo.request.dept.DeptAddReqVO;
import com.daniel.vo.request.dept.DeptUpdateReqVO;
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

    //获取部门的树形结构列表
    List<DeptRespVO> deptTreeList(String deptID);

    //新增部门
    SysDept addDept(DeptAddReqVO deptAddVO);

    void updateDeptInfo(DeptUpdateReqVO deptUpdateReqVO);
}
