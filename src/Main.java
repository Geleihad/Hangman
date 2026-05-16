import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final int MAX_MISTAKES = 6;
    private static final String REGEX = "^[А-Яа-яЁё]$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private static final String START = "Y";
    private static final String QUIT = "N";
    private static final String MASK_SYMBOL = "*";

    private static final String[] HANGMAN_STAGES = {
            """
        +---+
         |   |
             |
             |
             |
             |
        =========
        """,
            """
        +---+
        |   |
        O   |
            |
            |
            |
       =========
       """,
            """
        +---+
        |   |
        O   |
        |   |
            |
            |
       =========
       """,
            """
        +---+
        |   |
        O   |
       /|   |
            |
            |
       =========
       """,
            """
        +---+
        |   |
        O   |
       /|\\  |
            |
            |
       =========
       """,
            """
        +---+
        |   |
        O   |
       /|\\  |
       /    |
            |
       =========
       """,
            """
        +---+
        |   |
        O   |
       /|\\  |
       / \\  |
            |
       =========
       """
    };

    private static final Set<Character> usedLetters = new TreeSet<>();
    private static final Scanner scanner = new Scanner(System.in);

    private static int wrongLettersCount;
    private static StringBuilder mask;
    private static String wordToGuess;

    public static void main(String[] args) {
        startGame();
    }

    private static void startGame() {
        while (wantsToPlay()) {
            System.out.println("Игра началась");
            initWord();
            playRound();
        }
        System.out.println("Вы вышли из игры");
        scanner.close();
    }

    private static boolean wantsToPlay() {
        System.out.printf("Введите '%s', чтобы начать игру%n", START);
        System.out.printf("Введите '%s', чтобы завершить работу%n", QUIT);

        String userInput = scanner.next();

        while (!userInput.equalsIgnoreCase(START) && !userInput.equalsIgnoreCase(QUIT)) {
            System.out.printf("Некорректный ввод. Введите '%s' или '%s'%n", START, QUIT);
            userInput = scanner.next();
        }

        return userInput.equalsIgnoreCase(START);
    }

    private static void initWord() {
        List<String> words = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("dict"))
                )
        ).lines().toList();

        wordToGuess = words.get(new Random().nextInt(words.size())).toLowerCase();
    }


    private static void playRound() {
        wrongLettersCount = 0;
        usedLetters.clear();
        mask = new StringBuilder(MASK_SYMBOL.repeat(wordToGuess.length()));
        while ((wrongLettersCount < MAX_MISTAKES) && (mask.indexOf(MASK_SYMBOL) != -1)) {
            showState();
            makeMove();
        }
        showState();
        endGame();
    }


    private static void makeMove() {

        String input = scanner.next();

        if (!isRussianLetter(input)) {
            System.out.println("Некорректный ввод. Введите 1 символ кириллицы");
            return;
        }

        char inputLetter = Character.toLowerCase(input.charAt(0));
        if (usedLetters.contains(inputLetter)) {
            System.out.println("Вы уже вводили эту букву: " + inputLetter);
            return;
        }
        usedLetters.add(inputLetter);
        processLetter(inputLetter);

    }

    private static void showUsedLetters() {
        System.out.print("Использованные буквы: ");
        usedLetters.forEach(letter -> System.out.print(letter + " "));
        System.out.println();
    }


    private static boolean isRussianLetter(String input) {
        Matcher matcher = PATTERN.matcher(input);
        return matcher.matches();
    }

    private static void processLetter(char letter) {
        if (wordToGuess.indexOf(letter) == -1) {
            wrongLettersCount++;
        } else {
            showLetter(letter);
        }
    }


    private static void showLetter(char letter) {
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == letter) {
                mask.setCharAt(i, letter);
            }
        }
    }


    private static void showState() {
        System.out.println(mask);
        System.out.printf("Кол-во ошибок: %d/%d%n", wrongLettersCount, MAX_MISTAKES);
        showUsedLetters();
        System.out.println(HANGMAN_STAGES[wrongLettersCount]);
    }


    private static void endGame() {
        String resultMessage = wrongLettersCount < MAX_MISTAKES ? "Победа!" : "Поражение!";
        System.out.printf("%s Загаданное слово было: %s%n%n", resultMessage, wordToGuess);
    }
}



