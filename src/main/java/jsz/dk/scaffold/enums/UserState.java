package jsz.dk.scaffold.enums;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.enums
 * @ClassName: UserState
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/03 18:12
 */
public enum UserState {
    ENABLE("正常"),
    DISABLE("禁用"),
    BAN("封号");

    UserState(String desc){
    }
}
