public enum Ask {
    ACTION("""
            What would you like to do?
            1) Add game.
            2) Delete game.
            3) Search by name.
            4) Filter by price.
            5) Filter by type.
            6) Sorted by creation date.
            7) Show all games.
            Choose option with typing number 1-7
            To see this menu again type "menu"
            To quit type exit"""),
    LOWER("Enter lower price boarder"),
    UPPER("Enter upper price boarder"),
    TITLE("What is a title of game?"),
    RATING("What is a rating of game? (FORMAT: *.*) (optional)"),
    COST("What is a cost of game? (FORMAT: ***.**)"),
    PUBLISH_DATE("What is published date? (FORMAT yyyy-mm-dd)"),
    DESCRIPTION("What is a description of game? (optional)"),
    TYPE_FILTER("What is a type?"),
    TYPE("What is a type of game?");


    private final String question;

    Ask(String question) {
        this.question = question;
    }

    public String get() {
        return question;
    }
}
