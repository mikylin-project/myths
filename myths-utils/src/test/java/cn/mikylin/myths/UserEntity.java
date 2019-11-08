package cn.mikylin.myths;

import lombok.Data;
import java.util.Date;

@Data
public class UserEntity {
    private String name;
    private Date birthDay;
    private Integer age;
}
