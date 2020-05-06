package com.wrz.crud.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wrz
 * @since 2020-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer roleId;

    public SysUserRole(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
