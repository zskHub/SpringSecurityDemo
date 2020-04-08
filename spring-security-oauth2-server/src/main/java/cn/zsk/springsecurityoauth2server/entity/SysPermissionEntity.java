package cn.zsk.springsecurityoauth2server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author:zsk
 * @CreateTime:2019-12-20 15:03
 */
@TableName("sys_permission")
@Data
@Accessors(chain = true)
public class SysPermissionEntity implements Serializable {

    @TableId
    private Long id;

    //父权限
    private String parentId;

    //权限名称
    private String name;

    //权限英文名称
    private String enname;

    //授权路径
    private String url;

    //备注
    private String description;

    private Date createTime;

    private Date lastUpdateTime;

}
