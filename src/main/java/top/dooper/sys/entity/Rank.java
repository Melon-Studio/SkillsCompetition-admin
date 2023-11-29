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
 * @since 2023-11-27
 */
@TableName("sc_rank")
public class Rank implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    private String workId;

    /**
     * 分数
     */
    private Double score;

    /**
     * 排名
     */
    private String ranking;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        return "Rank{" +
            "id=" + id +
            ", workId=" + workId +
            ", score=" + score +
            ", ranking=" + ranking +
        "}";
    }

    public Rank(Integer id, String workId, Double score, String ranking) {
        this.id = id;
        this.workId = workId;
        this.score = score;
        this.ranking = ranking;
    }
}
