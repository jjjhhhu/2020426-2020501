package com.wrz.crud.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String phone;

    private String email;

    private String gender;

    public SysUser(String username, String password, String phone, String email) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public SysUser(Integer id,String username, String password, String phone, String email, String gender) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
    }
}
