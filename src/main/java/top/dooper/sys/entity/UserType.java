package top.dooper.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author AbuLan
 * @since 2023-09-26
 */
@TableName("sc_usertype")
public class UserType implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "typeID", type = IdType.AUTO)
    private Integer typeID;
    private String Name;

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
