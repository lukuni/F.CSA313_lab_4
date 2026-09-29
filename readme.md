# Лаборатори №4: Нэгжийн тестийн эхлэл — JUnit 5

**F.CSA313 — Программ хангамжийн чанарын баталгаа ба туршилт (2026)**


 **Оюутны нэр**: Чимэддагва Оюумаа 

 **Оюутны код**: B232270133 

---

## 1. Ажлын орчин

Ажлыг macOS (Apple Silicon) дээр Homebrew-ээр суулгасан OpenJDK болон Maven ашиглан гүйцэтгэсэн.

### `java -version`

```
openjdk version "27" 2026-09-15
OpenJDK Runtime Environment Homebrew (build 27)
OpenJDK 64-Bit Server VM Homebrew (build 27, mixed mode, sharing)
```

### `mvn -version`

```
Apache Maven 3.9.16 (2bdd9fddda4b155ebf8000e807eb73fd829a51d5)
Maven home: /opt/homebrew/Cellar/maven/3.9.16/libexec
Java version: 27, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk/27/libexec/openjdk.jdk/Contents/Home
Default locale: en_MN, platform encoding: UTF-8
OS name: "mac os x", version: "26.1", arch: "aarch64", family: "mac"
```

> Лабын шаардлага нь JDK 17 буюу түүнээс дээш. `pom.xml`-д `maven.compiler.release = 17` гэж заасан тул код Java 17-той нийцтэйгээр compile хийгдэнэ.

---

## 2. Төслийн бүтэц

```
F.CSA313_lab_4/
├── pom.xml
├── .gitignore                  # target/, .idea/, .DS_Store, *.docx
├── README.md
├── results/
│   ├── mvn-test.txt            # ногоон ажиллагаа (BUILD SUCCESS)
│   └── mvn-test-mutant.txt     # мутацитай ажиллагаа (BUILD FAILURE)
└── src/
    ├── main/java/mn/edu/must/sqat/GradeCalculator.java
    └── test/java/mn/edu/must/sqat/GradeCalculatorTest.java
```

### `pom.xml`-д хийсэн өөрчлөлтүүд (archetype-ийн анхны хувилбараас)

| Өөрчлөлт | Шалтгаан |
|---|---|
| `junit 4.11` хамаарлыг устгаж `junit-jupiter 5.10.2` нэмсэн | JUnit 5 ашиглах |
| `maven-surefire-plugin 3.2.5` зааж өгсөн | Surefire 2.x нь JUnit 5-ын тестийг танихгүй |
| `maven.compiler.source/target 1.7`-ийг устгаж `maven.compiler.release 17` болгосон | Lambda (`assertThrows`) compile хийгдэхгүй, JDK 20+ дээр "Source option 7 is no longer supported" алдаа гарна |
| `maven-compiler-plugin 3.13.0` зааж өгсөн | `release` тохиргоог дэмжих шинэ хувилбар |
| `App.java`, `AppTest.java`-г устгасан | AppTest нь JUnit 4 ашигладаг тул compile хийгдэхгүй |

### Ажиллуулах

```bash
mvn test
```

---

## 3. GradeCalculator

| Метод | Үүрэг | Буруу оролт |
|---|---|---|
| `letterGrade(double score)` | 90+ → A, 80–89 → B, 70–79 → C, 60–69 → D, <60 → F | `score < 0`, `score > 100` эсвэл `NaN` бол `IllegalArgumentException` |
| `totalScore(att, lab, quiz1, quiz2, exam)` | Ирц(10) + лаб/бие даалт(40) + сорил1(10) + сорил2(10) + шалгалт(30)-ийн нийлбэр | Аль нэг нь сөрөг, дээд хязгаараас хэтэрсэн эсвэл `NaN` бол `IllegalArgumentException` |

Хязгаарын шалгалтуудыг бүгд `>=` харьцуулалтаар бичсэн (жишээ нь `score >= 90`), учир нь 90, 80, 70, 60 оноо нь дээд дүнд хамаарна.
Мөн `Double.isNaN()`-г тусад нь шалгасан, учир нь NaN-тай хийсэн бүх харьцуулалт `false` буцаадаг тул `score < 0 || score > 100` шалгалт NaN-ийг барьж чаддаггүй.

---

## 4. Тестүүд

Бүх тест **Arrange–Act–Assert (AAA)** бүтэцтэй бөгөөд тус бүр монгол хэл дээрх `@DisplayName`-тэй.

