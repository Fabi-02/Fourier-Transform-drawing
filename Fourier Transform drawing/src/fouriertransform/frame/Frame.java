package fouriertransform.frame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import fouriertransform.fourier.Fourier;
import fouriertransform.utils.Cycle;

/**
 * @author Fabian
 */

@SuppressWarnings("serial")
public class Frame extends JPanel implements ActionListener {

	JFrame frame;

	public Frame() {
		frame = new JFrame("Fourier Transform drawing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(1050, 650);
		frame.setVisible(true);
		frame.add(this);

		init();
	}

	Timer timer;

	public static double xVal = 0;
	public static ArrayList<Double> drawingX;
	public static ArrayList<Double> drawingY;
	public static ArrayList<Double> valuesX;
	public static ArrayList<Double> valuesY;
	public static ArrayList<Cycle> cyclesX;
	public static ArrayList<Cycle> cyclesY;
	public static double cycleYpos = -1;
	public static double cycleXpos = -1;

	private void init() {

		this.setLayout(null);

		drawingX = new ArrayList<>();
		drawingY = new ArrayList<>();
		valuesX = new ArrayList<>();
		valuesY = new ArrayList<>();
		cyclesX = new ArrayList<>();
		cyclesY = new ArrayList<>();

		this.setBackground(Color.BLACK);

		frame.addMouseMotionListener(new mouseListener());
		frame.addMouseListener(new mouseClick());

		timer = new Timer(1000 / 120, this);
		timer.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2D = (Graphics2D) g;

		g2D.setColor(Color.WHITE);
		g2D.setFont(new Font("Courier", Font.BOLD, 50));
		g2D.drawString("DRAW HERE", 680, 180);
		
		g2D.fillRect(200, 200, 400, 400);
		g2D.fillRect(620, 200, 400, 400);

		g2D.setStroke(new BasicStroke(3));
		if (cyclesX.size() > 0 && cyclesY.size() > 0) {
			cyclesX.get(0).drawCycle(g2D, xVal);
			cyclesY.get(0).drawCycle(g2D, xVal);
		}

		g2D.setColor(new Color(255, 0, 0));

		for (int i = 1; i < drawingX.size() && i < drawingY.size(); i++) {
			double posX = drawingX.get(i) * 200;
			double posY = drawingY.get(i) * 200;
			double lposX = drawingX.get(i - 1) * 200;
			double lposY = drawingY.get(i - 1) * 200;
			g2D.drawLine((int) lposX + 820, (int) lposY + 400, (int) posX + 820, (int) posY + 400);
		}
		if (1 < drawingX.size() && 1 < drawingY.size()) {
			double posX = drawingX.get(0) * 200;
			double posY = drawingY.get(0) * 200;
			double lposX = drawingX.get(drawingX.size() - 1) * 200;
			double lposY = drawingY.get(drawingY.size() - 1) * 200;
			g2D.drawLine((int) lposX + 820, (int) lposY + 400, (int) posX + 820, (int) posY + 400);
		}

		for (int i = 1; i < valuesX.size() && i < valuesY.size(); i++) {
			double x = valuesX.get(i);
			double lx = valuesX.get(i - 1);
			double y = valuesY.get(i);
			double ly = valuesY.get(i - 1);
			g2D.drawLine((int) lx, (int) ly, (int) x, (int) y);
		}
		if (cycleXpos != -1 && cycleYpos != -1 && valuesX.size() > 1 && valuesY.size() > 1) {
			g2D.setColor(new Color(255, 0, 0, 126));
			g2D.drawLine((int) cycleXpos, 100, (int) cycleXpos, (int) ((double) valuesY.get(valuesY.size() - 1)));
			g2D.drawLine(100, (int) cycleYpos, (int) ((double) valuesX.get(valuesY.size() - 1)), (int) cycleYpos);
			g2D.setColor(Color.BLACK);
			g2D.fillOval((int) ((double) valuesX.get(valuesY.size() - 1)) - 3, (int) cycleYpos - 3, 6, 6);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (cyclesX.size() > 0) {
			xVal += (Math.PI / (cyclesX.size() / 2));
			if (xVal >= Math.PI * 2) {
				xVal = 0;
			}
		} else {
			xVal = 0;
		}
		repaint();
	}

	public void updateCycleValue() {
		if (drawingX.size() % 2 != 0) {
			drawingX.remove(0);
		}
		if (drawingY.size() % 2 != 0) {
			drawingY.remove(0);
		}
		ArrayList<double[]> fourierX = Fourier.fourier(drawingX);
		fourierX.sort(new Comparator<double[]>() {
			@Override
			public int compare(double[] d2, double[] d1) {
				if (d1[1] > d2[1]) {
					return 1;
				}
				if (d1[1] < d2[1]) {
					return -1;
				}
				return 0;
			}
		});
		ArrayList<double[]> fourierY = Fourier.fourier(drawingY);
		fourierY.sort(new Comparator<double[]>() {
			@Override
			public int compare(double[] d2, double[] d1) {
				if (d1[1] > d2[1]) {
					return 1;
				}
				if (d1[1] < d2[1]) {
					return -1;
				}
				return 0;
			}
		});
		xVal = 0;
		cyclesX.clear();
		cyclesY.clear();
		valuesX.clear();
		valuesY.clear();
		for (int i = 0; i < fourierX.size(); i++) {
			double[] data = fourierX.get(i);
			if (cyclesX.isEmpty()) {
				cyclesX.add(new Cycle(400, 100, data[0], data[1], data[2], 0));
			} else {
				cyclesX.add(new Cycle(data[0], data[1], data[2], 0));
				cyclesX.get(i - 1).addCycle(cyclesX.get(i));
			}
		}
		for (int i = 0; i < fourierY.size(); i++) {
			double[] data = fourierY.get(i);
			if (cyclesY.isEmpty()) {
				cyclesY.add(new Cycle(100, 400, data[0], data[1], data[2], Math.PI / 2));
			} else {
				cyclesY.add(new Cycle(data[0], data[1], data[2], Math.PI / 2));
				cyclesY.get(i - 1).addCycle(cyclesY.get(i));
			}
		}
	}

	public static boolean update = false;
	
	private class mouseListener extends JComponent implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			if (x >= 620 + 3 && x <= 1020 + 3 && y >= 227 && y <= 627) {
				if (update == true) {
					drawingX.clear();
					drawingY.clear();
					update = false;
				}
				double posX = x - 823;
				double posY = y - 427;

				drawingX.add(posX / 200d);
				drawingY.add(posY / 200d);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}

	private class mouseClick extends JComponent implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			updateCycleValue();
			update = true;
		}

	}
}
