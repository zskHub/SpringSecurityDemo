package cn.zsk.springsecurityoauth2server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:zsk
 * @CreateTime:2019-12-20 16:02
 */
@TableName("sys_role")
@Data
@Accessors(chain = true)
public class SysRoleEntity implements Serializable {

    @TableId
    private Long id;

    private Long parentId;

    private String name;

    private String enname;

    private String description;

    private Date createTime;

    private Date lastUpdateTime;
}
