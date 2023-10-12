package top.dooper.business.entity;

import java.util.List;

public class HtmlQualityReport {
    public HtmlQualityReport(int score, List<String> deductions) {
        this.score = score;
        this.deductions = deductions;
    }

    private int score;
    private List<String> deductions;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getDeductions() {
        return deductions;
    }

    public void setDeductions(List<String> deductions) {
        this.deductions = deductions;
    }
}
