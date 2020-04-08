package cn.zsk.springsecurityoauth2server.service;

import cn.zsk.springsecurityoauth2server.entity.SysUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author:zsk
 * @CreateTime:2020-04-03 13:34
 */
public interface SysUserService extends IService<SysUserEntity> {

    /**
     * 通过不同条件查询用户信息,同时级联查询用户的权限等相关信息
     * @param map
     * @return: cn.zsk.springsecurityoauth2serverrbac.entity.SysUserEntity
     * @throws
     * @time: 2020-04-03 13:56
     * @author: zsk
     */
    SysUserEntity findUserEntityByMap(Map<String,Object> map);
}
