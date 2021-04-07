package uk.co.stikman.spiro.client;

import java.util.ArrayList;
import java.util.List;

public class Figure {
	private final String	name;
	private float			phaseOffset	= 0.0f;
	private float			width		= 1.0f;
	private String			colour;
	private List<Func>		functions	= new ArrayList<>();
	private Vector2			tmpv		= new Vector2();

	public Figure(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getColour() {
		return colour;
	}

	public List<Func> getFunctions() {
		return functions;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public Vector2 eval(float theta, Vector2 out) {
		for (Func f : functions)
			out.addLocal(f.exec(theta, phaseOffset * Spiro.PI2, tmpv));
		return out;
	}

	public float getPhaseOffset() {
		return phaseOffset;
	}

	public void setPhaseOffset(float phaseOffset) {
		this.phaseOffset = phaseOffset;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
}
