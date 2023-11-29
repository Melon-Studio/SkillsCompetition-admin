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
 * @since 2023-11-20
 */
@TableName("sc_work")
public class Work implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 绑定的SID
     */
    private Long sid;

    /**
     * 解压的路径
     */
    private String path;

    /**
     * 评分
     */
    private Double score;

    /**
     * 评分组成员
     */
    private String scoreUser;

    /**
     * 上传日期
     */
    private String time;

    /**
     * 作品名称
     */
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
    public String getScoreUser() {
        return scoreUser;
    }

    public void setScoreUser(String scoreUser) {
        this.scoreUser = scoreUser;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Work{" +
            "id=" + id +
            ", sid=" + sid +
            ", path=" + path +
            ", score=" + score +
            ", scoreUser=" + scoreUser +
            ", time=" + time +
            ", name=" + name +
        "}";
    }
}
