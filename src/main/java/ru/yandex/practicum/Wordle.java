package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

public class Wordle {

    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "wordle.log";

    public static void main(final String[] args) {

        try (
                PrintWriter logWriter = new PrintWriter(LOG_FILE);
                Scanner inputScanner = new Scanner(System.in)
        ) {

            runGame(inputScanner, logWriter);

        } catch (Exception exception) {

            exception.printStackTrace();

            try (PrintWriter errorLogWriter =
                         new PrintWriter(LOG_FILE)) {

                exception.printStackTrace(errorLogWriter);

            } catch (FileNotFoundException logException) {
                logException.printStackTrace();
            }
        }
    }

    private static void runGame(
            final Scanner inputScanner,
            final PrintWriter logWriter
    ) throws IOException {

        WordleDictionaryLoader dictionaryLoader =
                new WordleDictionaryLoader();

        WordleDictionary dictionary =
                dictionaryLoader.loadDictionary(DICTIONARY_FILE);

        logWriter.println(
                "Словарь загружен: "
                        + dictionary.getWordCount()
                        + " слов."
        );

        WordleGame game = new WordleGame(dictionary);

        logWriter.println("Игра началась.");

        while (!game.isGameOver()) {

            System.out.print("> ");

            String userInput = inputScanner.nextLine();

            if (userInput.trim().isEmpty()) {

                String suggestedWord = game.getHint();

                if (suggestedWord == null) {

                    System.out.println(
                            "Подходящих подсказок больше нет."
                    );

                } else {

                    System.out.println(
                            "Подсказка: " + suggestedWord
                    );

                    logWriter.println(
                            "Подсказка: " + suggestedWord
                    );
                }

                continue;
            }

            try {

                String hint = game.makeGuess(userInput);

                System.out.println(hint);

                logWriter.println(
                        "Слово: "
                                + userInput
                                + ", результат: "
                                + hint
                );

            } catch (IllegalArgumentException exception) {

                System.out.println(
                        exception.getMessage()
                );

                logWriter.println(
                        "Ошибка ввода: "
                                + exception.getMessage()
                );
            }
        }

        if (game.hasWon()) {

            System.out.println(
                    "Вы угадали слово!"
            );

            logWriter.println("Победа.");

        } else {

            System.out.println(
                    "Попытки закончились."
            );

            logWriter.println("Поражение.");
        }

        System.out.println(
                "# загаданное слово: "
                        + game.getAnswer()
        );

        logWriter.println(
                "Ответ: "
                        + game.getAnswer()
        );
    }
}