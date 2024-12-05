import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// GameMain class
public class GameMain extends JPanel implements MouseListener {
    public static final int ROWS = 3; // number of rows in the grid
    public static final int COLS = 3; // number of columns in the grid
    public static final String TITLE = "Tic Tac Toe"; // window title

    public static final int CELL_SIZE = 100; // size of each cell in pixels
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS; // total canvas width
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS; // total canvas height
    public static final int CELL_PADDING = CELL_SIZE / 6; // padding inside each cell for symbols
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // symbol size (X or O)
    public static final int SYMBOL_STROKE_WIDTH = 8; // thickness of symbol lines

    private Board board; // game board to track cell contents
    private GameState currentState; // current state of the game
    private Player currentPlayer; // current player (X or O)
    private JLabel statusBar; // label to display game status messages

    public GameMain() {
        this.addMouseListener(this);

        statusBar = new JLabel("         ");
        statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.LIGHT_GRAY);

        // sets layout and add status bar to the bottom of the panel
        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));

        board = new Board(); // initialises the game board

        initGame(); // starts a new game
    }
// main method to start the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE); // creates the game window
            GameMain gamePanel = new GameMain(); // creates the game panel
            frame.add(gamePanel); // add the panel to the frame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes the program on exit
            frame.pack(); // adjusts the frame size
            frame.setLocationRelativeTo(null); // centre the window
            frame.setVisible(true); // displays the window
        });
    }
    // paints the game components on the screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE); // set background colour
        board.paint(g); // draws the game board

        // updates the status bar based on the current game state
        if (currentState == GameState.Playing) {
            statusBar.setForeground(Color.BLACK);
            if (currentPlayer == Player.Cross) {
                statusBar.setText("X's Turn");
            } else {
                statusBar.setText("O's Turn");
            }
        } else if (currentState == GameState.Draw) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == GameState.Cross_won) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again.");
        } else if (currentState == GameState.Nought_won) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again.");
        }
    }
    // initialises or resets the game
    public void initGame() {
        // clears all cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board.cells[row][col].content = Player.Empty;
            }
        }
        currentState = GameState.Playing; // sets the game state
        currentPlayer = Player.Cross; // starts with X
    }
    // the following code updates the game state after a move
    public void updateGame(Player thePlayer, int row, int col) {
        if (board.hasWon(thePlayer, row, col)) {
            if (thePlayer == Player.Cross) {
                currentState = GameState.Cross_won;
            } else {
                currentState = GameState.Nought_won;
            }
        } else if (board.isDraw()) {
            currentState = GameState.Draw;
        }
    }
    // the following code handles the mouse click events
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        int rowSelected = mouseY / CELL_SIZE;
        int colSelected = mouseX / CELL_SIZE;

        if (currentState == GameState.Playing) {
            if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS
                    && board.cells[rowSelected][colSelected].content == Player.Empty) {
                board.cells[rowSelected][colSelected].content = currentPlayer;
                updateGame(currentPlayer, rowSelected, colSelected);

                if (currentPlayer == Player.Cross) {
                    currentPlayer = Player.Nought;
                } else {
                    currentPlayer = Player.Cross;
                }
            }
        } else {
            initGame(); // restarts the game if it is over
        }
        repaint(); // repaints the board
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}

// Define the GameState enum
enum GameState {
    Playing, Draw, Cross_won, Nought_won
}

// Define the Player enum
enum Player {
    Empty, Cross, Nought
}

// Define the Board class
class Board {
    public Cell[][] cells;

    public Board() {
        cells = new Cell[GameMain.ROWS][GameMain.COLS]; // initialises the board
        for (int row = 0; row < GameMain.ROWS; ++row) {
            for (int col = 0; col < GameMain.COLS; ++col) {
                cells[row][col] = new Cell(row, col); // creates each cell
            }
        }
    }
    // paints the board
    public void paint(Graphics g) {
        for (int row = 0; row < GameMain.ROWS; ++row) {
            for (int col = 0; col < GameMain.COLS; ++col) {
                cells[row][col].paint(g); // paints each cell
            }
        }
    }
    // checks if a player has won
    public boolean hasWon(Player player, int row, int col) {
        // Check row
        if (cells[row][0].content == player && cells[row][1].content == player && cells[row][2].content == player)
            return true;
        // Check column
        if (cells[0][col].content == player && cells[1][col].content == player && cells[2][col].content == player)
            return true;
        // Check diagonals
        if (cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player)
            return true;
        if (cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player)
            return true;

        return false;
    }
    // the code below checks if the game is a draw
    public boolean isDraw() {
        for (int row = 0; row < GameMain.ROWS; ++row) {
            for (int col = 0; col < GameMain.COLS; ++col) {
                if (cells[row][col].content == Player.Empty) {
                    return false; // empty cell has been found, not a draw
                }
            }
        }
        return true; // no empty cells, its a draw
    }
}


// Define the Cell class
class Cell {
    public Player content; // content of the cell (X, O or empty)
    private int row, col; // row and column of the cell

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Player.Empty; // initialises as empty
    }
    //The following code paints the cell content (X or O)
    public void paint(Graphics g) {
        int x1 = col * GameMain.CELL_SIZE + GameMain.CELL_PADDING;
        int y1 = row * GameMain.CELL_SIZE + GameMain.CELL_PADDING;
        if (content == Player.Cross) {
            g.setColor(Color.RED);
            g.drawLine(x1, y1, x1 + GameMain.SYMBOL_SIZE, y1 + GameMain.SYMBOL_SIZE);
            g.drawLine(x1, y1 + GameMain.SYMBOL_SIZE, x1 + GameMain.SYMBOL_SIZE, y1);
        } else if (content == Player.Nought) {
            g.setColor(Color.BLUE);
            g.drawOval(x1, y1, GameMain.SYMBOL_SIZE, GameMain.SYMBOL_SIZE);
        }
    }
}
