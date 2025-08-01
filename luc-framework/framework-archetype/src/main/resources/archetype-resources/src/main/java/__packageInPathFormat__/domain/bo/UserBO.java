package ${package}.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <pre>
 * 用户业务对象
 * </pre>
 *
 * @author ${author}
 * @date ${date}
 */
@Data
@Accessors(chain = true)
public class UserBO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否为新用户
     */
    private Boolean isNewUser;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
}
