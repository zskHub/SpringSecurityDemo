package cn.zsk.springsecurityoauth2servermemory.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * @author:zsk
 * @CreateTime:2019-12-05 17:36
 */
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
