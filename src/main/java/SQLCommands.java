public enum SQLCommands {
    INSERT("""
                        INSERT INTO games(title, release_date, rating, cost, description, type, creation_date)
                        VALUES (?, ?, ?, ?, ?, ?, ?);
            """),
    DELETE("""
            DELETE FROM games WHERE id = ?;
            """),
    FIND_BY_NAME("""
                SELECT * FROM games WHERE title LIKE ?;
            """),
    FILTER_BY_PRICE("""
                SELECT * FROM games WHERE cost BETWEEN ? and ?;
            """),
    FILTER_BY_TYPE("""
            SELECT * FROM games WHERE type LIKE ?
            """),
    SORT_BY_DATE("""
            SELECT * FROM games ORDER BY creation_date ASC;
            """),
    GET_ALL("""
            SELECT * FROM games;
            """),
    GET("""
            SELECT * FROM games WHERE id = ?
            """),
    GET_TYPES("""
            SELECT type, COUNT(type) FROM games GROUP BY type
            """);

    private final String command;

    SQLCommands(String command) {
        this.command = command;
    }

    public String get() {
        return command;
    }
}
