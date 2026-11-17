# PROG5121 PoE — Part 3: QuickChat Store Data & Display Report

**Student Number:** ST10362279 | **IDE:** Apache NetBeans | **Build:** Maven | **Tests:** JUnit 5 | **CI:** GitHub Actions (`TestJava.yml`)

## Project Structure
- `src/ChatApp.java` — Main console application (Registration, Login, QuickChat Menu, Stored Messages Menu).
- `src/Login.java` — User registration and login validation logic.
- `src/Message.java` — Message validation, hashing, JSON read/write, parallel arrays, search, delete, and reports.
- `test/LoginTest.java` — JUnit 5 tests for `Login`.
- `test/MessageTest.java` — JUnit 5 tests for `Message` (Parts 2 & 3).
- `.github/workflows/TestJava.yml` — Automated GitHub Actions workflow running `mvn -B clean test`.
