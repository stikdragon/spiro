package uk.co.stikman.spiro.client;

import java.util.ArrayList;
import java.util.List;

public class SpiroSet {
	private List<Figure> figures = new ArrayList<>();

	public void parse(String text) {
		int counter = 0;
		Figure curfig = null;
		for (String bit : text.split("\r?\n")) {
			try {
				String line = bit.trim().toLowerCase();
				++counter;
				if (line.isEmpty())
					continue;

				if (curfig == null) {
					if (line.startsWith("figure ")) {
						curfig = new Figure(line.substring(7));
					} else
						throw new RuntimeException("expected 'figure <name>'");
				} else {
					if (line.startsWith("colour "))
						curfig.setColour(line.substring(7));
					else if (line.startsWith("phase")) 
						curfig.setPhaseOffset(Float.parseFloat(line.substring(6)));
					else if (line.startsWith("circle"))
						parseCircle(line, curfig);
					else if (line.startsWith("width"))
						curfig.setWidth(Float.parseFloat(line.substring(6)));
					else if (line.equals("end")) {
						figures.add(curfig);
						curfig = null;
					} else
						throw new RuntimeException("expected 'colour', 'width', 'phase', 'circle', or 'end'");
				}
			} catch (Exception e) {
				throw new RuntimeException("Parse error on line " + counter + ": " + e.getMessage(), e);
			}
		}
	}

	private void parseCircle(String line, Figure curfig) {
		if (!line.matches("^circle\\(-?[0-9.]+ *, *[0-9.]+ *\\)$"))
			throw new RuntimeException("'circle' should be like 'circle(frequency,radius)'");
		line = line.substring(7, line.length() - 1);
		String[] bits = line.split(",");
		FuncCircle c = new FuncCircle(Float.parseFloat(bits[0].trim()), Float.parseFloat(bits[1].trim()));
		curfig.getFunctions().add(c);
	}

	public List<Figure> getFigures() {
		return figures;
	}

}
