package jsz.dk.signmanagement.entity.vo;

import lombok.Data;

import java.sql.Timestamp;


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
