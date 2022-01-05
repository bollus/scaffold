package jsz.dk.scaffold.enums;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.enums
 * @ClassName: OperationUnit
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/12 16:24
 */
public enum OperationUnit {
    /**
     * 被操作的单元
     */
    UNKNOWN("unknown"),
    USER("user"),
    GOOGLE("google"),
    EMPLOYEE("employee"),
    Redis("redis");

    private String value;

    OperationUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
