package jsz.dk.scaffold.enums;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.enums
 * @ClassName: OperationType
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/12 16:20
 */
public enum OperationType {
    /**
     * 操作类型
     */
    UNKNOWN("unknown"),
    DELETE("delete"),
    SELECT("select"),
    UPDATE("update"),
    INSERT("insert");

    private String value;

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    OperationType(String s){
        this.value = s;
    }
}
