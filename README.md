# spring security #
- 本项目仅仅是一个很简单，很简单的demo。还有很多技术点没有讲解。
- spring security 相关介绍

- 技术介绍：
- 四种模式：
*简介说明源地址：`https://www.funtl.com/zh/spring-security-oauth2/%E5%AE%A2%E6%88%B7%E7%AB%AF%E6%8E%88%E6%9D%83%E6%A8%A1%E5%BC%8F.html#%E6%A6%82%E8%BF%B0`*
1. 简化模式：简化模式适用于纯静态页面应用。所谓纯静态页面应用，也就是应用没有在服务器上执行代码的权限（通常是把代码托管在别人的服务器上），
只有前端 JS 代码的控制权。这种场景下，应用是没有持久化存储的能力的。因此，按照 oAuth2.0 的规定，这种应用是拿不到 Refresh Token 的。
该模式下，access_token容易泄露且不可刷新。

<img src="2-image/sso2-01.png" width = "800" height = "200" align=center />

2. 授权码模式适用于有自己的服务器的应用，它是一个一次性的临时凭证，用来换取 access_token 和 refresh_token。
认证服务器提供了一个类似这样的接口：`https://www.baidu.com/exchange?code=&client_id=&client_secret=`
需要传入 code、client_id 以及 client_secret。验证通过后，返回 access_token 和 refresh_token。一旦换取成功，code 立即作废，不能再使用第二次。

<img src="2-image/sso2-02.png" width = "800" height = "200" align=center />

3. 密码模式：密码模式中，用户向客户端提供自己的用户名和密码。客户端使用这些信息，向 "服务商提供商" 索要授权。
在这种模式中，用户必须把自己的密码给客户端，但是客户端不得储存密码。这通常用在用户对客户端高度信任的情况下，比如客户端是操作系统的一部分。
一个典型的例子是同一个企业内部的不同产品要使用本企业的 oAuth2.0 体系。在有些情况下，产品希望能够定制化授权页面。
由于是同个企业，不需要向用户展示“xxx将获取以下权限”等字样并询问用户的授权意向，而只需进行用户的身份认证即可。这个时候，由具体的产品团队开发定制化的授权界面，接收用户输入账号密码，并直接传递给鉴权服务器进行授权即可。

<img src="2-image/sso2-03.png" width = "800" height = "200" align=center />

4. 客户端模式：如果信任关系再进一步，或者调用者是一个后端的模块，没有用户界面的时候，可以使用客户端模式。鉴权服务器直接对客户端进行身份验证，验证通过后，返回 token。

<img src="2-image/sso2-04.png" width = "800" height = "200" align=center />

- spring security 配置介绍

- 说明方式，（以 2.1 基于内存存储令牌 为例）：
     2.1 基于内存存储令牌（主要的技术）  
        模块说明：XXXXXX。（简单介绍使用哪些模块）  
    2.1.1 XXX模块（下面开始对模块的详细介绍）  
      a.说明  
      （对该模块的说明）  
      b.注意事项  
        （过程中需要注意的地方。）  
      c.实现方式（具体实现方式）
        1. XXXX
        2. XXXX
        。。。  
      d.测试启动（如何测试）
       1. XXXX
       2. XXXX
       。。。  
      e.其他（其他额外需要的地方）


## 1 目录结构 ##

## 2 目录说明 ##

### 2.1 基于内存存储令牌 ###
* 使用的模块：spring-security-oauth2-server-memory
#### 2.1.1 spring-security-oauth2-server-memory ####
##### a.说明 #####
    演示基础的spring security使用。未使用数据库
##### b.注意事项 #####
    在演示通过code获取access_token时，要注意修改链接中的code为当前获取的code，因为写笔记可能需要多次刷新导致截图上code不一致。下面的模块也是同样道理。
##### c.实现方法 #####
1. pom文件
    ```
       <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.security.oauth</groupId>
            <artifactId>spring-security-oauth2</artifactId>
        </dependency>
    ```