### 4.1. `@Test` методууд (12)

| # | Метод | Шалгах зүйл | Ангилал |
|---|---|---|---|
| 1 | `typicalScoresGiveCorrectGrades` | 95→A, 85→B, 75→C, 65→D, 30→F | Ердийн утга |
| 2 | `ninetyIsExactlyA` | 90 → A | Хязгаар |
| 3 | `justBelowNinetyIsB` | 89.99 → B | Хязгаар |
| 4 | `sixtyIsExactlyD` | 60 → D | Хязгаар |
| 5 | `justBelowSixtyIsF` | 59.99 → F | Хязгаар |
| 6 | `validRangeEndpoints` | 0 → F, 100 → A | Хязгаар |
| 7 | `negativeScoreThrows` | `letterGrade(-1)` → exception | Буруу оролт (`assertThrows`) |
| 8 | `scoreAboveHundredThrows` | `letterGrade(101)` → exception | Буруу оролт (`assertThrows`) |
| 9 | `nanScoreThrows` | `letterGrade(NaN)` → exception | Буруу оролт (`assertThrows`) |
| 10 | `maxComponentsSumToHundred` | `totalScore(10, 40, 10, 10, 30)` = 100 | Ердийн утга |
| 11 | `negativeAttendanceThrows` | `att = -5` → exception | Буруу оролт (`assertThrows`) |
| 12 | `labAboveMaxThrows` | `lab = 41` → exception | Буруу оролт (`assertThrows`) |

### 4.2. `@ParameterizedTest` методууд (3)

| # | Метод | `@CsvSource` мөр | Шалгах зүйл |
|---|---|---|---|
| 13 | `letterGradeBoundaries` | 11 | 95, 90, 89.99, 80, 79.99, 70, 69.99, 60, 59.99, 0, 100 — бүх хязгаарын утгын хоёр тал |
| 14 | `totalScoreValidInputs` | 5 | Хүчинтэй оноонуудын нийлбэр (0, 51, 60, 84, 100) |
| 15 | `totalScoreOutOfRangeThrows` | 10 | Таван хэсэг бүрийн доод (−) ба дээд (+) хязгаараас гарсан утга → exception |

### 4.3. Шаардлагын хамрах хүрээ

| Шаардлага | Хаана |
|---|---|
| Хязгаарын утгууд 90, 89.99, 60, 59.99, 0, 100 | #2–#6, #13 |
| `assertThrows` — `letterGrade` | #7, #8, #9 |
| `assertThrows` — `totalScore` (сөрөг ба хэтэрсэн) | #11, #12, #15 |
| `totalScore` зөв нийлбэр (10+40+10+10+30 = 100) | #10, #14 |
| `@ParameterizedTest` — `letterGrade` ба `totalScore` | #13, #14, #15 |

---

## 5. Тестийн үр дүн

- **Тестийн методын тоо: 15** (12 `@Test` + 3 `@ParameterizedTest`)
- **`results/mvn-test.txt`-ийн сүүлийн мөр:**

