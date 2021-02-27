package com.daniel.service.impl;

import com.daniel.entity.SysUser;
import com.daniel.mapper.SysUserMapper;
import com.daniel.service.home.HomeService;
import com.daniel.service.permission.PermissionService;
import com.daniel.vo.response.home.HomeRespVO;
import com.daniel.vo.response.permission.PermissionRespNodeVO;
import com.daniel.vo.response.user.UserInfoRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: HomeServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/2/1 22:40
 * @Description:
 */

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PermissionService permissionService;

    /**
     * 通过传入用户的ID，查询用户拥有的权限，进而通过权限展示主页的左侧菜单栏
     * @param userId 传入的用户ID
     * @return
     */
    @Override
    public HomeRespVO getHomeInfo(String userId) {
        HomeRespVO homeRespVO = new HomeRespVO();
        //构造假数据，后期是从数据库中获取用户的信息
        //String home="[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\": [],\"id\":\"6\",\"title\":\"五级类目5-6\",\"url\":\"string\"}],\"id\":\"5\",\"title\":\"四级类目4- 5\",\"url\":\"string\"}],\"id\":\"4\",\"title\":\"三级类目3- 4\",\"url\":\"string\"}],\"id\":\"3\",\"title\":\"二级类目2- 3\",\"url\":\"string\"}],\"id\":\"1\",\"title\":\"类目1\",\"url\":\"string\"},{\"children\": [],\"id\":\"2\",\"title\":\"类目2\",\"url\":\"string\"}]";
//        String home="[\n" +
//                "    {\n" +
//                "        \"children\": [\n" +
//                "            {\n" +
//                "                \"children\": [],\n" +
//                "                \"id\": \"3\",\n" +
//                "                \"title\": \"菜单权限管理\",\n" +
//                "                \"url\": \"/index/menus\"\n" +
//                "            }\n" +
//                "        ],\n" +
//                "        \"id\": \"1\",\n" +
//                "        \"title\": \"组织管理\",\n" +
//                "        \"url\": \"string\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"children\": [],\n" +
//                "        \"id\": \"2\",\n" +
//                "        \"title\": \"类目2\",\n" +
//                "        \"url\": \"string\"\n" +
//                "    }\n" +
//                "]";
        //List<PermissionRespNodeVO> list = JSON.parseArray(home,PermissionRespNodeVO.class);//切分数据成我们的权限信息数组
        List<PermissionRespNodeVO> list  = permissionService.permissionTreeList(userId);
        homeRespVO.setMenus(list);

        UserInfoRespVO userInfoRespVO = new UserInfoRespVO();

        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);//获取id对应的用户
        if ( sysUser != null ) {
            userInfoRespVO.setUsername(sysUser.getUsername());
            userInfoRespVO.setDeptName("教务部");
            userInfoRespVO.setId(sysUser.getId());
        }
        homeRespVO.setUserInfoVO(userInfoRespVO);
        return homeRespVO;
    }
}
