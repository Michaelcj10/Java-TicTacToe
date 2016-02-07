import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class Gui extends JFrame {
    JTextField[] myBoard;
    private JPanel gridPanel;
    private Controller control;
    final JLabel whatMove = new JLabel("What Move");
    private final JPanel topPanel = new JPanel();
    private final JPanel bottom = new JPanel();
    final JButton reset = new JButton("Reset");

    private Gui() {
        super("Tic Tac Toe");
        initialisePanels();
    }

    private void initialisePanels(){
        myBoard = new JTextField[9];
        gridPanel = new JPanel();
        control = new Controller(this);
        makeGrid();
        gridPanel.setLayout(new GridLayout(3, 3));
        topPanel.add(whatMove);
        whatMove.setForeground(Color.WHITE);
        bottom.add(reset);
        setSize(600, 600);
        setResizable(false);
        setVisible(true);
        whatMove.setText("Current Move Player 1");
        reset.setForeground(Color.black);
        reset.setBackground(Color.white);
        reset.setActionCommand("RESET_GAME");
    }

    private void makeGrid() {
        for (int i = 0; i < 9; i++) {

            myBoard[i] = new JTextField();
            myBoard[i].setActionCommand(control.actionCommands[i]);
            myBoard[i].setSize(150, 20);
            myBoard[i].setHorizontalAlignment(JTextField.CENTER);
            myBoard[i].setFont(new Font("SansSerif", Font.BOLD, 140));
            myBoard[i].setForeground(Color.WHITE);
            myBoard[i].addActionListener(control);
            gridPanel.add(myBoard[i]);
            myBoard[i].setBackground(Color.WHITE);
            myBoard[i].setOpaque(false);
        }

        setBounds(100, 100, 450, 300);
       JPanel contentPane = new JPanel() {
            public void paintComponent(Graphics g) {
               Image img = Toolkit.getDefaultToolkit().getImage(
                        Gui.class.getResource("blue.png"));
               g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);

            }
        };

        setContentPaneParams(contentPane);
        setPanelOpacity();
    }

    private void setPanelOpacity(){
        gridPanel.setOpaque(false);
        topPanel.setOpaque(false);
        bottom.setOpaque(false);
        topPanel.setVisible(true);
    }

    private void setContentPaneParams(JPanel contentPane){
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        contentPane.add(gridPanel);
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        new Gui();
    }
}
