package com.daniel.serializer;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Package: com.daniel.serializer
 * @ClassName: MyStringRedisSerializer
 * @Author: daniel
 * @CreateTime: 2021/1/30 12:49
 * @Description:
 */
public class MyStringRedisSerializer implements RedisSerializer<Object> {
    private final Charset charset;
    public MyStringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }
    public MyStringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }
    @Override
    public String deserialize(byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    //自定义Redis序列化的方式
    @Override
    public byte[] serialize(Object object) {
        if (object == null) {//空的话直接输出
            return new byte[0];
        }
        if(object instanceof String){//我们的目标就是要得到String类型，所以将其转换成String类型再返回
            return object.toString().getBytes(charset);
        }else {//其他类型的话，使用alibaba的fastjson封装好的函数来帮我们强转成String类型再返回
            String string = JSON.toJSONString(object);
            return string.getBytes(charset);
        }
    }
}
