package cn.zsk.springsecurityoauth2serverrbac.service;

import cn.zsk.springsecurityoauth2serverrbac.entity.SysPermissionEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author:zsk
 * @CreateTime:2020-04-03 13:35
 */
public interface SysPermissionService extends IService<SysPermissionEntity> {

    /**
     * 通过用户id查询用户授权信息
     * @param userId
     * @return: java.util.List<cn.zsk.springsecurityoauth2serverrbac.entity.SysPermissionEntity>
     * @throws
     * @time: 2020-04-03 14:07
     * @author: zsk
     */
    List<SysPermissionEntity> findSysPermissionListByUserId(Long userId) throws Exception;
}
