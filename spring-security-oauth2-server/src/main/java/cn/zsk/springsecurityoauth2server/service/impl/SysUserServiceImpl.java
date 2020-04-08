package cn.zsk.springsecurityoauth2server.service.impl;

import cn.zsk.springsecurityoauth2server.entity.SysUserEntity;
import cn.zsk.springsecurityoauth2server.mapper.SysUserMapper;
import cn.zsk.springsecurityoauth2server.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author:zsk
 * @CreateTime:2020-04-03 13:46
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUserEntity findUserEntityByMap(Map<String, Object> map) {
        SysUserEntity sysUserEntity = sysUserMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("user_name",map.get("userName")));
        return sysUserEntity;
    }
}