2. yml文件
```
server:
  # undertow
  undertow:
    accesslog:
      dir: ./log/ # Undertow access log directory.
      enabled: false # Enable access log.
      pattern: '%t [%I] %a %r %s (%b Byte) (%T ms)' # Format pattern for access logs.
      prefix: access_log. # Log file name prefix.
      rotate: true # Enable access log rotation.
      suffix: log # Log file name suffix.
    buffer-size: # Size of each buffer in bytes.
    buffers-per-region: # Number of buffer per region.
    direct-buffers: # Allocate buffers outside the Java heap.
    io-threads: # Number of I/O threads to create for the worker.
    max-http-post-size: 0 # Maximum size in bytes of the HTTP post content.
    worker-threads: # Number of worker threads.
  port: 9101
  application:
    name: oauth2-server-memory
```
3. 配置文件-新增AuthorizationServerConfiguration文件
    ```
    @Configuration
    @EnableAuthorizationServer
    public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    //客户端标识
                    .withClient("client")
                    //客户端安全码
                    .secret("secret")
                    //客户端授权方式
                    .authorizedGrantTypes("authorization_code")
                    //客户端授权范围
                    .scopes("app")
                    //资源服务器地址
                    .redirectUris("https://www.baidu.com");
        }
    }
    ```
4. 配置文件-新增WebSecurityConfigurerAdapter文件
    ```
    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
    public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            //模拟将用户密码写在内存中，即这里直接写死了。一般情况会放到数据库中
            auth.inMemoryAuthentication()
                    .withUser("admin").password("123456").roles("ADMIN")
                    .and()
                    .withUser("user").password("123456").roles("USER");
        }
    }
    ```
##### d.测试 #####

    默认的一些url
    /oauth/authorize        授权端点
    /oauth/token            令牌端点
    /oauth/confirm_access   用户确认授权提交端点
    /oauth/error            授权服务错误信息端点
    /oauth/check_token      用于资源服务访问的令牌解析端点
    /oauth/token_key        提供公有密匙的端点，如果你使用JWT令牌的话
1. 访问链接`localhost:9101/oauth/authorize?client_id=client&client_secret=secret&response_type=code`,尝试获取授权码，由于加入spring security的原因，就会跳转到登录页面，如图：
<img src="2-image/sso2-server-memory01.png" width = "800" height = "200" align=center />

2. 当在第一步的测试登录页面登录时，会报错，如图：这是因为从2.0以后的版本，spring security默认的有个自己的密码加密方式，不允许这里的明文密码。

```
 auth.inMemoryAuthentication()
                    .withUser("admin").password("123456").roles("ADMIN")
                    .and()
                    .withUser("user").password("123456").roles("USER");
```
    不加密时报错如图所示：

<img src="2-image/sso2-server-memory02.png" width = "800" height = "200" align=center />

3. 需要将密码修改为加密方式。修改WebSecurityConfiguration类。

```
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public  BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //模拟将用户密码写在内存中，即这里直接写死了。一般情况会放到数据库中
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("123456")).roles("ADMIN")
                .and()
                .withUser("user").password(passwordEncoder().encode("123456")).roles("USER");
    }
}

```
同时AuthorizationServerConfigurerAdapter里面的secret也需要加密

```
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //客户端标识
                .withClient("client")
                //客户端安全码,需要加密，否则登录会报错
                .secret(passwordEncoder.encode("secret"))
                //客户端授权方式
                .authorizedGrantTypes("authorization_code")
                //客户端授权范围
                .scopes("app")
                //资源服务器地址
                .redirectUris("http://www.baidu.com");
    }
}
```
4. 然后重新输入`localhost:9101/oauth/authorize?client_id=client&client_secret=secret&response_type=code`,输入账号密码后，打开页面，如图sso2-server-memory03:

<img src="2-image/sso2-server-memory03.png" width = "800" height = "200" align=center />

5. 然后获取到授权码code，如图：sso2-server-memory04:

<img src="2-image/sso2-server-memory04.png" width = "800" height = "200" align=center />

6. 通过授权码，向授权服务器申请token
   6.1 使用curl命令：`curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d 'grant_type=authorization_code&code=1JuO6V' "http://client:secret@localhost:9101/oauth/token"`
   6.2 使用postman,设置方式：
   
   <img src="2-image/sso2-server-memory05.png" width = "800" height = "200" align=center />
   
   访问后如图所示结果：
   
   <img src="2-image/sso2-server-memory06.png" width = "800" height = "200" align=center />

