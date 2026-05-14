import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    private static int numOfWrongLetters;
    private static final int MAX_MISTAKES = 6;
    private static StringBuilder state;
    private static String wordToGuess;
    private static String userInput;
    private static final Set<Character> usedLetters = new HashSet<>();

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        startGame();
    }

    private static void startGame() {
        askUser();
        while (!userInput.equalsIgnoreCase("N")) {
            System.out.println("Игра началась");
            getWord();
            playRound();
            askUser();
        }
        System.out.println("Вы вышли из игры");

    }

    private static void askUser() {
        System.out.println(
                "Введите любой символ, кроме N или n, чтобы начать игру. " + "\n" +
                        "Введите N или n, чтобы завершить работу.");
        userInput = sc.next();
    }

    private static void getWord() {
        List<String> words = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("dict"))
                )
        ).lines().toList();

        wordToGuess = words.get(new Random().nextInt(words.size())).toLowerCase();
    }

    private static void playRound() {
        numOfWrongLetters = 0;
        usedLetters.clear();
        state = new StringBuilder("*".repeat(wordToGuess.length()));
        while ((numOfWrongLetters < MAX_MISTAKES) && (state.indexOf("*") != -1)) {
            printState();
            makeMove();
        }
        printState();
        endGame();
    }


    private static void makeMove() {

        String input = sc.next();

        if (!isInputOk(input)) {
            System.out.println("Введите 1 символ кириллицы");
            return;
        }

        char inputLetter = Character.toLowerCase(input.charAt(0));
        if (usedLetters.contains(inputLetter)) {
            System.out.println("Вы уже вводили эту букву: " + inputLetter);
            return;
        }
        usedLetters.add(inputLetter);

        checkLetter(inputLetter);

    }


    private static boolean isInputOk(String input) {
        return input.length() == 1 && input.matches("^[А-Яа-яЁё]$");
    }

    private static void checkLetter(char letter) {
        if (wordToGuess.indexOf(letter) == -1) {
            numOfWrongLetters++;
        } else {
            showLetter(letter);
        }
    }


    private static void showLetter(char letter) {
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == letter) {
                state.setCharAt(i, letter);
            }
        }
    }


    private static void printState() {
        System.out.println(state + " Кол-во ошибок: " + numOfWrongLetters + "/" + MAX_MISTAKES);
        switch (numOfWrongLetters) {
            case 0 -> System.out.println(
                    """
                            +---+
                             |   |
                                 |
                                 |
                                 |
                                 |
                            =========""" + "\n");


            case 1 -> System.out.println("""
                     +---+
                     |   |
                     O   |
                         |
                         |
                         |
                    =========""");

            case 2 -> System.out.println("""
                     +---+
                     |   |
                     O   |
                     |   |
                         |
                         |
                    =========""");

            case 3 -> System.out.println("""
                     +---+
                     |   |
                     O   |
                    /|   |
                         |
                         |
                    =========""");
            case 4 -> System.out.println("""
                     +---+
                     |   |
                     O   |
                    /|\\  |
                         |
                         |
                    =========""");
            case 5 -> System.out.println("""
                     +---+
                     |   |
                     O   |
                    /|\\  |
                    /    |
                         |
                    =========""");
            case 6 -> System.out.println("""
                     +---+
                     |   |
                     O   |
                    /|\\  |
                    / \\  |
                         |
                    =========""" + "\n");
        }
    }


    private static void endGame() {
        if (numOfWrongLetters < MAX_MISTAKES)
            System.out.println("Победа! Загаданное слово было: " + wordToGuess + "\n");
        else System.out.println("Поражение! Загаданное слово было: " + wordToGuess + "\n");
    }
}

