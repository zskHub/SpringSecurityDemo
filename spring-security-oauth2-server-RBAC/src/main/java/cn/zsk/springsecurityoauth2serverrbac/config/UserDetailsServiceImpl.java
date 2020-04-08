package cn.zsk.springsecurityoauth2serverrbac.config;

import cn.zsk.springsecurityoauth2serverrbac.entity.SysPermissionEntity;
import cn.zsk.springsecurityoauth2serverrbac.entity.SysUserEntity;
import cn.zsk.springsecurityoauth2serverrbac.service.SysPermissionService;
import cn.zsk.springsecurityoauth2serverrbac.service.SysUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author:zsk
 * @CreateTime:2020-04-03 14:01
 */
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