### 2.2 基于jdbc存储令牌 ###
* 使用的模块：spring-security-oauth2-server
#### 2.2.1 spring-security-oauth2-server-jdbc ####
##### a.说明 #####
    演示基础的spring security使用。token使用数据库存储，用户依旧使用内存方式
##### b.注意事项 #####
这里并没有使用mybatis-plus里面的增删改查，
##### c.实现方法 #####
1. 建立数据表，官方提供了建表脚本初始化 oAuth2 相关表，地址如下：
`https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql`
由于我们使用的是 MySQL 数据库，默认建表语句中主键为 VARCHAR(256)，这超过了最大的主键长度，请手动修改为 128，并用 BLOB 替换语句中的 LONGVARBINARY 类型，修改后的建表脚本如下：
```
CREATE TABLE `clientdetails` (
  `appId` varchar(128) NOT NULL,
  `resourceIds` varchar(256) DEFAULT NULL,
  `appSecret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `grantTypes` varchar(256) DEFAULT NULL,
  `redirectUrl` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additionalInformation` varchar(4096) DEFAULT NULL,
  `autoApproveScopes` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`appId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `clientdetails` (
  `appId` varchar(128) NOT NULL,
  `resourceIds` varchar(256) DEFAULT NULL,
  `appSecret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `grantTypes` varchar(256) DEFAULT NULL,
  `redirectUrl` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additionalInformation` varchar(4096) DEFAULT NULL,
  `autoApproveScopes` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`appId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_approvals` (
  `userId` varchar(256) DEFAULT NULL,
  `clientId` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `expiresAt` timestamp NULL DEFAULT NULL,
  `lastModifiedAt` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(128) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_client_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_code` (
  `code` varchar(256) DEFAULT NULL,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
2. pom文件
```
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.security.oauth</groupId>
            <artifactId>spring-security-oauth2</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatisplus.version}</version>
        </dependency>
    </dependencies>

```

3. AuthorizationServerConfiguration文件改动
```
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Bean
    @ConfigurationProperties("spring.datasource.druid" )
    public DataSource dataSource() {
        return DruidDataSourceBuilder
                .create()
                .build();
    }

    @Bean
    public TokenStore tokenStore(){
        // 基于 JDBC 实现，令牌保存到数据
        return new JdbcTokenStore(dataSource());
    }

    @Bean
    public ClientDetailsService jdbcClientDetails(){
        // 基于 JDBC 实现，需要事先在数据库配置客户端信息
        return new JdbcClientDetailsService(dataSource());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 读取客户端配置
       clients.withClientDetails(jdbcClientDetails());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints){
        // 设置令牌
        endpoints.tokenStore(tokenStore());
    }
}

```
##### d.测试 #####
1. 访问链接`localhost:9102/oauth/authorize?client_id=client&response_type=code`,尝试获取授权码，由于加入spring security的原因，就会跳转到登录页面，如图：

<img src="2-image/sso2-server-jdbc01.png" width = "800" height = "200" align=center />

2. 输入账号密码登录后如图。此时这里的信息就是通过读取数据库中表`oauth_client_details`中的客户端信息配置。

<img src="2-image/sso2-server-jdbc02.png" width = "600" height = "200" align=center />

3. 查看下此时数据库中的配置信息

<img src="2-image/sso2-server-jdbc04.png" width = "600" height = "200" align=center />

4.点击授权后页面

<img src="2-image/sso2-server-jdbc03.png" width = "600" height = "200" align=center />

5. 获取access_token,结果

<img src="2-image/sso2-server-jdbc06.png" width = "600" height = "200" align=center />

6. 此时查询数据库，表`oauth_access_token` 增加了一条记录

<img src="2-image/sso2-server-jdbc05.png" width = "600" height = "200" align=center />

### 2.3 spring security oauth2基于RBAC的认证 ###
* 使用的模块：spring-security-oauth2-server-RBAC
#### 2.3.1 spring-security-oauth2-server-RBAC ####
##### a.说明 #####
    演示基础的spring security使用。token使用数据库存储，用户信息也使用数据库存储
##### b.注意事项 #####
    添加了有关mybatis plus的使用
##### c.实现方法 #####
1. 增加数据表，用户表，角色表，权限表，用户-角色表，角色-权限表。一个用户对应N个角色，一个角色对应N个权限。
```
-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`  (
  `token_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authentication` blob NULL,
  `refresh_token` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`authentication_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth_approvals
