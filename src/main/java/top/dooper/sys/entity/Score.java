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
 * @since 2023-11-25
 */
@TableName("sc_score")
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评分ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 作品ID
     */
    private Integer workId;

    /**
     * 分数
     */
    private Double score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{" +
            "id=" + id +
            ", userId=" + userId +
            ", workId=" + workId +
            ", score=" + score +
        "}";
    }
}
