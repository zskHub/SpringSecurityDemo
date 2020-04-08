package cn.zsk.springsecurityoauth2server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author:zsk
 * @CreateTime:2019-12-20 16:50
 */
@TableName
@Data
@Accessors(chain = true)
public class SysRolePermissionEntity implements Serializable {

    @TableId
    private Long id;

    private Long roleId;

    private Long permissionId;
}
