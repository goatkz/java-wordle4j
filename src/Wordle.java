import dictionary.WordleDictionary;
import dictionary.WordleDictionaryLoader;
import exceptions.DictionaryLoadException;
import exceptions.EmptyDictionaryException;
import exceptions.InvalidWordException;
import exceptions.WordNotFoundInDictionary;
import game.WordleGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    private static final String DICTIONARY_FILE = "words.txt";
    private static final String LOG_FILE = "wordle.log";

    public static void main(final String[] args) {

        try (PrintWriter log = new PrintWriter(LOG_FILE);
             Scanner scanner = new Scanner(System.in)) {

            runGame(scanner, log);

        } catch (Exception exception) {

            exception.printStackTrace();

            try (PrintWriter log = new PrintWriter(LOG_FILE)) {
                exception.printStackTrace(log);
            } catch (FileNotFoundException logException) {
                logException.printStackTrace();
            }
        }
    }

    private static void runGame(
            final Scanner scanner,
            final PrintWriter log
    ) throws Exception {

        WordleDictionaryLoader loader =
                new WordleDictionaryLoader();

        WordleDictionary dictionary =
                loader.loadDictionary(DICTIONARY_FILE);

        log.println(
                "Словарь загружен: "
                        + dictionary.getWordCount()
                        + " слов."
        );

        WordleGame game =
                new WordleGame(dictionary);

        log.println("Игра началась.");

        while (!game.isGameOver()) {

            System.out.print("> ");

            String input = scanner.nextLine();

            if (input.trim().isEmpty()) {

                String hint = game.getHint();

                if (hint == null) {
                    System.out.println(
                            "Подходящих подсказок больше нет."
                    );
                } else {
                    System.out.println(
                            "Подсказка: " + hint
                    );

                    log.println(
                            "Подсказка: " + hint
                    );
                }

                continue;
            }

            try {

                String hint =
                        game.makeGuess(input);

                System.out.println(hint);

                log.println(
                        "Слово: "
                                + input
                                + ", результат: "
                                + hint
                );

            } catch (InvalidWordException exception) {

                System.out.println(
                        exception.getMessage()
                );

                log.println(
                        "Ошибка ввода: "
                                + exception.getMessage()
                );

            } catch (WordNotFoundInDictionary exception) {

                System.out.println(
                        exception.getMessage()
                );

                log.println(
                        "Слово отсутствует в словаре: "
                                + input
                );
            }
        }

        if (game.hasWon()) {

            System.out.println(
                    "Вы угадали слово!"
            );

            log.println("Победа.");

        } else {

            System.out.println(
                    "Попытки закончились."
            );

            log.println("Поражение.");
        }

        System.out.println(
                "# загаданное слово: "
                        + game.getAnswer()
        );

        log.println(
                "Ответ: "
                        + game.getAnswer()
        );
    }
}