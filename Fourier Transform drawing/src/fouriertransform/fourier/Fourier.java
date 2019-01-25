package fouriertransform.fourier;

import java.util.ArrayList;

/**
 * @author Fabian
 */

public class Fourier {

	public static ArrayList<double[]> fourier(ArrayList<Double> data) {
		int N = data.size();
		ArrayList<double[]> fourier = new ArrayList<double[]>();

		for (int k = 0; k < N; k++) {
			double re = 0;
			double im = 0;
			for (int n = 0; n < N; n++) {
				re += data.get(n) * Math.cos((2 * Math.PI * k * n) / N);
				im -= data.get(n) * Math.sin((2 * Math.PI * k * n) / N);
			}
			re = re / N;
			im = im / N;
			fourier.add(new double[] {k, Math.sqrt(re * re + im * im), Math.atan2(im, re)});
		}
		
		return fourier;
	}
}