-- ----------------------------
DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals`  (
  `userId` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `clientId` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `expiresAt` timestamp(0) NULL DEFAULT NULL,
  `lastModifiedAt` timestamp(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端标识',
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端安全码',
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端授权范围',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端授权方式',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源服务器地址',
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `access_token_validity` int(11) NULL DEFAULT NULL,
  `refresh_token_validity` int(11) NULL DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('client', NULL, '$2a$10$X3sGCVNCKB3aVPzJI8Tbzudjm3cZrgZnGPkfCPE.ABTGfG5iJ8Yui', 'app', 'authorization_code', 'https://127.0.0.1', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token`  (
  `token_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`authentication_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`  (
  `code` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authentication` blob NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`  (
  `token_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication` blob NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父权限',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名称',
  `enname` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限英文名称',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权路径',
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NOT NULL,
  `last_update_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (37, 0, '系统管理', 'System', '/', NULL, '2019-04-04 23:22:54', '2019-04-04 23:22:56');
INSERT INTO `sys_permission` VALUES (38, 37, '用户管理', 'SystemUser', '/users/', NULL, '2019-04-04 23:25:31', '2019-04-04 23:25:33');
INSERT INTO `sys_permission` VALUES (39, 38, '查看用户', 'SystemUserView', '/users/view/**', NULL, '2019-04-04 15:30:30', '2019-04-04 15:30:43');
INSERT INTO `sys_permission` VALUES (40, 38, '新增用户', 'SystemUserInsert', '/users/insert/**', NULL, '2019-04-04 15:30:31', '2019-04-04 15:30:44');
INSERT INTO `sys_permission` VALUES (41, 38, '编辑用户', 'SystemUserUpdate', '/users/update/**', NULL, '2019-04-04 15:30:32', '2019-04-04 15:30:45');
INSERT INTO `sys_permission` VALUES (42, 38, '删除用户', 'SystemUserDelete', '/users/delete/**', NULL, '2019-04-04 15:30:48', '2019-04-04 15:30:45');
INSERT INTO `sys_permission` VALUES (44, 37, '内容管理', 'SystemContent', '/contents/', NULL, '2019-04-06 18:23:58', '2019-04-06 18:24:00');
INSERT INTO `sys_permission` VALUES (45, 44, '查看内容', 'SystemContentView', '/contents/view/**', NULL, '2019-04-06 23:49:39', '2019-04-06 23:49:41');
INSERT INTO `sys_permission` VALUES (46, 44, '新增内容', 'SystemContentInsert', '/contents/insert/**', NULL, '2019-04-06 23:51:00', '2019-04-06 23:51:02');
INSERT INTO `sys_permission` VALUES (47, 44, '编辑内容', 'SystemContentUpdate', '/contents/update/**', NULL, '2019-04-06 23:51:04', '2019-04-06 23:51:06');
INSERT INTO `sys_permission` VALUES (48, 44, '删除内容', 'SystemContentDelete', '/contents/delete/**', NULL, '2019-04-06 23:51:08', '2019-04-06 23:51:10');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父角色',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `enname` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色英文名称',
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NOT NULL,
  `last_update_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (37, 0, '超级管理员', 'admin', NULL, '2019-04-04 23:22:03', '2019-04-04 23:22:05');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限 ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (37, 37, 37);
INSERT INTO `sys_role_permission` VALUES (38, 37, 38);
INSERT INTO `sys_role_permission` VALUES (39, 37, 39);
INSERT INTO `sys_role_permission` VALUES (40, 37, 40);
INSERT INTO `sys_role_permission` VALUES (41, 37, 41);
INSERT INTO `sys_role_permission` VALUES (42, 37, 42);
INSERT INTO `sys_role_permission` VALUES (43, 37, 44);
INSERT INTO `sys_role_permission` VALUES (44, 37, 45);
INSERT INTO `sys_role_permission` VALUES (45, 37, 46);
INSERT INTO `sys_role_permission` VALUES (46, 37, 47);
INSERT INTO `sys_role_permission` VALUES (47, 37, 48);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册邮箱',
  `create_time` datetime(0) NOT NULL,
  `last_update_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`user_name`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (37, 'admin', '$2a$10$9ZhDOBp.sRKat4l14ygu/.LscxrMUcDAfeVOEPiYwbcRkoB09gCmi', '15888888888', 'lee.lusifer@gmail.com', '2019-04-04 23:21:27', '2019-04-04 23:21:29');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (37, 37, 37);

SET FOREIGN_KEY_CHECKS = 1;
```
2. pom文件
```

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.security.oauth</groupId>
            <artifactId>spring-security-oauth2</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatisplus.version}</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>
