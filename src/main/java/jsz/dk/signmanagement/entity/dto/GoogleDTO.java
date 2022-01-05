package jsz.dk.signmanagement.entity.dto;

import lombok.Data;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.entity
 * @ClassName: GoogleDTO
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/15 23:53
 */
@Data
public class GoogleDTO {
    private String secret;
    private Long code;
}
