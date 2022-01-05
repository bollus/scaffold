package jsz.dk.scaffold.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jsz.dk.scaffold.common.annotations.CreateTime;
import jsz.dk.scaffold.common.annotations.SnowflakeId;
import jsz.dk.scaffold.common.annotations.State;
import jsz.dk.scaffold.common.annotations.UpdateTime;
import lombok.Data;

import java.util.Date;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.entity
 * @ClassName: SourceEntity
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/02 21:31
 */
@Data
public class SourceEntity {
    @SnowflakeId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @State(value = "ENABLE")
    private String status;
    @CreateTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @UpdateTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
