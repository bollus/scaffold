package jsz.dk.scaffold.common.entity;

import lombok.extern.slf4j.Slf4j;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.entity
 * @ClassName: CustomException
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/14 16:51
 */
@Slf4j
@SuppressWarnings("unused")
public class CustomException extends Exception{
    private final String tag;
    private Integer code;
    private String message;
    private String stack;

    public CustomException(String tag, Integer code, String stack , String message) {
        this.tag = tag;
        this.code = code;
        this.message = message;
        this.stack = stack;
    }

    public CustomException(String tag, String stack , String message) {
        this.tag = tag;
        this.code = 1005;
        this.message = message;
        this.stack = stack;
    }

    public CustomException(String tag, String message, Integer code) {
        this.tag = tag;
        this.code = code;
        this.message = message;
    }

    public CustomException(String tag, String message) {
        this.tag = tag;
        this.code = 1005;
        this.message = message;
    }

    public CustomException(String message, Integer code) {
        this.tag = "";
        this.code = code;
        this.message = message;
    }

    public String getTag(){return this.tag;}

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setCode(String tag , Integer code) {
        this.code = code;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getMsg() {
        return this.message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    public void printErr() {
        log.error("[tag]={}; [stack]={}; [message]={}", this.tag + this.code, this.stack, this.message);
    }
}
