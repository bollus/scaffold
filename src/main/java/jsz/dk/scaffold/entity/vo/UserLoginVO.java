package jsz.dk.scaffold.entity.vo;

import lombok.Data;


/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.entity
 * @ClassName: UserloginVO
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/15 19:22
 */
@Data
public class UserLoginVO {
    private String username;
    private String token;
    private long ts;
}
