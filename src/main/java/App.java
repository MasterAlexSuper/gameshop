


import java.sql.SQLException;

import java.util.Map;
import java.util.Scanner;

public class App {

    private static Commands commands;

    public static void start() {
        String fail = "Database connection failed";
        GameRepository repository;
        String unknown = "Unknown command";

        try {
            repository = new GameRepository(DbConnection.getConnection());
        } catch (SQLException e) {
            System.err.println(fail);
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);
        commands = new Commands(scanner, repository);
        Map<String, Runnable> orchestrator = init();
        String command;
        System.out.println(Ask.ACTION.get());
        do {
            command = scanner.nextLine();
            orchestrator
                    .getOrDefault(command, () -> System.out.println(unknown))
                    .run();
        } while (!command.equals("exit"));
    }

    private static Map<String, Runnable> init() {
        return Map.of(
                "1", commands.add(),
                "2", commands.delete(),
                "3", commands.find(),
                "4", commands.filterByPrice(),
                "5", commands.filterByType(),
                "6", commands.getAllByDate(),
                "7", commands.getAll(),
                "exit", () -> {
                },
                "menu", () -> System.out.println(Ask.ACTION.get())
        );
    }

}

