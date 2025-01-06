# WordleFX

WordleFX is an implementation of the popular **Wordle** game built using **JavaFX**. The game challenges players to guess a hidden word within a limited number of attempts, providing feedback on the correctness of the guessed letters.

## Features

- User interface built with JavaFX.
- Word validation mechanism.
- Visual feedback with color hints:
  - Green: Correct letter in the correct position.
  - Yellow: Correct letter in the wrong position.
  - Gray: Incorrect letter.
- Configurable word length and number of attempts.
- Random word generation for each game.

## Requirements

- Java 11 or higher.
- Maven (optional, for building the project).

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Jakubowsky97/WordleFX.git
```
2. Navigate to the project directory:
   ```bash
cd WordleFX
```

3. Compile and run the project:
   ```bash
mvn clean javafx:run
```
