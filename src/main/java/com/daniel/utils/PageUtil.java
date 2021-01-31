package com.daniel.utils;

import com.daniel.vo.response.PageVO;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Package: com.daniel.utils
 * @ClassName: PageUtil
 * @Author: daniel
 * @CreateTime: 2021/1/31 17:56
 * @Description: 页面封装数据的工具类
 */
public class PageUtil {

    private PageUtil(){
    }

    public static <T>PageVO getPageVO(List<T> list ) {
        PageVO<T> pageVO = new PageVO<>();

        if ( list instanceof Page) {//判断是否是分页的实例
            Page page = (Page) list;//强转成Page类型，就能调用Page的方法来注入属性
            pageVO.setTotalRows(page.getTotal());//设置总的行数
            pageVO.setList(page.getResult());//设置list
            pageVO.setTotalPages(page.getPages());//设置总的页数
            pageVO.setCurPageSize(page.size());//设置当前页的大小
            pageVO.setPageNum(page.getPageNum());//设置页数
            pageVO.setPageSize(page.getPageSize());//设置页面size
        }
        return pageVO;
    }
}
