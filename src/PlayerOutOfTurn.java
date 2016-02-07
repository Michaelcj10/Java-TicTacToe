class PlayerOutOfTurn extends Exception {
    public PlayerOutOfTurn() {
        super("It's Not Your Turn!!!");
    }
}