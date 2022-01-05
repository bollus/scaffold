package jsz.dk.scaffold.configuration;

import jsz.dk.scaffold.interceptor.SqlInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.configuration
 * @ClassName: MybatisPlusConfig
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/03 19:23
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public SqlInterceptor createUpdateTimeInterceptor() {
        return new SqlInterceptor();
    }
}
