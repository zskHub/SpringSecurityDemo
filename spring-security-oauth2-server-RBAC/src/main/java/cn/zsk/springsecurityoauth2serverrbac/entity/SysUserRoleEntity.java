package cn.zsk.springsecurityoauth2serverrbac.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author:zsk
 * @CreateTime:2019-12-20 17:04
 */
@TableName("sys_user_role")
@Data
@Accessors(chain = true)
public class SysUserRoleEntity implements Serializable {

    @TableId
    private Long id;

    private Long userId;

    private Long roleId;
}
