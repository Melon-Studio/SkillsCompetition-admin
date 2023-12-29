package top.dooper.utils;

import top.dooper.sys.entity.Rank;
import top.dooper.sys.entity.Score;

import java.math.BigDecimal;
import java.util.*;

public class ScoreUtils {

    public static List<Rank> calculateAverageScore(List<Score> allScores) {
        Map<Integer, List<Double>> workIdToScores = new HashMap<>();

        for (Score score : allScores) {
            int workId = score.getWorkId();
            double currentScore = score.getScore();

            workIdToScores.computeIfAbsent(workId, k -> new ArrayList<>()).add(currentScore);
        }

        List<Rank> averageScores = new ArrayList<>();
        for (Map.Entry<Integer, List<Double>> entry : workIdToScores.entrySet()) {
            int workId = entry.getKey();
            List<Double> scores = entry.getValue();

            double sum = 0.0;
            for (double score : scores) {
                sum += score;
            }

            double average = sum / scores.size();
            BigDecimal bigDecimal = new BigDecimal(average);
            double res = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

            Rank rank = new Rank(null, String.valueOf(workId), res, null);
            averageScores.add(rank);
        }

        return averageScores;
    }

    public static List<Rank> rankScores(List<Rank> ranks) {
        ranks.sort(Comparator.comparingDouble(Rank::getScore));

        Collections.reverse(ranks);

        int rank = 1;
        for (Rank r : ranks) {
            int a = rank++;
            r.setRanking(String.valueOf(a));
        }

        return ranks;
    }
}
