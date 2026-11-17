# PROG5121 PoE — Part 3: QuickChat Store Data & Display Report

**Student Number:** ST10362279 | **IDE:** Apache NetBeans | **Build:** Maven | **Tests:** JUnit 5 | **CI:** GitHub Actions (`TestJava.yml`)

## What Part 3 adds
- **Dynamic Parallel Arrays (no hard-coding):**
  - `sentMessages` — stores all sent messages.
  - `disregardedMessages` — stores all disregarded messages.
  - `storedMessages` — reads `messages.json` into an array of stored messages.
  - `messageHashes` & `messageIds` — parallel arrays aligned with sender, recipient, message text, and status flag.
- **Fourth Main Menu Option (`4) Stored Messages`):**
  - `a)` Display sender and recipient of all stored messages.
  - `b)` Display the longest message.
  - `c)` Search for a message ID and display the corresponding recipient and message.
  - `d)` Search for all messages for a particular recipient.
  - `e)` Delete a message using the message hash.
  - `f)` Display a report listing the full details of all stored messages.
  - `g)` Display a report of all sent messages (Message Hash, Recipient, Message).
- **Automated Unit Tests:**
  - `LoginTest` (13 tests) and `MessageTest` (15 tests, covering all Part 2 and Part 3 brief test cases) run automatically via `.github/workflows/TestJava.yml`.

## Run & Test
Open `ChatAppPart3` in Apache NetBeans (`File > Open Project`), run `ChatApp`, and execute unit tests with `mvn test`.
