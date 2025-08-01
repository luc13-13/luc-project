package ${package}.domain.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <pre>
 * 实体基类
 * </pre>
 *
 * @author ${author}
 * @date ${date}
 */
@Data
@Accessors(chain = true)
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    @Column(name = "create_by", length = 50)
    private String createBy;

    /**
     * 更新者
     */
    @Column(name = "update_by", length = 50)
    private String updateBy;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 创建前自动设置创建时间
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    /**
     * 更新前自动设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
