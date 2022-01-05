package jsz.dk.scaffold.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jsz.dk.scaffold.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.mapper
 * @ClassName: UserMapper
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/02 22:37
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
