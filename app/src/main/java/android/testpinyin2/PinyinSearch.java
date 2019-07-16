package android.testpinyin2;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PinyinSearch {
    private final List<Word> targets = new ArrayList<Word>();

    public PinyinSearch(List<String> list) throws PinyinException {
        for (String name : list) {
            Word word = new Word(name);
            targets.add(word);
        }
    }

    public List<Score> search(String input, int limit) throws PinyinException {
        Word word = new Word(input);
        return targets.stream().map(x -> {
            Score score = new Score();
            score.word = x;
            score.score = x.compareTo(word);
            return score;
        }).sorted().limit(limit).collect(Collectors.toList());
    }

    private static int getEditDistance(String s1, String s2) {
        int matrix[][];
        int s1_len; // length of s
        int s2_len; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char s_i; // ith character of s
        char t_j; // jth character of t
        int cost; // cost
        // Step 1
        s1_len = s1.length();
        s2_len = s2.length();
        if (s1_len == 0) {
            return s2_len;
        }
        if (s2_len == 0) {
            return s1_len;
        }
        matrix = new int[s1_len + 1][s2_len + 1];

        // Step 2
        for (i = 0; i <= s1_len; i++) {
            matrix[i][0] = i;
        }
        for (j = 0; j <= s2_len; j++) {
            matrix[0][j] = j;
        }

        // Step 3
        for (i = 1; i <= s1_len; i++) {
            s_i = s1.charAt(i - 1);
            // Step 4
            for (j = 1; j <= s2_len; j++) {
                t_j = s2.charAt(j - 1);
                // Step 5
                cost = (s_i == t_j) ? 0 : 1;
                // Step 6
                matrix[i][j] = Minimum(matrix[i - 1][j] + 1, matrix[i][j - 1] + 1,
                        matrix[i - 1][j - 1] + cost);
            }
        }
        // Step 7
        return matrix[s1_len][s2_len];
    }

    private static int Minimum(int a, int b, int c) {
        int im = a < b ? a : b;
        return im < c ? im : c;
    }

    class Word implements Comparable {
        final String word;
        final String pinyin1;
        final String pinyin2;

        Word(String word) throws PinyinException {
            this.word = word;
            this.pinyin1 = PinyinHelper.convertToPinyinString(word, ",", PinyinFormat.WITH_TONE_NUMBER);
            this.pinyin2 = PinyinHelper.convertToPinyinString(word, ",", PinyinFormat.WITHOUT_TONE);
        }

        @Override
        public String toString() {
            return word;
        }

        @Override
        public int compareTo(Object obj) {
            if (obj instanceof Word) {
                Word word = (Word) obj;
                int score1 = getEditDistance(this.pinyin1, word.pinyin1);
                int score2 = getEditDistance(this.pinyin2, word.pinyin2);
                return score1 + score2;
            }
            return 0;
        }
    }

    class Score implements Comparable {
        Word word;
        int score;

        @Override
        public int compareTo(Object obj) {
            if (obj instanceof Score) {
                return score - ((Score) obj).score;
            }
            return 0;
        }

        @Override
        public String toString() {
            return "{" +
                    "word=" + word +
                    ", score=" + score +
                    '}';
        }
    }

}
