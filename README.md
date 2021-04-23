## 设计思想

### 资源分配的设计思想

权限管理系统可以有基于角色分配资源，基于权限分配资源。

基于角色分配资源，相当于角色和权限相关联，用户和角色相关联。

但是这会带来一定的问题：

当用户添加角色，其拥有的权限已经和某一角色的权限相同，那么对于权限的判断语句就需要大量的修改，例如：

```java
//修改前
if ( user.hasRole("role1") ) {
    //权限1
}

//修改后
if ( user.hasRole("role1") || user.hasRole("role2") ) {
    //权限1
}
```

这毫无疑问是不利于系统的拓展。

如果说是基于权限来分配资源的话，可拓展性就会好很多。

例如，在分配权限是，可以这么判断：

```java
if ( user.hasPermission("permission1") ) {
    //权限1
}
```

无论用户怎么修改权限，权限的判断语句逻辑都不会改变

但这并不意味着角色是可以舍弃的。角色依旧是用户的可视化UI界面分配权限的中转。只需要我们在资源分配时，先通过用户获取所拥有的角色，在通过角色获取所拥有的权限，最后简单的通过hasPermission()来判断即可。

## Redis处理

### 使用连接池

目的：客户端连接Redis时，使用的是TCP连接。这样每次都需要进行三次握手和四次挥手，连接的开销会很大。引入Redis连接池，那么就可以使得每次连接时从连接池中获取连接对象，用完后归还给连接池，这些操作是在本地进行的，开销不会很大，只有在初始化连接池的时候，才建立了TCP连接。

### Redis序列化

#### 目的

序列化将对象信息转成二进制数组。

持久性：存在数据库中， 利于检索使用

深度复制：赋值二进制

Redis提供了两种序列化的方式：RedisTemplate和StringRedisTemplate。

二者区别：

1.StringRedisTemplate只能对String类型的key-value进行操作，而RedisTemplate可对Object类型的key-value进行操作。

2.二者序列化的方式不同：

StringRedisTemplate使用的是StringRedisSerializer，序列化后得到的是二进制数组，反序列化后得到String类型

RedisTemplate使用的是JdkSerializationRedisSerializer来进行序列化，序列化后得到的是二进制数组，反序列化后得到的是Object类型

而StringRedisSerializer使用的是String.getBytes()来获取，JdkRedisSerializer序列化对象，存入Redis中，是不可视化的，因为他还存储了对象的其他属性。

如果不指定Redis序列化的方式，那么有可能会出现存储对象时，出现类型无法转换的问题。

#### 如何序列化

编写Redis配置类，加载到容器中。

然后实例化RedisTemplate<String,Object>对象，设置value的序列化方式都按照我们的配置类来序列化。

序列化配置类：

实现RedisSerializer<Object>接口，重写serialize和deserialize方法。即序列化与反序列化的方法。

序列化：判断对象是否是String类型的实例，如果是，那么就直接将对象toString().getBytes返回序列化结果；如果不是，使用fastJson的toJSONString()方法来使对象转成Sting类型后，在获取二进制数组返回。

反序列化：将二进制数组转成String类型返回

#### 对Redis基本操作的封装

序列化Redis后，那么redis的基本需要经过我们自定义序列化后的方式来进行操作。我复制了源码中redis的基本操作，然后每个方法不是用源码的方式返回，而是通过我们自定义的redisTemplate来返回响应操作，这样就能使得redis进行操作时，我们的序列化方式起到作用

## Swagger2

引入目的：显示接口文档。方便与前端人员的对接

使用方法：

1.在控制层使用@Api注解，controller的方法中使用@ApiOperation注解

2.在VO的属性上使用@ApiModelPropertiy注解

## JWT

