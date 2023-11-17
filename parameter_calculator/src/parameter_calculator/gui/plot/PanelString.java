package parameter_calculator.gui.plot;

import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelString extends JPanel {
	private String s;
	private final int x;
	private final int y;
	/*
	 * 0 : 左寄せ
	 * 1 : 右寄せ
	 * 2 : 中央揃え
	 */
	private final int type;
	
	public PanelString(String value, int x,int y,int type) {
		this.s = value;
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public void setString(String value) {
		this.s = value;
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int true_x;
        if(type == 2) {
        	true_x = x - g.getFontMetrics().stringWidth(s) / 2;
        }
        else if(type == 1) {
        	true_x = x - g.getFontMetrics().stringWidth(s);
        }
        else {
        	true_x = x;
        }
        g.drawChars(s.toCharArray(), 0, s.length(), true_x, y);
    }
}

