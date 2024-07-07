import model.Game;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


public class GameRepository {

    private final Connection connection;


    public GameRepository(Connection connection) {
        this.connection = connection;
    }


    public Game insert(Game game) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommands.INSERT.get(), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, game.getName().toLowerCase());
            preparedStatement.setDate(2, game.getReleaseDate());
            preparedStatement.setBigDecimal(3, game.getRating());
            preparedStatement.setBigDecimal(4, game.getCost());
            preparedStatement.setString(5, game.getDescription());
            preparedStatement.setString(6, game.getType().toLowerCase());
            preparedStatement.setDate(7, Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();

            game.setId(preparedStatement.getGeneratedKeys().getInt(1));
            game.setCreationDate(Date.valueOf(LocalDate.now()));
            return game;

        } catch (SQLException e) {
            System.err.println("Insert went wrong");
            System.out.println(e.getMessage());
        }
        return game;
    }

    public Game get(int id) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommands.GET.get());
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Game.builder()
                    .name(resultSet.getString("title"))
                    .cost(resultSet.getBigDecimal("cost"))
                    .type(resultSet.getString("type"))
                    .rating(resultSet.getBigDecimal("rating"))
                    .description(resultSet.getString("description"))
                    .creationDate(resultSet.getDate("creation_date"))
                    .releaseDate(resultSet.getDate("release_date"))
                    .id(resultSet.getInt("id"))
                    .build();


        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Game deleteGame(int id) {
        try {
            Game deleted = get(id);
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommands.DELETE.get());
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return deleted;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return get(id);
    }

    public List<Game> getAll() {
        List<Game> games = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommands.GET_ALL.get());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Game game = Game.builder()
                        .name(resultSet.getString("title"))
                        .cost(resultSet.getBigDecimal("cost"))
                        .type(resultSet.getString("type"))
                        .rating(resultSet.getBigDecimal("rating"))
                        .description(resultSet.getString("description"))
                        .creationDate(resultSet.getDate("creation_date"))
                        .releaseDate(resultSet.getDate("release_date"))
                        .id(resultSet.getInt("id"))
                        .build();
                games.add(game);
            }
            return games;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return games;
    }

    public Optional<Game> find(String title) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommands.FIND_BY_NAME.get());
            preparedStatement.setString(1, "%" + title.toLowerCase() + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return Optional.of(Game.builder()
                    .name(resultSet.getString("title"))
                    .cost(resultSet.getBigDecimal("cost"))
                    .type(resultSet.getString("type"))
                    .rating(resultSet.getBigDecimal("rating"))
                    .description(resultSet.getString("description"))
                    .creationDate(resultSet.getDate("creation_date"))
                    .releaseDate(resultSet.getDate("release_date"))
                    .id(resultSet.getInt("id"))
                    .build());

        } catch (SQLException e) {
            System.out.println("Could not find the game with such name");
        }
        return Optional.empty();
    }

    public List<Game> filterByType(String type) {
        List<Game> games = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommands.FILTER_BY_TYPE.get());
            preparedStatement.setString(1, "%" + type.toLowerCase() + "%");
            return getGames(games, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public List<Game> filterByPrice(int lower, int upper) {
        List<Game> games = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommands.FILTER_BY_PRICE.get());
            preparedStatement.setInt(1, lower);
            preparedStatement.setInt(2, upper);
            return getGames(games, preparedStatement);
        } catch (SQLException e) {
            System.err.println("Games not found");
        }
        return games;
    }

    public List<Game> sortedByDate() {
        List<Game> games = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommands.SORT_BY_DATE.get());
            return getGames(games, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Game> getGames(List<Game> games, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            games.add(Game.builder()
                    .name(resultSet.getString("title"))
                    .cost(resultSet.getBigDecimal("cost"))
                    .type(resultSet.getString("type"))
                    .rating(resultSet.getBigDecimal("rating"))
                    .description(resultSet.getString("description"))
                    .creationDate(resultSet.getDate("creation_date"))
                    .releaseDate(resultSet.getDate("release_date"))
                    .id(resultSet.getInt("id"))
                    .build());

        }
        return games;
    }

    public Map<String, Integer> getTypes() {
        Map<String, Integer> types = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommands.GET_TYPES.get());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                types.put(resultSet.getString("type"), resultSet.getInt("count"));
            }
            return types;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

