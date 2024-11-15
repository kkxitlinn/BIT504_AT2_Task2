import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Cell {
    // Content of this cell (empty, cross, or nought)
    Player content;
    // Row and column of this cell
    int row, col;
    
    /** Constructor to initialize this cell with the specified row and column */
    public Cell(int row, int col) {
        // Initialize the row and column for this cell
        this.row = row;
        this.col = col;
        
        // Set the cell content to empty at the start
        clear();
    }
    
    /** Paints itself on the graphics canvas, given the Graphics context g */
    public void paint(Graphics g) {
        // Graphics2D allows setting of pen's stroke size
        Graphics2D graphic2D = (Graphics2D) g;
        graphic2D.setStroke(new BasicStroke(GameMain.SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Calculate the position to draw the symbol
        int x1 = col * GameMain.CELL_SIZE + GameMain.CELL_PADDING;
        int y1 = row * GameMain.CELL_SIZE + GameMain.CELL_PADDING;
        
        if (content == Player.Cross) {
            // Draw X symbol in red
            graphic2D.setColor(Color.RED);
            int x2 = (col + 1) * GameMain.CELL_SIZE - GameMain.CELL_PADDING;
            int y2 = (row + 1) * GameMain.CELL_SIZE - GameMain.CELL_PADDING;
            graphic2D.drawLine(x1, y1, x2, y2);
            graphic2D.drawLine(x2, y1, x1, y2);
        } else if (content == Player.Nought) {
            // Draw O symbol in blue
            graphic2D.setColor(Color.BLUE);
            graphic2D.drawOval(x1, y1, GameMain.SYMBOL_SIZE, GameMain.SYMBOL_SIZE);
        }
    }
    
    /** Sets this cell's content to EMPTY */
    public void clear() {
        content = Player.Empty; // Set the content to empty (no symbol)
    }
}

