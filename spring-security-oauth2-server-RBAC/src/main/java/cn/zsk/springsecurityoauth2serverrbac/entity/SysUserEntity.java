package cn.zsk.springsecurityoauth2serverrbac.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:zsk
 * @CreateTime:2019-12-20 14:10
 */
@TableName("sys_user")
@Data
@Accessors(chain = true)
public class SysUserEntity implements Serializable {

    @TableId
    private Long id;

    private String userName;

    private String password;

    private String phone;

    private String email;

    private Date createTime;

    private Date lastUpdateTime;
}
