package mn.edu.must.sqat;

public class GradeCalculator {

    static final double MAX_ATT = 10;
    static final double MAX_LAB = 40;
    static final double MAX_QUIZ1 = 10;
    static final double MAX_QUIZ2 = 10;
    static final double MAX_EXAM = 30;

    // 90+ -> A, 80-89 -> B, 70-79 -> C, 60-69 -> D, <60 -> F 
    // score нь 0-100 хязгаараас гарвал IllegalArgumentException шиднэ
    public String letterGrade(double score) {
        // NaN-тай харьцуулалт бүгд false буцаадаг тул тусад нь шалгана
        if (Double.isNaN(score) || score < 0 || score > 100) {
            throw new IllegalArgumentException("Оноо 0-100 хооронд байх ёстой: " + score);
        }
        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    // Ирц(10), лаб+бие даалт(40), сорил1(10), сорил2(10), шалгалт(30)
    // Аль нэг нь сөрөг эсвэл дээд хязгаараасаа хэтэрсэн бол IllegalArgumentException шиднэ.
    public double totalScore(double att, double lab, double quiz1, double quiz2, double exam) {
        check("att", att, MAX_ATT);
        check("lab", lab, MAX_LAB);
        check("quiz1", quiz1, MAX_QUIZ1);
        check("quiz2", quiz2, MAX_QUIZ2);
        check("exam", exam, MAX_EXAM);
        return att + lab + quiz1 + quiz2 + exam;
    }

    private void check(String name, double value, double max) {
        if (Double.isNaN(value) || value < 0 || value > max) {
            throw new IllegalArgumentException(
                    name + " нь 0-" + (int) max + " хооронд байх ёстой: " + value);
        }
    }
}