```
3. 增加了有关mybatis plus的相关配置。详见`src\main\java\cn\zsk\springsecurityoauth2serverrbac\config\MybatisConfig.java`
4. 有关dataSource的在mybatis plus配置文件中有配置，所以这里就不在配置
```
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore(){
        // 基于 JDBC 实现，令牌保存到数据
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public ClientDetailsService jdbcClientDetails(){
        // 基于 JDBC 实现，需要事先在数据库配置客户端信息
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 读取客户端配置
       clients.withClientDetails(jdbcClientDetails());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints){
        // 设置令牌
        endpoints.tokenStore(tokenStore());
    }
}
```
5. 增加UserDetailsServiceImpl类，并且实现UserDetailsService类，重写loadUserByUsername方法。这个方法里面会查询用户账号密码等信息。有点像是shiro中myRelam中的认证方法
```
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPermissionService sysPermissionService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Map<String,Object> map = new HashMap<>();
        map.put("userName",userName);
        //查询用户信息
        SysUserEntity sysUserEntity = sysUserService.findUserEntityByMap(map);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if(null != sysUserEntity){
            //获取用户授权
            List<SysPermissionEntity> sysPermissionEntityList = sysPermissionService.findSysPermissionListByUserId(Optional
                    .ofNullable(sysUserEntity)
                    .map(SysUserEntity::getId)
                    .orElse(0L));
            //声明用户授权
            for(SysPermissionEntity entity : sysPermissionEntityList){
                if(null != entity && null != entity.getEnname()){
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(entity.getEnname());
                    grantedAuthorities.add(grantedAuthority);
                }

            }
        }
        // 由框架完成认证工作
        return new User(sysUserEntity.getUserName(), sysUserEntity.getPassword(), grantedAuthorities);
    }
}

```
6. WebSecurityConfiguration文件
```
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public  BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 使用自定义认证与授权
        auth.userDetailsService(userDetailsService());
    }
```
##### d.测试 #####
1. 访问链接`localhost:9103/oauth/authorize?client_id=client&response_type=code`,尝试获取授权码，由于加入spring security的原因，就会跳转到登录页面，如图：

<img src="2-image/sso2-server-rbac01.png" width = "800" height = "200" align=center />

2. 输入账号密码登录(本项目数据库默认账号密码为：admin   123456)。然后选择授权，返回授权码

<img src="2-image/sso2-server-rbac03.png" width = "800" height = "200" align=center />

<img src="2-image/sso2-server-rbac02.png" width = "800" height = "200" align=center />

3. 换取access_token

<img src="2-image/sso2-server-rbac04.png" width = "800" height = "200" align=center />

4. 操作成功后数据库 oauth_access_token 表中会增加一笔记录。

### 2.4 创建资源服务器 ###
* 使用的模块：spring-security-oauth2-server spring-security-oauth2-source
#### 2.4.1 spring-security-oauth2-server ####
##### a.说明 #####
    拷贝spring-security-oauth2-server-RBAC模块，同时配合资源服务器模块做一些修改
##### b.注意事项 #####

##### c.实现方法 #####
1. 改动的地方：WebSecurityConfiguration 类,增加了将 check_token 暴露出去的方法
```
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public  BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 使用自定义认证与授权
        auth.userDetailsService(userDetailsService());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 将 check_token 暴露出去，否则资源服务器访问时报 403 错误
        web.ignoring().antMatchers("/oauth/check_token");
    }
}

