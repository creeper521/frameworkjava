package com.bitejiuyeke.biteportalservice.user.entity.dto;

import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import com.bitejiuyeke.biteportalservice.user.entity.vo.UserVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * C端用户DTO
 */
@Data
public class UserDTO extends LoginUserDTO {

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 对象转换
     * @return
     */
    public UserVO convertToVO() {
        UserVO userVo = new UserVO();
        BeanUtils.copyProperties(this, userVo);
        userVo.setNickName(this.getUserName());
        return userVo;
    }
}