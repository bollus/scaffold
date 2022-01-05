package jsz.dk.scaffold.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.configuration
 * @ClassName: DefaultProperties
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/13 19:47
 */
@ConfigurationProperties(DefaultProperties.PREFIX)
public class DefaultProperties {
    public static final String PREFIX = "advice";

    /**
     * 统一返回过滤包
     */
    private List<String> adviceFilterPackage = new ArrayList<>();

    /**
     * 统一返回过滤类
     */
    private List<String> adviceFilterClass = new ArrayList<>();


    public List<String> getAdviceFilterPackage() {
        return adviceFilterPackage;
    }

    public void setAdviceFilterPackage(List<String> adviceFilterPackage) {
        this.adviceFilterPackage = adviceFilterPackage;
    }

    public List<String> getAdviceFilterClass() {
        return adviceFilterClass;
    }

    public void setAdviceFilterClass(List<String> adviceFilterClass) {
        this.adviceFilterClass = adviceFilterClass;
    }
}
