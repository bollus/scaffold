package jsz.dk.scaffold.entity;

import jsz.dk.scaffold.common.entity.SourceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.entity
 * @ClassName: User
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/02 21:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends SourceEntity {
    private String username;
    private String password;
    private String googleSecret;
}
