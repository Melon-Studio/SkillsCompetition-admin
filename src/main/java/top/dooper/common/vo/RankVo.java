package top.dooper.common.vo;

public class RankVo {
    private Integer id;
    private String work_id;
    private String sid;
    private String username;
    private String score;
    private String ranking;

    public RankVo(Integer id, String work_id, String sid, String username, String score, String ranking) {
        this.id = id;
        this.work_id = work_id;
        this.sid = sid;
        this.username = username;
        this.score = score;
        this.ranking = ranking;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWork_id() {
        return work_id;
    }

    public void setWork_id(String work_id) {
        this.work_id = work_id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }
}
