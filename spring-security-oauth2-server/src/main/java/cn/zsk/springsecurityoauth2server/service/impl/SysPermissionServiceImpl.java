package cn.zsk.springsecurityoauth2server.service.impl;

import cn.zsk.springsecurityoauth2server.entity.SysPermissionEntity;
import cn.zsk.springsecurityoauth2server.mapper.SysPermissionMapper;
import cn.zsk.springsecurityoauth2server.service.SysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:zsk
 * @CreateTime:2020-04-03 13:37
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermissionEntity> implements SysPermissionService {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermissionEntity> findSysPermissionListByUserId(Long userId) throws Exception {
        if(null == userId){
            throw new Exception("通过用户id查询用户权限异常，缺少userId");
        }
        List<SysPermissionEntity> sysPermissionEntityList = sysPermissionMapper.findSysPermissionListByUserId(userId);
        return sysPermissionEntityList;
    }
}