```
##### d.测试方法 #####

#### 2.4.2 spring-security-oauth2-source ####
##### a.说明 #####
    
##### b.注意事项 #####

##### c.实现方法 #####
1. 增加表结构
```
-- ----------------------------
-- Table structure for source_content
-- ----------------------------
DROP TABLE IF EXISTS `source_content`;
CREATE TABLE `source_content`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) NOT NULL COMMENT '内容类目ID',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容标题',
  `sub_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子标题',
  `title_desc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题描述',
  `url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接',
  `pic` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片绝对路径',
  `pic2` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片2',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `created` datetime(0) NULL DEFAULT NULL,
  `updated` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE,
  INDEX `updated`(`updated`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of source_content
-- ----------------------------
INSERT INTO `source_content` VALUES (28, 89, '标题', '子标题', '标题说明', 'http://www.jd.com', NULL, NULL, NULL, '2019-04-07 00:56:09', '2019-04-07 00:56:11');
INSERT INTO `source_content` VALUES (29, 89, 'ad2', 'ad2', 'ad2', 'http://www.baidu.com', NULL, NULL, NULL, '2019-04-07 00:56:13', '2019-04-07 00:56:15');
INSERT INTO `source_content` VALUES (30, 89, 'ad3', 'ad3', 'ad3', 'http://www.sina.com.cn', NULL, NULL, NULL, '2019-04-07 00:56:17', '2019-04-07 00:56:19');
INSERT INTO `source_content` VALUES (31, 89, 'ad4', 'ad4', 'ad4', 'http://www.baidu.com', NULL, NULL, NULL, '2019-04-07 00:56:22', '2019-04-07 00:56:25');

-- ----------------------------
-- Table structure for source_content_category
-- ----------------------------
DROP TABLE IF EXISTS `source_content_category`;
CREATE TABLE `source_content_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类目ID',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父类目ID=0时，代表的是一级的类目',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态。可选值:1(正常),2(删除)',
  `sort_order` int(4) NULL DEFAULT NULL COMMENT '排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数',
  `is_parent` tinyint(1) NULL DEFAULT 1 COMMENT '该类目是否为父类目，1为true，0为false',
  `created` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id`, `status`) USING BTREE,
  INDEX `sort_order`(`sort_order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 98 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内容分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of source_content_category
-- ----------------------------
INSERT INTO `source_content_category` VALUES (30, 0, 'LeeShop', 1, 1, 1, '2015-04-03 16:51:38', '2015-04-03 16:51:40');
INSERT INTO `source_content_category` VALUES (86, 30, '首页', 1, 1, 1, '2015-06-07 15:36:07', '2015-06-07 15:36:07');
INSERT INTO `source_content_category` VALUES (87, 30, '列表页面', 1, 1, 1, '2015-06-07 15:36:16', '2015-06-07 15:36:16');
INSERT INTO `source_content_category` VALUES (88, 30, '详细页面', 1, 1, 1, '2015-06-07 15:36:27', '2015-06-07 15:36:27');
INSERT INTO `source_content_category` VALUES (89, 86, '大广告', 1, 1, 0, '2015-06-07 15:36:38', '2015-06-07 15:36:38');
INSERT INTO `source_content_category` VALUES (90, 86, '小广告', 1, 1, 0, '2015-06-07 15:36:45', '2015-06-07 15:36:45');
INSERT INTO `source_content_category` VALUES (91, 86, '商城快报', 1, 1, 0, '2015-06-07 15:36:55', '2015-06-07 15:36:55');
INSERT INTO `source_content_category` VALUES (92, 87, '边栏广告', 1, 1, 0, '2015-06-07 15:37:07', '2015-06-07 15:37:07');
INSERT INTO `source_content_category` VALUES (93, 87, '页头广告', 1, 1, 0, '2015-06-07 15:37:17', '2015-06-07 15:37:17');
INSERT INTO `source_content_category` VALUES (94, 87, '页脚广告', 1, 1, 0, '2015-06-07 15:37:31', '2015-06-07 15:37:31');
INSERT INTO `source_content_category` VALUES (95, 88, '边栏广告', 1, 1, 0, '2015-06-07 15:37:56', '2015-06-07 15:37:56');
INSERT INTO `source_content_category` VALUES (96, 86, '中广告', 1, 1, 1, '2015-07-25 18:58:52', '2015-07-25 18:58:52');
INSERT INTO `source_content_category` VALUES (97, 96, '中广告1', 1, 1, 0, '2015-07-25 18:59:43', '2015-07-25 18:59:43');

