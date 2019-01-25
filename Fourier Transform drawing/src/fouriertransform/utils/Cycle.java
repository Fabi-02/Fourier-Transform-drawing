package fouriertransform.utils;

import java.awt.Color;
import java.awt.Graphics2D;

import fouriertransform.frame.Frame;

/**
 * @author Fabian
 */

public class Cycle {

	public double offsetX;
	public double offsetY;

	public double freq;
	public double amp;
	public double phase;

	public double rotation;

	public Cycle child;

	public Cycle(double offsetX, double offsetY, double freq, double amp, double phase, double rotation) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;

		this.freq = freq;
		this.amp = amp;
		this.phase = phase;

		this.rotation = rotation;
	}

	public Cycle(double freq, double amp, double phase, double rotation) {
		this.freq = freq;
		this.amp = amp;
		this.phase = phase;

		this.rotation = rotation;
	}

	public void addCycle(Cycle cycle) {
		this.child = cycle;
	}

	public void drawCycle(Graphics2D g2D, double xVal) {
		g2D.setColor(Color.WHITE);
		double radius = amp * 200; // 100 = height
		g2D.setColor(new Color(255, 255, 255, 100));
		g2D.drawOval((int) offsetX - (int) radius, (int) offsetY - (int) radius, (int) radius * 2, (int) radius * 2);
		double angle = (xVal * freq) + phase + rotation;
		double x = radius * Math.cos(angle);
		double y = radius * Math.sin(angle);
		g2D.setColor(Color.WHITE);
		g2D.drawLine((int) offsetX, (int) offsetY, (int) (x + offsetX), (int) (y + offsetY));

		if (this.child == null) {
//			g2D.setColor(new Color(255, 0, 0, 126));
//			g2D.drawLine((int) (x + offsetX), (int) (y + offsetY), Frame.beginGraphX, (int) (y + offsetY));
			if (rotation % Math.PI == 0) {
				Frame.valuesX.add(x + offsetX);
				Frame.cycleXpos = x + offsetX;
				if (Frame.valuesX.size() > Frame.cyclesX.size()) {
					Frame.valuesX.remove(0);
				}
			} else {
				Frame.valuesY.add(y + offsetY);
				Frame.cycleYpos = x + offsetY;
				if (Frame.valuesY.size() > Frame.cyclesY.size()) {
					Frame.valuesY.remove(0);
				}
			}
		} else {
			child.offsetX = x + offsetX;
			child.offsetY = y + offsetY;
			child.drawCycle(g2D, xVal);
		}
	}
}
