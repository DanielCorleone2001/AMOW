package com.daniel;


import com.daniel.mapper.SysUserMapper;
import com.daniel.service.redis.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AmowApplicationTests {

    @Test
   public  void contextLoads() {

    }
    @Autowired
    private RedisService redisService;

    @Test
    public void RedisTest() {
        redisService.set("redisTest_1","redisTestSuccess");
        System.out.println(redisService.get("redisTest_1"));
    }

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void SysUserMapperTest() {
        System.out.println(sysUserMapper.selectByUsername("dev"));
        //System.out.println(sysUserMapper.selectByPrimaryKey("1"));
    }
}
