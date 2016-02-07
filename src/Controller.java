import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

class Controller implements ActionListener {

    private final static int totalMovesAllowed = 9;
    private static String lastPicked = null;
    private static int picked = 0;
    private static boolean didAnyoneWin = false;
    final String[] actionCommands = {"SQUARE_ONE", "SQUARE_TWO", "SQUARE_THREE", "SQUARE_FOUR",
            "SQUARE_FIVE", "SQUARE_SIX", "SQUARE_SEVEN", "SQUARE_EIGHT", "SQUARE_NINE"};
    private final Gui myGame;
    private String test;

    public Controller(Gui givenGuiView) {
        this.myGame = givenGuiView;
        myGame.reset.addActionListener(this);
    }

    private static void showOptionPane(String exceptionName, String optionPaneMessage) {
        JOptionPane.showMessageDialog(null,
                "" + exceptionName,
                "" + optionPaneMessage,
                JOptionPane.ERROR_MESSAGE);
    }

    private static void setSquareDisabled(int position, Gui myGame) {
        myGame.myBoard[position].setEditable(false);
        myGame.myBoard[position].setEnabled(false);
    }

    private static void resetBoard(Gui myGame) {
        for (int t = 0; t < totalMovesAllowed; t++) {
            myGame.myBoard[t].setEnabled(true);
            myGame.myBoard[t].setEditable(true);
            myGame.whatMove.setText("Player One Move");
            picked = 0;
            myGame.myBoard[t].setText(null);
            didAnyoneWin = false;
            lastPicked = null;
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof JTextField) {

            try {
                //get the board position
                int boardPosition = Arrays.asList(actionCommands).indexOf(e.getActionCommand());
                //set variable test to board position text
                test = myGame.myBoard[boardPosition].getText().toUpperCase();
                //check if it is the users turn
                whichPlayerHasTurn();
                //check if the character is valid
                validCharachter();
                //if it is, last picked is now an X or an O
                lastPicked = test.toUpperCase();
                //Set that square disabled
                setSquareDisabled(boardPosition, myGame);
                //increment the total amount of squares picked
                picked++;
                //check if anyone won
                CheckWin();
                //set the text on that square to uppercase
                myGame.myBoard[boardPosition].setText(test.toUpperCase());
                //check for a stalemate
                draw();
                setTextForWhosTurnItIs();

                //catch all possible exceptions
            } catch (PlayerOutOfTurn e1) {
                showOptionPane(e1.toString(), "Not your turn");
            } catch (Stalemate e1) {
                showOptionPane(e1.toString(), "Draw");
            } catch (DisallowedCharachter e1) {
                showOptionPane(e1.toString(), "Wrong charachter");
            } catch (VictoryException e1) {
                showOptionPane(e1.toString(), "You Won!");
            }
        }

        if (e.getActionCommand().equals("RESET_GAME")) {
            resetBoard(myGame);
        }
    }

    private void setTextForWhosTurnItIs() {
        String player;
        if (picked % 2 == 0) {
            player = "Player 1";
        } else {
            player = "Player 2";
        }
        myGame.whatMove.setText("Current Move " + player);
    }

    private void draw() throws Stalemate {
        //if we have taken all 9 positions and there's no winner, it's stalemate
        if (picked == totalMovesAllowed && !didAnyoneWin) {
            myGame.whatMove.setText("Press Reset!");
            throw (new Stalemate());
        }
    }

    private void whichPlayerHasTurn() throws PlayerOutOfTurn {
        //if the text just entered is the same as last time, exception called
        if (test.equals(lastPicked)) {
            throw (new PlayerOutOfTurn());
        }
    }

    private void validCharachter() throws DisallowedCharachter {
        //if neither an X or O is entered, exception called
        if (!test.equals("X") && !test.equals("O")) {
            throw (new DisallowedCharachter());
        }
    }

    private boolean isMiddleSquareEmpty() {
        //if middle square is empty or not
        return myGame.myBoard[4].getText().length() <= 0;
    }

    private void CheckWin() throws VictoryException {

        //middle square is empty don't bother checking for diagonal win
        if (!isMiddleSquareEmpty()) {
            checkDiagonalTopRightToBottomLeft();
            checkDiagonalTopLeftToBottomRight();
        }

        checkHorozontalWin();
        checkVercticalWin();

        if (didAnyoneWin) {resetBoard(myGame);
          throw (new VictoryException());
        }
    }

    /*
    we can win if we have same text in 0,3,6 positions as well as
    1,4,7 and 2,5,9. I made a loop that increments in 1 and checks the text
    in the current field(0 or 1 or 2) and the file + 3 to it and +6.
    We only run a check if the first field is not empty,you can win otherwise.
     */
    private void checkVercticalWin() throws VictoryException {

        for (int x = 0; x < 3; x++) {
            if (myGame.myBoard[x].getText().toUpperCase().length() > 0) {

                if (myGame.myBoard[x].getText().toUpperCase().equals(myGame.myBoard[x + 3].getText().toUpperCase())
                        && myGame.myBoard[x].getText().toUpperCase().equals(myGame.myBoard[x + 6].getText().toUpperCase())) {
                    didAnyoneWin = true;
                }
            }

            if (didAnyoneWin) {
                throw (new VictoryException());
            }
        }
    }

    private void checkDiagonalTopRightToBottomLeft() throws VictoryException {

        String positionMiddleText = myGame.myBoard[4].getText().toUpperCase();
        String positionTopRightText = myGame.myBoard[2].getText().toUpperCase();
        String positionBottomLeftText = myGame.myBoard[6].getText().toUpperCase();

        didAnyoneWin = (positionTopRightText.equals(positionMiddleText) && positionMiddleText.equals(positionBottomLeftText));

        if (didAnyoneWin) {
            throw (new VictoryException());
        }
    }

    private void checkDiagonalTopLeftToBottomRight() throws VictoryException {

        String positionZeroText = myGame.myBoard[0].getText().toUpperCase();
        String positionMiddleText = myGame.myBoard[4].getText().toUpperCase();
        String positionLastText = myGame.myBoard[8].getText().toUpperCase();
        didAnyoneWin = (positionZeroText.equals(positionMiddleText) && positionZeroText.equals(positionLastText));

        if (didAnyoneWin) {
            throw (new VictoryException());
        }
    }

    /*
    we can win if we have same text in 0,1,2 positions as well as
    3,4,5 and 6,7,8. I made a loop that increments in 3 and checks the text
    in the current field(0 or 3 or 6) and the file + 1 to it and +2
     */
    private void checkHorozontalWin() throws VictoryException {

        for (int x = 0; x < 7; x += 3) {
            if (myGame.myBoard[x].getText().toUpperCase().length() > 0) {
                didAnyoneWin = (myGame.myBoard[x].getText().toUpperCase().
                        equals(myGame.myBoard[x + 1].getText()) && myGame.myBoard[x].getText()
                        .toUpperCase().equals(myGame.myBoard[x + 2].getText().toUpperCase()));
            }

            if (didAnyoneWin) {
                throw (new VictoryException());
            }
        }
    }
}
