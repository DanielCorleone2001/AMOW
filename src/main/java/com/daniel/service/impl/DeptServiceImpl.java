package com.daniel.service.impl;

import com.daniel.contains.Constant;
import com.daniel.entity.SysDept;
import com.daniel.entity.SysUser;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.mapper.SysDeptMapper;
import com.daniel.mapper.SysUserMapper;
import com.daniel.service.DeptService;
import com.daniel.service.RedisService;
import com.daniel.service.UserService;
import com.daniel.utils.CodeUtil;
import com.daniel.vo.request.dept.DeptAddReqVO;
import com.daniel.vo.request.dept.DeptUpdateReqVO;
import com.daniel.vo.response.dept.DeptRespVO;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserService userService;

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
    public List<DeptRespVO> deptTreeList(String deptID) {
        List<SysDept> list = sysDeptMapper.selectAll();

        if ( !StringUtils.isEmpty(deptID) && !list.isEmpty() ) {//删除某个部门的叶子节点
            for ( SysDept dept : list ) {
                if ( dept.getId().equals(deptID) ) {
                    list.remove(dept);
                    break;
                }
            }
        }

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
    public SysDept addDept(DeptAddReqVO deptAddVO) {
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeptInfo(DeptUpdateReqVO deptUpdateReqVO) {
        //获取想要更新的部门
        SysDept targetDept = sysDeptMapper.selectByPrimaryKey(deptUpdateReqVO.getId());

        //非空检验
        if ( targetDept == null ) {
            log.error("传入的部门ID {} 不合法",deptUpdateReqVO.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        SysDept updateDept = new SysDept();
        BeanUtils.copyProperties(deptUpdateReqVO,updateDept);
        updateDept.setUpdateTime(new Date());
        //检查是否成功更新
        if ( sysDeptMapper.updateByPrimaryKeySelective(updateDept) != 1 ) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }

        //层级关系发生了变化的情况
        if ( !deptUpdateReqVO.getPid().equals(targetDept.getPid()) ) {
            //获取父级部门
            SysDept parent = sysDeptMapper.selectByPrimaryKey(deptUpdateReqVO.getPid());

            /**
             * 自相矛盾的情况。前者意思是该部门父级不是顶层部门，而后者意思是该部门的父级是顶层部门
             */
            if ( !deptUpdateReqVO.getPid().equals("0") && parent==null ) {
                log.error("传入的部门ID {} 不合法");
                throw new BusinessException(BaseResponseCode.DATA_ERROR);
            }

            //获取之前的父级部门
            SysDept oldParent = sysDeptMapper.selectByPrimaryKey(targetDept.getPid());
            String oldRelationCode;
            String newRelationCode;

            //根据情乱改变根目录
            if ( targetDept.getPid().equals("0") ) {
                oldRelationCode = targetDept.getDeptNo();
                newRelationCode = parent.getRelationCode() + targetDept.getDeptNo();
            } else if ( deptUpdateReqVO.getPid().equals("0") ) {
                oldRelationCode = targetDept.getRelationCode();
                newRelationCode = targetDept.getDeptNo();
            } else {
                oldRelationCode = oldParent.getRelationCode();
                newRelationCode = parent.getRelationCode();
            }

            sysDeptMapper.updateRelationCode(oldRelationCode,newRelationCode,targetDept.getRelationCode());
        }
    }

    @Override
    public void deleteDept(String deptID) {
        //获取目标部门
        SysDept targetDept = sysDeptMapper.selectByPrimaryKey(deptID);

        if ( targetDept == null ) {
            log.error("传入的部门ID {} 不合法",deptID);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        List<String> deptIdList = sysDeptMapper.selectAllChildrenIdList(targetDept.getRelationCode());
        List<SysUser> userList = sysUserMapper.selectUserListByDeptIdList(deptIdList);

        if ( !userList.isEmpty() ) {
            throw new BusinessException(BaseResponseCode.NOT_PERMISSION_DELETED_DEPT);
        }

        targetDept.setDeleted(0);
        targetDept.setUpdateTime(new Date());

        if ( sysDeptMapper.updateByPrimaryKeySelective(targetDept) != 1 ) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
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
