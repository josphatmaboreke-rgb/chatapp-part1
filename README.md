# PROG5121 PoE — Part 1: Registration and Login Feature (Chat App)

**Student Number:** ST10362279
**Module:** PROG5121 — Introduction to Programming
**IDE:** Apache NetBeans | **Build tool:** Maven | **Testing:** JUnit 5 | **CI:** GitHub Actions

## What this application does

This is a **console** application (no GUI) that forms the first part of a chat app:

1. **Registration** — the user enters a username, password and South African cell phone number.
   - Username must contain an underscore `_` and be no more than 5 characters.
   - Password must be at least 8 characters and contain a capital letter, a number and a special character.
   - Cell number must contain the international code `+27` followed by no more than 10 digits.
2. **Login** — the user logs in with the same username and password and is greeted by name,
   or told the details are incorrect.

## Project structure

```
part1/
├── pom.xml                      Maven build file (JUnit dependency + Surefire)
├── README.md
├── .github/workflows/maven.yml  GitHub Action: runs my tests on every push
└── src/
    ├── main/java/za/co/iie/chatapp/
    │   ├── Login.java           All my registration/login logic (testable)
    │   └── ChatApp.java         Console menu with main()
    └── test/java/za/co/iie/chatapp/
        └── LoginTest.java       13 JUnit 5 tests using the brief's test data
```

## Methods in `Login.java`

| Method | What it does |
|---|---|
| `boolean checkUserName(String)` | Checks for an underscore and max 5 characters |
| `boolean checkPasswordComplexity(String)` | Checks 8+ chars, capital, number, special character |
| `boolean checkCellPhoneNumber(String)` | Regex check for `+27` and max 10 digits |
| `String registerUser(...)` | Returns the correct registration message |
| `boolean loginUser(String, String)` | Checks the login details against the registered details |
| `String returnLoginStatus(String, String)` | Returns the welcome / failure message |

## How to run

In NetBeans: right-click the project → **Run** (or press F6).
From the terminal:

```bash
mvn clean test          # runs my 13 JUnit tests
mvn exec:java           # runs the console app
```

## Test data used

| Test | Data | Expected |
|---|---|---|
| Username correct | `kyl_1` | true |
| Username incorrect | `kyle!!!!!!!` | false |
| Password correct | `Ch&&sec@ke99!` | true |
| Password incorrect | `password` | false |
| Cell number correct | `+27838968976` | true |
| Cell number incorrect | `08966553` | false |

## Reference

Oracle (2024) *Class Pattern (java.util.regex)*, Java SE 17 API Documentation. Available at:
https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html (Accessed: 18 September 2026).
