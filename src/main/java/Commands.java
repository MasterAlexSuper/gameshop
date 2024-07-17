
import model.Game;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Commands {

    private final Scanner scanner;
    private final GameRepository gameRepository;

    public Runnable add() {
        String success = "Game was successfully added";
        // If the value must be in the table or can be empty
        boolean required = true;
        boolean optional = false;
        String wrong = "Something went wrong";
        return () -> {
            try {
                Game game = Game.builder()
                        .name(getValue(Ask.TITLE.get(), required))
                        .type(getValue(Ask.TYPE.get(), required))
                        .cost(BigDecimal.valueOf(Double.parseDouble(getValue(Ask.COST.get(), required))))
                        .releaseDate(Date.valueOf(getValue(Ask.PUBLISH_DATE.get(), required)))
                        .rating(BigDecimal.valueOf(Double.parseDouble(getValue(Ask.RATING.get(), optional))))
                        .description(getValue(Ask.DESCRIPTION.get(), optional))
                        .build();
                System.out.println(gameRepository.insert(game));

                System.out.println(success);
            } catch (Exception e) {
                System.err.println(wrong);
            }
        };
    }

    private String getValue(String toPrint, boolean strict) {
        String value;
        if (strict) {
            do {
                System.out.println(toPrint);
                value = scanner.nextLine().trim();
            } while (value.isEmpty());
        } else {
            System.out.println(toPrint);
            value = scanner.nextLine().trim();
        }
        return value;
    }

    public Runnable delete() {
        String success = "Game was successfully deleted";
        String canceled = "Operation was canceled";
        String notFound = "Failed to find game with this name";
        String confirm = "Are you sure you want to delete this game?";
        return () -> {
            System.out.println(Ask.TITLE.get());
            String line = scanner.nextLine();
            Game game;
            if (gameRepository.find(line).isPresent()) {
                game = gameRepository.find(line).get();
                System.out.println(confirm);
                System.out.println(game.getName());
                switch (scanner.nextLine().toLowerCase()) {
                    case "yes", "y":
                        System.out.println(gameRepository.deleteGame(game.getId()));

                        System.out.println(success);
                        break;
                    case "no", "n":
                        System.out.println(canceled);
                        break;
                }
            } else {
                System.out.println(notFound);
            }
        };
    }

    public Runnable find() {

        return () -> {
            System.out.println(Ask.TITLE.get());
            String title = scanner.nextLine()
                    .trim()
                    .toLowerCase();
            if (gameRepository.find(title).isPresent()) {
                System.out.println(gameRepository.find(title).get());
            }
        };
    }

    public Runnable filterByPrice() {
        String warning = "You must enter a number";
        return () -> {
            boolean finished = false;
            while (!finished) {
                try {
                    System.out.println(Ask.LOWER.get());
                    int lower = Integer.parseInt(scanner.nextLine());
                    System.out.println(Ask.UPPER.get());
                    int upper = Integer.parseInt(scanner.nextLine());
                    gameRepository.filterByPrice(lower, upper).forEach(System.out::println);
                    finished = true;

                } catch (NumberFormatException e) {
                    System.err.println(warning);
                }
            }

        };
    }

    public Runnable filterByType() {
        String wrong = "Check spelling!";
        return () -> {
            boolean finished = false;
            while (!finished) {
                System.out.println(Ask.TYPE_FILTER.get());
                System.out.println(gameRepository.getTypes());
                List<Game> games = gameRepository.filterByType(scanner.nextLine().trim());
                if (!games.isEmpty()) {
                    games.forEach(System.out::println);
                    finished = true;
                } else {
                    System.out.println(wrong);
                }
            }

        };
    }

    public Runnable getAllByDate() {
        return () -> gameRepository.sortedByDate().forEach(System.out::println);
    }

    public Runnable getAll() {
        return () -> gameRepository.getAll().forEach(System.out::println);

    }


    public Commands(Scanner scanner, GameRepository gameRepository) {
        this.scanner = scanner;
        this.gameRepository = gameRepository;
    }
}
