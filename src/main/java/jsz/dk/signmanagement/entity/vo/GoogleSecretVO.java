package jsz.dk.signmanagement.entity.vo;

import lombok.Data;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.entity
 * @ClassName: GoogleSecretVO
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/15 18:05
 */
@Data
public class GoogleSecretVO {
    private String secret;
    private String secretQrCode;
}
