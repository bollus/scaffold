package jsz.dk.scaffold.common.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import jsz.dk.scaffold.enums.ResponseCode;
import jsz.dk.scaffold.utils.IPUtil;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.entity
 * @ClassName: ResponseParent
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/03 18:46
 */
@Data
public class ResponseParent<T> implements Serializable {
    private Integer code;
    private String error;
    private String clientIp;
    private String requestAction;
    private String message;
    private T data;

    @SuppressWarnings("unused")
    public void init() {
        ServletRequestAttributes attr=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attr != null;
        HttpServletRequest request =attr.getRequest();
        this.error = "";
        this.setClientIp(IPUtil.getIpAddr(request));
        this.setRequestAction(request.getRequestURL() + ":" + request.getMethod());
    }

    @SuppressWarnings("unused")
    public static <T> ResponseParent<T> succeed() {
        ResponseParent<T> responseParent = new ResponseParent<>();
        responseParent.init();
        responseParent.code = 200;
        responseParent.data = null;
        return responseParent;
    }

    @SuppressWarnings("unused")
    public static <T> ResponseParent<T> succeed(String message) {
        ResponseParent<T> responseParent = new ResponseParent<>();
        responseParent.init();
        responseParent.code = 200;
        responseParent.data = null;
        responseParent.message = message;
        return responseParent;
    }

    public static <T> ResponseParent<T> succeed(T data) {
        ResponseParent<T> responseParent = new ResponseParent<>();
        responseParent.init();
        responseParent.code = 200;
        responseParent.setData(data);
        return responseParent;
    }

    public static <T> ResponseParent<T> succeed(String message, T data) {
        ResponseParent<T> responseParent = new ResponseParent<>();
        responseParent.init();
        responseParent.code = 200;
        responseParent.message = message;
        responseParent.setData(data);
        return responseParent;
    }

    public static <T> ResponseParent<T> fail(Integer code, String msg) {
        ResponseParent<T> responseParent = new ResponseParent<>();
        responseParent.init();
        responseParent.code = code;
        responseParent.error = msg;
        return responseParent;
    }

    @SuppressWarnings("unused")
    public static <T> ResponseParent<T> fail(Integer code, String msg, T data) {
        ResponseParent<T> responseParent = new ResponseParent<>();
        responseParent.init();
        responseParent.code = code;
        responseParent.error = msg;
        responseParent.setData(data);
        return responseParent;
    }

    public static <T extends ResponseCode> ResponseParent<T> fail(T resultEnum) {
        ResponseParent<T> responseParent = new ResponseParent<>();
        responseParent.init();
        responseParent.code = resultEnum.getCode();
        responseParent.error = resultEnum.getMessage();
        return responseParent;
    }

    /**
     * 获取 json
     */
    @SuppressWarnings("unused")
    public String buildResultJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", this.code);
        jsonObject.put("msg", this.message);
        jsonObject.put("data", this.data);
        return JSON.toJSONString(jsonObject, SerializerFeature.DisableCircularReferenceDetect);
    }
}
