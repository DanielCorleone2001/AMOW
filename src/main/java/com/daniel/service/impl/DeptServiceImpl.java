package com.daniel.service.impl;

import com.daniel.contains.Constant;
import com.daniel.entity.SysDept;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.mapper.SysDeptMapper;
import com.daniel.service.DeptService;
import com.daniel.service.RedisService;
import com.daniel.utils.CodeUtil;
import com.daniel.vo.request.DeptAddVO;
import com.daniel.vo.response.DeptRespVO;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: DeptServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/2/20 11:25
 * @Description:
 */
@Service
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private RedisService redisService;

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

    @Override
    public List<DeptRespVO> deptTreeList() {
        List<SysDept> list = sysDeptMapper.selectAll();

        DeptRespVO dept = new DeptRespVO();
        dept.setId("0");
        dept.setTitle("默认顶级菜单");
        dept.setSpread(true);
        dept.setChildren(getTreeList(list));
        List<DeptRespVO> result = new ArrayList<>();
        result.add(dept);
        return result;
    }

    /**
     *  添加部门功能实现方法
     * @param deptAddVO
     * @return
     */
    @Override
    public SysDept addDept(DeptAddVO deptAddVO) {
        String relationCode;
        long result = redisService.incrby(Constant.DEPT_CODE_KEY,1);
        String deptCode = CodeUtil.deptCode(String.valueOf(result),6,"0");
        SysDept parent = sysDeptMapper.selectByPrimaryKey(deptAddVO.getPid());
        if ( deptAddVO.getPid().equals("0")) {//如果是顶级菜单
            relationCode = deptCode;
        } else if ( parent == null ) {
            log.error("传入的pid:{}不合法",deptAddVO.getPid());
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        } else {
            relationCode = parent.getRelationCode()+deptCode;
        }
        //配置基本的信息
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(deptAddVO,sysDept);
        sysDept.setCreateTime(new Date());
        sysDept.setId(UUID.randomUUID().toString());
        sysDept.setDeptNo(deptCode);
        sysDept.setRelationCode(relationCode);
        if ( sysDeptMapper.insertSelective(sysDept)  != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
        return sysDept;
    }


    public List<DeptRespVO> getTreeList(List<SysDept> deptList) {
        List<DeptRespVO> list = new ArrayList<>();

        for ( SysDept dept : deptList) {
            if ( dept.getPid().equals("0")) {
                DeptRespVO deptTree = new DeptRespVO();
                BeanUtils.copyProperties(dept,deptTree);
                deptTree.setTitle(dept.getName());
                deptTree.setSpread(true);
                deptTree.setChildren(getChileList(dept.getId(),deptList));
                list.add(deptTree);
            }
        }
        return list;
    }

    public List<DeptRespVO> getChileList(String id, List<SysDept> deptList) {
        List<DeptRespVO> list = new ArrayList<>();

        for ( SysDept dept: deptList ) {
            if ( dept.getPid().equals(id)) {
                DeptRespVO childTree = new DeptRespVO();
                BeanUtils.copyProperties(dept,childTree);
                childTree.setTitle(dept.getName());
                childTree.setChildren(getChileList(dept.getId(),deptList));
                list.add(childTree);
            }
        }
        return list;
    }
}