```
Tests run: 38, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Surefire нь `@CsvSource`-ийн мөр бүрийг тусдаа тест гэж тоолдог тул: **12 + 11 + 5 + 10 = 38**.

---

## 6. Мутацийн туршилт

`letterGrade` доторх `score >= 90` нөхцөлийг санаатайгаар `score > 90` болгож `mvn test` ажиллуулсан (`results/mvn-test-mutant.txt`):

```
Tests run: 38, Failures: 2, Errors: 0, Skipped: 0
BUILD FAILURE
```

**Унасан тестүүд:**

| Тест | Мэдээлэл |
|---|---|
| `GradeCalculatorTest.ninetyIsExactlyA:44` — «90 оноо яг A дүн байх ёстой» | `expected: <A> but was: <B>` |
| `GradeCalculatorTest.letterGradeBoundaries(double, String)[2]` (мөр 116) — `90, A` өгөгдөл | `expected: <A> but was: <B>` |

`89.99 → B`, `95 → A`, `100 → A` зэрэг бусад тест унаагүй, учир нь мутаци зөвхөн яг 90 оноон дээрх үр дүнг өөрчилдөг.
Үүний дараа `>=` болгож буцааж засаад `mvn test`-ийг дахин ажиллуулахад бүх 38 тест ногоон болсон (`results/mvn-test.txt`).

### Нэмэлт туршилт: NaN шалгалтыг хасах

`letterGrade` доторх `Double.isNaN(score) ||` шалгалтыг түр хасаж `mvn test` ажиллуулахад:

```
[ERROR]   GradeCalculatorTest.nanScoreThrows:104 Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.
[ERROR] Tests run: 38, Failures: 1, Errors: 0, Skipped: 0
```

Өөрөөр хэлбэл NaN шалгалтгүй бол `letterGrade(NaN)` exception шидэхгүй, чимээгүйхэн «F» буцаадаг. Туршилтын дараа `git checkout`-оор кодыг анхны байдалд нь буцаасан (энэ өөрчлөлт commit хийгдээгүй).

---

## 7. Дүгнэлт

Би GradeCalculator классын `letterGrade` ба `totalScore` методуудад 15 тестийн метод бичсэн бөгөөд Surefire үүнийг 38 тест гэж тоолсон. Тестүүдээ ердийн утга, хязгаарын утга (90, 89.99, 60, 59.99, 0, 100) болон буруу оролт гэсэн бүлгүүдэд хувааж, бүгдийг AAA бүтцээр бичсэн. `score >= 90`-ийг `score > 90` болгосон мутацид `ninetyIsExactlyA` болон `letterGradeBoundaries`-ийн `[90, A]` мөр «expected: <A> but was: <B>» мэдээлэлтэй унасан. Харин 89.99 ба 95 зэрэг утгууд унаагүй нь энэ төрлийн алдаа зөвхөн яг хязгаар дээр илэрдэг тул хязгаарын утгыг тестлэхгүй бол олдохгүйг харуулсан. Хамгийн сонирхолтой нь `nanScoreThrows` тест байсан: `Double.NaN`-тай хийсэн бүх харьцуулалт `false` буцаадаг тул `score < 0 || score > 100` шалгалт NaN-ийг барьж чаддаггүй. `isNaN` шалгалтыг түр хасаж үзэхэд `letterGrade(NaN)` exception шидэхгүй чимээгүйхэн «F» буцааж, `nanScoreThrows` тест унасан. Эндээс pass болсон тест нь зөв тест гэсэн үг биш бөгөөд хязгаар болон онцгой утгуудыг зориуд шалгах хэрэгтэй гэдгийг ойлгосон.

---

## 8. Нэмэлт даалгавар: AI өнцөг

**Дүгнэлт.** AI нь хязгаарын утгын хоёр талыг (90/89.99, 80/79.99, 70/69.99, 60/59.99), `letterGrade`-ийн NaN тохиолдол болон `totalScore`-ийн таван хэсэг бүрийн доод, дээд хязгаарыг санасан нь сайн байсан. [Өөрийн бодлоо бичнэ үү: AI-ийн тестээс та өөрөө юуг мартах байсан бэ, жишээ нь NaN эсвэл хэсэг бүрийн хязгаар.] Гэвч AI 0–100 мужийн гадна талын хязгаарыг зөвхөн −1 ба 101-ээр шалгасан тул `score > 100.5` эсвэл `score < -0.5` гэсэн алдаатай код ч бүх тестийг давсан. Мөн `letterGrade`-д NaN тест бичсэн мөртлөө `totalScore`-д бичээгүй, мөн бүх нийлбэрийн тест бүхэл тоогоор дууссан тул `Math.round` хийсэн алдааг илрүүлж чадаагүй. Чанартай тестүүд нь `letterGradeBoundaries` ба `totalScoreOutOfRangeThrows` parameterized тестүүд байсан, учир нь нэг методоор олон хязгаарыг системтэйгээр хамарсан. Харин 90, 89.99, 60, 59.99-ийн тусдаа `@Test` методууд parameterized тесттэй давхардсан, `typicalScoresGiveCorrectGrades` нь 5 assertion-ийг `assertAll`-гүй бичсэн тул эхний алдаа гарахад бусдыг нь харуулахгүй, мөн `assertThrows` тестүүд exception-ий мессежийг шалгадаггүй. Эндээс AI-ийн тест бүх тест ногоон байсан ч «сайн тест» гэсэн баталгаа биш бөгөөд мутаци ашиглаж шалгах нь AI-ийн гаралтыг шүүмжтэй үнэлэх үр дүнтэй арга гэдгийг ойлгосон.

