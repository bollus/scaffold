package jsz.dk.scaffold.common.entity;

import jsz.dk.scaffold.enums.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.entity
 * @ClassName: ApiException
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/15 18:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private Integer status;
    private String message;
    private Object data;
    private Exception exception;
    private ResponseCode responseCode;
    public ApiException() {
        super();
    }
    public ApiException(Integer status, String message, Object data, Exception exception) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.exception = exception;
    }
    public ApiException(ResponseCode responseCode) {
        this(responseCode.getCode(),responseCode.getMessage(),null,null);
    }
    public ApiException(ResponseCode responseCode, Object data) {
        this(responseCode.getCode(),responseCode.getMessage(),data,null);
    }
    public ApiException(ResponseCode responseCode, Object data, Exception exception) {
        this(responseCode.getCode(),responseCode.getMessage(),data,exception);
    }
}
