package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GradeCalculator-ийн нэгжийн тестүүд")
class GradeCalculatorTest {

    private static final double DELTA = 1e-9;

    // ---------- letterGrade: ердийн утгууд ----------

    @Test
    @DisplayName("Ердийн утгууд: 95→A, 85→B, 75→C, 65→D, 30→F")
    void typicalScoresGiveCorrectGrades() {
        // Arrange
        GradeCalculator calc = new GradeCalculator();
        // Act
        String a = calc.letterGrade(95);
        String b = calc.letterGrade(85);
        String c = calc.letterGrade(75);
        String d = calc.letterGrade(65);
        String f = calc.letterGrade(30);
        // Assert
        assertEquals("A", a);
        assertEquals("B", b);
        assertEquals("C", c);
        assertEquals("D", d);
        assertEquals("F", f);
    }

    // ---------- letterGrade: хязгаарын утгууд ----------

    @Test
    @DisplayName("90 оноо яг A дүн байх ёстой (хязгаарын тохиолдол)")
    void ninetyIsExactlyA() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(90.0);             // Act
        assertEquals("A", grade);                          // Assert
    }

    @Test
    @DisplayName("89.99 оноо B дүн байх ёстой (A-ийн хязгаарын доод тал)")
    void justBelowNinetyIsB() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(89.99);            // Act
        assertEquals("B", grade);                          // Assert
    }

    @Test
    @DisplayName("60 оноо яг D дүн байх ёстой (тэнцэх хязгаар)")
    void sixtyIsExactlyD() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(60.0);             // Act
        assertEquals("D", grade);                          // Assert
    }

    @Test
    @DisplayName("59.99 оноо F дүн байх ёстой (тэнцэх хязгаарын доод тал)")
    void justBelowSixtyIsF() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(59.99);            // Act
        assertEquals("F", grade);                          // Assert
    }

    @Test
    @DisplayName("Хүчинтэй мужийн хоёр төгсгөл: 0→F, 100→A")
    void validRangeEndpoints() {
        // Arrange
        GradeCalculator calc = new GradeCalculator();
        // Act
        String lowest = calc.letterGrade(0.0);
        String highest = calc.letterGrade(100.0);
        // Assert
        assertEquals("F", lowest);
        assertEquals("A", highest);
    }

    // ---------- letterGrade: буруу оролт ----------

    @Test
    @DisplayName("Сөрөг оноо (-1) IllegalArgumentException шидэх ёстой")
    void negativeScoreThrows() {
        GradeCalculator calc = new GradeCalculator();                             // Arrange
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-1)); // Act + Assert
    }

    @Test
    @DisplayName("100-аас их оноо (101) IllegalArgumentException шидэх ёстой")
    void scoreAboveHundredThrows() {
        GradeCalculator calc = new GradeCalculator();                              // Arrange
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(101)); // Act + Assert
    }

    @Test
    @DisplayName("NaN оноо IllegalArgumentException шидэх ёстой (F буцаах ёсгүй)")
    void nanScoreThrows() {
        GradeCalculator calc = new GradeCalculator();                                     // Arrange
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(Double.NaN)); // Act + Assert
    }

    // ---------- totalScore ----------

    @Test
    @DisplayName("Бүх хэсэг дээд оноотой бол нийлбэр 100 байх ёстой")
    void maxComponentsSumToHundred() {
        GradeCalculator calc = new GradeCalculator();              // Arrange
        double total = calc.totalScore(10, 40, 10, 10, 30);        // Act
        assertEquals(100.0, total, DELTA);                         // Assert
    }

    @Test
    @DisplayName("Сөрөг ирцийн оноо (att = -5) IllegalArgumentException шидэх ёстой")
    void negativeAttendanceThrows() {
        GradeCalculator calc = new GradeCalculator();                                           // Arrange
        assertThrows(IllegalArgumentException.class, () -> calc.totalScore(-5, 40, 10, 10, 30)); // Act + Assert
    }

    @Test
    @DisplayName("Дээд хязгаараас хэтэрсэн лабын оноо (lab = 41) IllegalArgumentException шидэх ёстой")
    void labAboveMaxThrows() {
        GradeCalculator calc = new GradeCalculator();                                           // Arrange
        assertThrows(IllegalArgumentException.class, () -> calc.totalScore(10, 41, 10, 10, 30)); // Act + Assert
    }
}