[科普](https://learnku.com/articles/17883)

### 引入

用户与服务器的通信：

**基于cookie-session的通信**：

1.用户向服务器发送账号与密码

2.服务器通过对比后，在当前session中存入登录的信息，比如用户角色信息，登录时间等等

3.服务器返回给用户一个session_id,写入到用户的cookie

4.用户每次再访问服务器，都通过cookie来获取session_id，传给服务器

5.服务器通过session_id来获取之前的登录信息，从而达到用户身份认证的目的

缺点：系统的拓展性不强。在服务器集群中，需要将session的信息进行共享，使得每台服务器都能读取到该session。

**基于JWT的用户认证：**

用户首次登录，和服务器认证成功后，返回一个JSON对象给用户

```json
{
  "姓名": "张三",
  "角色": "管理员",
  "到期时间": "2018年7月1日0点0分"
}
```

此后用户与服务器进行通信的时候，都需要发送JSON对象给服务器，服务器通过JSON来进行身份的认证。同时服务器生成这个JSON对象时，会加上签名，防止用户篡改JSON对象。

### 引用更好的解释

#### 传统的 session 流程

1. 浏览器发起请求登陆
2. 服务端验证身份，生成身份验证信息，存储在服务端，并且告诉浏览器写入 Cookie
3. 浏览器发起请求获取用户资料，此时 Cookie 内容也跟随这发送到服务器
4. 服务器发现 Cookie 中有身份信息，验明正身
5. 服务器返回该用户的用户资料

#### JWT 流程

1. 浏览器发起请求登陆
2. 服务端验证身份，根据算法，将用户标识符打包生成 token, 并且返回给浏览器
3. 浏览器发起请求获取用户资料，把刚刚拿到的 token 一起发送给服务器
4. 服务器发现数据中有 token，验明正身
5. 服务器返回该用户的用户资料

**区别**

1. session 存储在服务端占用服务器资源，而 JWT 存储在客户端

2. session 存储在 Cookie 中，存在伪造跨站请求伪造攻击的风险

3. session 只存在一台服务器上，那么下次请求就必须请求这台服务器，不利于分布式应用

4. 存储在客户端的 JWT 比存储在服务端的 session 更具有扩展性

   

**JWT的缺点**

服务器不保存session状态，那么使用期间不可能取消令牌和更改令牌权限，只要Token签发了，在有效期内会一直生效

### JWT的token的结构

header.payload.signature

即：头部.负载.签名

![image](https://tvax4.sinaimg.cn/large/0085EwgIgy1goak3m58wgj30sf07mmzl.jpg)

header: JSON对象

![image](https://tva1.sinaimg.cn/large/0085EwgIgy1goak5kh8zrj30wr0admye.jpg)

Payload: JSON对象

![image](https://tvax4.sinaimg.cn/large/0085EwgIgy1goak6ekblrj30wu0lwwgz.jpg)

signature： 指定密钥，然后通过算法来产生token

![image](https://tvax4.sinaimg.cn/large/0085EwgIgy1goak7nohsjj30y30aydh9.jpg)

### JWT工具类

#### 签发token

1.设置签名算法为HS256

2.生成签名密钥，为一个base64加密后的字符串

3.调用Jwts.builder()来获取JwtBuilder对象

4.配置头部信息，签发人，subject(我配置为用户的ID)，配置有效时间

5.配置签名加密算法，

6.调用builder.compact();即可完成token的签发

#### 生成accessToken

调用上文的生成签发token的方法，传入的有效时间为accessToken的有效时间

#### 生成refreshToken

调用上文的生成签发token的方法，传入的有效时间为refreshToken的有效时间

#### token解析

调用此方法，获取到负载信息

```java
Jwts.parser()//创建解析对象
.setSigningKey(secretKey)//设置安全密钥
.parseClaimsJws(token)//解析token
.getBody()//获取负载信息
```

#### 验证token是否过期

1.调用上面的token解析方法，即可获得负载的信息

2.从负载中获取到有效时间和当前时间对比即可

#### 判断token是否有效

1.调用上文的token解析获取负载

2.检查负载中token不为空而且未过期

#### 刷新token

主动刷新token，改变token中负载的信息

1.通过传入的refreshToken判断是否为空。为空表示用户信息未改变，直接返回原来的refreshToken

2.调用签发token的方法重新生成新的refreshToken

#### 获取token的剩余时间

1.调用解析token方法，获取负载信息

2.通过负载信息获取token的过期时间，减去当前时间

### Token业务逻辑

#### 注销token问题

当用户进行以下操作：

1.退出登录

2.修改密码

3.自己的角色或者权限发生变化

4.被其他人设为被禁用或者锁定

5.被删除

6.被管理员注销账号

由于session是存在数据库中的，那么从数据库中删掉session就可以了。但是token是签发之后就有效，要么删除，要么等自己失效。

解决方法：

1.将token存在缓存中，如redis。如果发生了上面的逻辑，那么就将token从redis中删除。但是存在一个问题，每次使用token时发送请求，都要在redis中查找，这很明显违背了token的初衷：无状态

2.维护一个新的黑名单token。如果想让token失效就直接把token加入到黑名单。

#### token续签问题

token存在过期时间，如果过期了怎么签发新的token，来使得避免用户重新登录呢？

1.方法一，类似于session的方法进行更新。设置token的过期时间，每次服务器进行校验的时候，如果发现了快要过期的token，就要发送新的token，客户端发送请求的时候，就要将新token和本地token进行对比。如果不一样的话就更新本地token成新的token。

优点：思路简单，容易实现

缺点：只有token快过期的时候才会更新token，而且开销会比较大。

2.方法二：登录时发送两个token：accessToken和refreshToken。accessToken的过期时间较为短暂，比如三十分钟，而refreshToken的过期时间会比较长，比如十天。登录之后，客户端发送请求时，如果accessToken过期，将refreshToken发送给服务器，服务器通过refreshToken来生成新的accessToken。如果refreshToken也过期，那么就需要重新登录。

#### 用户登录

第一次登陆时，用hashMap存储用户的用户信息和权限信息。然后将hashmap注入到token的负载中生成accessToken和refreshToken。接着将token存在客户端。

#### 退出登录

用户退出登录时，将accessToken和refreshToken同时失效，即放到黑名单中。

#### 修改密码(token的注销)

修改密码时，之前的token应该是失效的了，因此需要将accessToken和refreshToken加入黑名单中。在登陆的逻辑中，需要对token是否被加入黑名单进行判断。

#### shiro中的token认证逻辑

1.从登录请求中获取到用户ID

2.判断用户ID对应的token是否被锁定、被删除、被加入黑名单

3.判断accessToken是否有效

4.判断token是否被刷新过，刷新过的话表明当前token应该是无效的

## Shiro

### 身份认证流程

1.subject.login(token)，传入前端的token

2.进入AuthenticatingSecurityManager的anthenticate执行用户认证

3.进入到ModularRealmAuthenticator的doAuthenticate方法进行认证

在此方法中，获取设置的realm的个数来进入对应的方法

单个realm调用doSingleRealmAuthcation方法

多个realm调用doMultiRealmAuthcation方法

4.进入到AuthticatingRealm类中的getAuthticationInfo中，从缓存获取用户认证信息。接着判断是否从缓存中得到信息。如果没有，那就进入我们自定义的realm中的doGetAuthentication方法获取。获取后进入到assertCredentialsMatch方法

5.在assertCredentialsMatch方法中调用我们自定义的MyHashedCredentialsMatcher方法中的doCredentialsMatch方法

6.再doCredentialMatch()方法中，通过redis判断是否有锁定用户的token，黑名单token，是否有删除用户的token，用户的token是否有效，用户是否主动退出，用户是否刷新过token

认证过程到此结束

### 授权过程

1.后端使用@RequirePemissions("sys:user:list")注解，在controller层进行授权

2.执行AnthoriziingSecurityManager里的checkPermission进行授权认证

3.进入到ModularRealmAuthorizer执行授权。

首先使用assertRealmConfigured()方法检查有没有配置realm

接着对所有的realm中都要调用其中的isPermitted进行授权认证

4.进入到AuthorizingRealm中获取到用户拥有的权限信息

5.从缓存中获取用户所拥有的权限信息。如果没有的话就进入到自定义的MyRealm中执行doGetAuthorizationInfo来获取权限信息

6.将权限信息存入到缓存中

7.在AuthorizingRealm方法中调用isPermitt()方法将用户的权限信息封装成Permissions类型，然后循环遍历来查看是否有权限，一旦出现出现灭有的权限，就抛出异常禁止访问

### 自定义Realm

1.集成AuthorizingRealm，重写doGetAuthorization方法与doGetAuthentication方法

2.doGetAuthrization方法用于权限认证，可获取到负载中存储的权限与角色信息

3.doGetAuthorication方法用于身份认证，可用于获取到用户的认证信息，认证方法：

编写集成HashCredentialsMatcher方法，重写doCredentialsMatch方法，调用redis来查看用户是否被锁定，被删除，token是否有效，然后再放行

### Shiro核心配置

1.将我们的登录认证方法，即myHashCredentialsMatcher方法注入到bean中

2.实例化我们自定义的Realm，配置认证方法为我们的myHashCredentialsMatahcer方法

3.配置SecurityManager，设置realm为我们自定义的realm实例，将securitymanager注册到bean中

4.配置拦截的url以及对应的认证等级

编写ShiroFilterFactoryBean，编写对应的url与拦截的等级

在返回ShiroFilterFactoryBean实例

### Shiro的Cache

1.实现shiro的CacheManager接口，重写其getCache方法

2.将shiro的配置信息存入到redis中即可

## 前后端数据的封装

1.定义DataResult类，包含code、msg属性，以及范型data，表示客户端的响应信息

2.创建响应接口，包含getCode()方法和getMsg()方法

3.编写响应码的枚举类

## 接管全局异常

接管系统抛出的异常，转换成我们规定好的DataResult，便于前端接收数据并输出。看一个简单的例子：

![image](https://tva3.sinaimg.cn/large/0085EwgIgy1gn5xqquh9vj30h105hq4f.jpg)

简单的设置一个除零异常

如果不接管异常的话，系统输出的异常的数据，不是json格式，无法用于前端的处理。

![image](https://tva1.sinaimg.cn/large/0085EwgIgy1gn5xua1thmj30wi0bawwf.jpg)

前端是无法处理这种格式的数据的。

### 配置异常处理类

```java
package com.daniel.exception.handler;

import com.daniel.exception.code.BaseResponseCode;
import com.daniel.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Package: com.daniel.exception.handler
 * @ClassName: RestExceptionHandler
 * @Author: daniel
 * @CreateTime: 2021/1/30 19:00
 * @Description: 接管系统异常，自定义成我们想要的结果
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public DataResult handleException(Exception e) {
        log.error("handlerException...{}",e);
        return DataResult.getResult(BaseResponseCode.SYSTEM_ERROR);
    }
}

```

这时候我们再看看处理结果：

![image](https://tvax4.sinaimg.cn/large/0085EwgIgy1gn5xvofcdrj311z0hb75c.jpg)

已经是json格式，这才达到了前后端能分离的目的。

### @ExceptionHandler()详解

```java
//运行时异常的监控，此类是自定义的。
    @ExceptionHandler(value = BusinessException.class)//捕获BusinessException此异常
    public DataResult businessException(BusinessException e){
        log.error("businessException,{},{}",e.getLocalizedMessage(),e);
        return DataResult.getResult(e.getCode(),e.getMsg());
    }

```

可见可以指定异常类

捕获异常的过程

1.ExceptionHandlerExceptionResolver实现了InitializatingBean，在实例化时会被初始化到HandlerExceptionResolver bean中

2.DispatcherServlet初始化时，会搜索所有的HandlerExceptionResolver，记录成一个List类型

3.当exception被抛出时，就会遍历handlerExceptionResovler的List集合中

4.查看是否有对应的异常处理方法：

​	首先从发生异常的控制器中寻找@ExceptionHandler方法来处理

​	找不到的话就从所有@ControllerAdvice注解类中寻找@ExceptionHandler来处理该异常

5.找到方法后处理该异常



## 用户登录

1.接收到前端传入的请求VO，该VO只有用户名和密码

2.通过VO的用户名来获取用户，并判断非空

3.获取用户的盐值，用户的明文密码，数据库的密文密码。传入方法类中来进行比对

4.新建哈希表，建立用户用户名，角色名，权限列表的映射。key为自定义的String类型的Constans，value为用户的实际值。

5.将用户的ID以及哈希表作为参数来生成accessToken(JWT)

然后生成refreshToken

6.返回响应VO，包含accessToken和refreshToken以及userID

## 自定义过滤器

1.集成AccessControlFilter父类，重写isAccessAllowed方法和onAccessDenied方法

2.由于该类是链式过滤，当isAccessAllowed不通过时会进入到onAccessDenied方法，因此直接让isAccessAllowed返回false，方便我们在onAccessDenied编写业务逻辑

3.onAccessDenied业务逻辑：

(1)将客户端发来的请求数据转成HTTP请求，（为什么要转换？）

因为Servlet只提供了基本不的信息，无法获取到用户的请求类型。

2.获取用户的token，判断头部是否携带了accessToken。未携带就抛出token不能为空的异常

3.通过accesssToken，生成UserNamePassWordToken类型的对象，将该对象作为参数，调用shiro的getSubject.login()，让shiro帮我们进行身份认证与权限认证

## MyBatis

### SQL注入

什么是sql注入？

SQL注入即是指web应用程序对用户输入数据的合法性没有判断或过滤不严，攻击者可以在web应用程序中事先定义好的查询语句的结尾上添加额外的[SQL语句](https://baike.baidu.com/item/SQL语句/5714895)，在管理员不知情的情况下实现非法操作，以此来实现欺骗数据库服务器执行非授权的任意查询，从而进一步得到相应的数据信息。

### 防止SQL注入

Mybatis并不能编译SQL语句，而是前后加“”,丢给MySQL进行执行.

1.like参数注入容易导致SQL注入

解决方法：使用这样的语句：

```xml
like concat('%',#{value},'%')
```

2.in的参数可能会导致SQL注入

解决方法：

```xml
id in
<foreach collection ="list" item ="item" open="(" close=")" sepator=",">
#{item}
</foreach>
```

## 菜单权限模块

### 获取菜单权限树

1.编写后端响应类

2.编写实现接口

3.编写实现类：

getChild：递归的获取当前树的所有子类。遍历传入的权限集合，对于每一个权限，如果当前权限的父级ID和传入的ID相等，说明是该ID的子类，就将其加入到响应的结果集和中

getTree():递归的获取到所有的菜单，只寻找父级ID为0，即顶层模块的菜单，这样就能获得所有的菜单权限树，在每一棵树里，调用getChild，获取到其子类

### 新增菜单权限

1.创建后端响应的接口

2.编写实现接口

3.编写实现类

将获取的vo复制得到SysPermission类型，然后检验权限的类型。因为需要限定：

​	-操作类型为目录时，父级类型需要是目录

​	-操作类型为菜单时，父级类型需要是目录

​	-操作类型为按钮时，父级类型需要是菜单

通过简单的switch语句判断即可

检验成功后，调用mapper将数据插入

4.在控制层编写PostMapping进行相应的url业务跳转逻辑

使用@RequestBody注解

### 更新菜单权限

1.实例化菜单权限对象，将前端传入的数据复制到对象中

2.对传入的数据进行检查：

​	-操作类型为目录时，父级类型需要是目录

​	-操作类型为菜单时，父级类型需要是目录

​	-操作类型为按钮时，父级类型需要是菜单

3.如果菜单发生了变化或者是权限状态发生了变化，就通过权限ID来获取所有的权限对象，判断权限是否有子集，如果有的话不允许更新

4.通过权限ID来获取所有对应的角色ID，通过角色ID来获取所有对应的用户，将用户的授权信息删除，标记用户已经刷新token

### 删除菜单权限

1.通过权限ID获取到对应的权限

2.通过权限ID获取到所有的子权限，如果子集非空，那么抛出异常，不允许删除

3.将该权限的删除字段标记为已删除

4.通过权限ID获取所有的角色ID集合

5.通过权限ID删除掉角色-权限表中对应的字段

6.通过角色ID集合获取到对应的用户ID集合

7.遍历用户ID集合，对于每个用户，都标记其刷新过token，并将其授权信息删除

## 角色模块

### 新增角色

1.实例化角色对象

2.将前端传入的vo复制给角色实例

3.调用mapper，新建角色

4.判断传入的角色中，是否也设置了权限

5.如果设置了权限，调用角色-权限服务，对角色进行权限的配置

### 获取角色拥有权限

1.通过mapper，通过角色ID来获取到角色

2.通过角色-权限服务，获取到角色所拥有的权限集合



### 更新角色信息

```java
@Transactional(rollbackFor = Exception.class)
```

1.加上事务，出现异常时回滚到出现异常前

2.通过角色ID获取到需要更新的角色

3.复制更新信息给角色实例

4.将更新后的角色信息插入到sys_role表中

5.实例化新的角色-权限操作对象，配置角色ID与权限ID集合

6.调用角色-权限服务，更新角色拥有的权限

7.调用用户-角色服务，通过角色ID来获取对应所有的用户集合

8.对于每一个用户，调用redisService，

注入key为JWT-REFRESH-KEY+用户ID，value为用户ID

并且删除掉该用户的授权信息缓存

```java
 redisService.delete(Constant.IDENTIFY_CACHE_KEY+userID);
```

### 删除角色

1.实例化一个角色对象，设置它的角色ID为删除的角色ID，设置deleted为0，即标记删除

2.调用角色mapper，更新对应的字段。(只标记为不可用，不删除)

3.调用用户-角色mapper，通过角色ID来获取到所有的用户ID集合

4.调用用户-角色mapper，通过角色ID来删除掉对应的字段

5.调用角色-权限mapper，通过角色ID来删除掉对应的字段

6.然后对于每一个用户，都需要对其token标记为已经刷新过

同时清除其对应的授权数据缓存

## 用户模块

### 登录

1.通过用户ID来获取用户信息

2.判断用户的状态，被锁定的话就抛出异常

3.获取用户的盐值，将前端提交的明文密码与数据库中的密码进行对比

4.创建accessToken的负载，类型为hashMap，配置用户名，角色名，权限信息的映射

5.生成accessToken，注入上述的负载

6.创建refreshToken的负载，类型为hashMap，配置用户名的映射

6.生成refreshToken，注入上述的负载

7.生成登录请求实例，注入accessToken，refreshToken等信息在返回

### 新增用户

1.新建用户实例

2.将前端传入的用户信息复制给用户实例

3.配置基本的用户信息：加盐、明文密码加密，设置id，创建时间

4.将配置后的信息注入到数据库中

##  用户-角色模块

### 获取用户所拥有的角色

1.通过用户ID获取到角色ID集合

2.注入到响应类中，即可实现在配置用户角色时，能获取到已经拥有的角色

### 赋予用户角色

1.删除掉用户原来拥有的角色

2.通过传入的VO获取需要添加的角色ID的集合，遍历，然后添加到UerRole类型的集合中

3.通过Mappler批量插入用户的角色集合

4.redis删除掉之前的认证信息，并设置新的refreshToken，使得用户的角色信息能得到改变

### 用户刷新token

1.判断用户token是否有效，调用redis判断是否被加入黑名单

2.通过token来获取到用户ID与用户名

3.新建负载信息，通过用户ID和用户名来获取当前的角色，当前的权限，从而保证负载信息和数据库的一致性

4.创建新的accessToken，将刚才的负载传入生成新的token

### 更新用户信息

1.通过用户ID来获取对应的用户实例

2.将前端传入的用户信息赋值给对象实例

3.判断设置的信息中，用户是否禁用，

如果禁用的话，调用redis插入对应的key-value。key：ACCOUNT_LOCK_KEY+用户ID, value：用户ID

如果不是禁用的话，调用redis删除对应的key。key：ACCOUNT_LOCK_KEY+用户ID

### 删除用户

1.用户删除是比较危险的操作，因此我只是将用户ID标记为已删除，不删掉用户的信息。

2.在数据库中将用户标记用户为删除状态

3.在redis中插入新的key-value：

key为DELETED_USER_KEY+用户ID，value为用户ID。

同时将用户的授权信息在redis中删除：

redisService.delete(IDENTIfY_CACHE_KEy+用户ID)

### 用户退出登录

1.调用Shiro中的SecurityUtils.getSubject()获取当前的subject

2.调用subject.isAuthenticated()判断用户是否通过认证。是的话就调用subject.logout()

3.否则的话，通过accessToken获取用户ID，在redis进行操作：

将用户的accessToken和refreshToken加入黑名单

key为常量+accessToken/refreshToken，value为用户ID

set(JWT_ACCESS_TOKEN_BLACKLIST_accessToken,userID,......)

set(JWT_REFRESH_TOKEN_BLACKLIST_refreshToken,userID,......)

### 获取用户详细信息

调用mapper，通过用户ID获取所有的列即可

### 更新用户详细信息

1.实例化用户对象

2.将更新的信息复制到用户实例

3.调用mapper更新用户信息(update语句)

### 更新用户密码

1.调用mappper，通过用户ID来获取到用户实例

2.传入盐值，前端的明文密码，数据库中的密文密码比对，看是否正确

3.生成新的密文密码，调用mapper更新用户的密码

## 部门模块

### 获取部门

### 新增部门

### 更新部门信息

### 删除部门

## 日志模块

### 自定义AOP注解

```java
@Documented//表明这个注解应该被javadoc工具记录
@Target(ElementType.METHOD)//作用：用于描述注解的使用范围（即：被描述的注解可以用在什么地方）
@Retention(RetentionPolicy.RUNTIME)//这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用.
public @interface MyLog {

    //模块
    String title() default "";

    //功能
    String action() default "";
}

```

### AOP切面类

1.将类使用@Aspect注解，表示创建一个切面类，并用@Component注解将类注入到容器中

```java
@Aspect//创建切面类
@Component
@Slf4j
public class SysLogAspect {}
```

2.设置切点

```java
@Pointcut("@annotation(com.daniel.aop.annotation.MyLog)")//指定的切入点
    public void logPointCut() {
    }
```

将该注解的切入点设为logPointCut()方法

3.环绕增强

```java
@Around("logPointCut()")
    public Object around( ProceedingJoinPoint point ) throws Throwable{

        //开始时间
        long beginTime= System.currentTimeMillis();

        //执行方法，它的上面为执行方法前，同理下面为执行方法后
        Object result = point.proceed();

        //执行时长
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        try {
            //保存日志的业务逻辑方法
            saveSysLog(point, time);
        } catch (Exception e) {
            log.error("e={}",e);
        }

        return result;
    }
```

@Around注解表示，在执行logPointCut()方法前后所进行的操作

4.实现日志操作的业务逻辑

```java
private void saveSysLog (ProceedingJoinPoint point, long time ) {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        MyLog myLog = method.getAnnotation(MyLog.class);
        if(myLog != null){
            //注解上的描述
            sysLog.setOperation(myLog.title()+"-"+myLog.action());
        }

        //请求的方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        //打印该方法耗时时间
        log.info("请求{}.{}耗时{}毫秒",className,methodName,time);
        try {
            //请求的参数
            Object[] args = point.getArgs();
            String params=null;
            if(args.length!=0){
                params= JSON.toJSONString(args);
            }

            sysLog.setParams(params);
        } catch (Exception e) {

        }
        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));
        log.info("Ip{}，接口地址{}，请求方式{}，入参：{}",sysLog.getIp(),request.getRequestURL(),request.getMethod(),sysLog.getParams());
        //用户名
        String  token = request.getHeader(Constant.ACCESS_TOKEN);
        String userId= JWToken.getUserId(token);
        String username= JWToken.getUserName(token);
        sysLog.setUsername(username);
        sysLog.setUserId(userId);
        sysLog.setTime((int) time);
        sysLog.setId(UUID.randomUUID().toString());
        sysLog.setCreateTime(new Date());
        log.info(sysLog.toString());
        sysLogMapper.insertSelective(sysLog);
    }
```