```
2. 添加有关mybatis plus的配置。添加controller，entity，mapper，service，impl，dto，这些基础配置。
3. application.yml配置,有关数据库的yml配置，这里就省略了，直接查看application-dev.yml文件吧。
同时到这一步，可以对资源服务器的初步测试，可以查看下面的测试模块说明。
```
server:
  # undertow
  undertow:
    accesslog:
      dir: ./log/ # Undertow access log directory.
      enabled: false # Enable access log.
      pattern: '%t [%I] %a %r %s (%b Byte) (%T ms)' # Format pattern for access logs.
      prefix: access_log. # Log file name prefix.
      rotate: true # Enable access log rotation.
      suffix: log # Log file name suffix.
    buffer-size: # Size of each buffer in bytes.
    buffers-per-region: # Number of buffer per region.
    direct-buffers: # Allocate buffers outside the Java heap.
    io-threads: # Number of I/O threads to create for the worker.
    max-http-post-size: 0 # Maximum size in bytes of the HTTP post content.
    worker-threads: # Number of worker threads.
  port: 9106
  application:
    name: oauth2-server-source
  servlet:
    context-path: /oauth2-server-source

spring:
  # 环境 dev|test|prod
  profiles:
    active: dev
```
4. 进一步修改，完成资源服务器的配置。首先新建类：`ResourceServerConfiguration.java`
```
@Configuration
@EnableResourceServer  //标识资源服务器
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Primary
    @Bean
    public RemoteTokenServices remoteTokenServices() {
        final RemoteTokenServices tokenServices = new RemoteTokenServices();
        //设置授权服务器check_token端点完整地址
        tokenServices.setCheckTokenEndpointUrl("http://localhost:9105/oauth/check_token");
        //设置客户端id与secret，注意：client_secret值不能使用passwordEncoder加密！
        tokenServices.setClientId("client");
        tokenServices.setClientSecret("secret");
        return tokenServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 以下为配置所需保护的资源路径及权限，需要与认证服务器配置的授权部分对应
                .antMatchers("/").hasAuthority("SystemContent")
                .antMatchers("/view/**").hasAuthority("SystemContentView")
                .antMatchers("/insert/**").hasAuthority("SystemContentInsert")
                .antMatchers("/update/**").hasAuthority("SystemContentUpdate")
                .antMatchers("/delete/**").hasAuthority("SystemContentDelete");
    }

}
```

##### d.测试方法 #####
1. 当执行方法执行到第3步时，就可以查验资源服务器是否正常（未对接认证服务器，单纯的验证资源是否能查询出来。）访问地址：
`localhost:9106/oauth2-server-source/`

2. 这个页面需要登录，用户名是user，密码在项目启动时有一个默认密码，如图

<img src="2-image/sso2-server-source01.png" width = "800" height = "200" align=center />

3. 查询结果,到这一步，资源服务器的初步测试结束，下面测试就需要连接上认证服务器使用token授权。

<img src="2-image/sso2-server-source02.png" width = "800" height = "200" align=center />

4. 对资源服务器完成所有的配置后。首先启动认证服务器，资源服务器。此时再次访问`localhost:9106/oauth2-server-source/`,就不会打开login页面
而是404了，登录页面没有了，因为都用认证服务器了。

5. 访问授权服务器 `localhost:9105/oauth/authorize?client_id=client&response_type=code`。然后用code获取access_token。过程和上面模块一样，这里省略说明了。

<img src="2-image/sso2-server-source03.png" width = "800" height = "200" align=center />

5.1 可以校验授权服务器是否正常，`http://localhost:9105/oauth/check_token?token=e1f375da-0d57-4a75-8ca4-e98f77e92e2f`

<img src="2-image/sso2-server-source06.png" width = "800" height = "200" align=center />

6. 访问地址`http://localhost:9106/oauth2-server-source/?access_token=4121cb82-0375-4805-9da0-b7b4155be447` 出现资源信息。

<img src="2-image/sso2-server-source04.png" width = "800" height = "200" align=center />

7. 或者通过postman访问。

<img src="2-image/sso2-server-source05.png" width = "800" height = "200" align=center />
