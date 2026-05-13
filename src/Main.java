import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    static int numOfWrongLetters;
    static final int MAX_MISTAKES = 6;
    static StringBuilder state;
    static String wordToGuess;
    static String userInput;
    static Set<Character> usedLetters = new HashSet<>();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        startGame();
    }

    static void startGame() {
        askUser();
        while (!userInput.equalsIgnoreCase("N")) {
            System.out.println("Игра началась");
            getWord();
            playRound();
            askUser();
        }
        System.out.println("Вы вышли из игры");

    }

    static void askUser() {
        System.out.println(
                "Введите любой символ, кроме N или n, чтобы начать игру. " + "\n" +
                        "Введите N или n, чтобы завершить работу.");
        userInput = sc.next();
    }

    static void getWord() {
        List<String> words = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("dict"))
                )
        ).lines().toList();

        wordToGuess = words.get(new Random().nextInt(words.size())).toLowerCase();
    }

    static void playRound() {
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


    static void makeMove() {

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


    static boolean isInputOk(String input) {
        return input.length() == 1 && input.matches("^[А-Яа-яЁё]$");
    }

    static void checkLetter(char letter) {
        if (wordToGuess.indexOf(letter) == -1) {
            numOfWrongLetters++;
        } else {
            showLetter(letter);
        }
    }


    static void showLetter(char letter) {
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == letter) {
                state.setCharAt(i, letter);
            }
        }
    }


    static void printState() {
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


    static void endGame() {
        if (numOfWrongLetters < MAX_MISTAKES)
            System.out.println("Победа! Загаданное слово было: " + wordToGuess + "\n");
        else System.out.println("Поражение! Загаданное слово было: " + wordToGuess + "\n");
    }
}

