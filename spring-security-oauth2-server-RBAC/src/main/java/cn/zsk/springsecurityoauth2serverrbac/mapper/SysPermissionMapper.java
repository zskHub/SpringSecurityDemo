package cn.zsk.springsecurityoauth2serverrbac.mapper;

import cn.zsk.springsecurityoauth2serverrbac.entity.SysPermissionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:zsk
 * @CreateTime:2020-04-03 13:38
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermissionEntity> {
    /**
     * 通过用户id查询用户授权信息
     * @param userId
     * @return: java.util.List<cn.zsk.springsecurityoauth2serverrbac.entity.SysPermissionEntity>
     * @throws
     * @time: 2020-04-03 14:07
     * @author: zsk
     */
    List<SysPermissionEntity> findSysPermissionListByUserId(Long userId);
}
