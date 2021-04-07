package uk.co.stikman.spiro.client;

public class FuncCircle implements Func {
	private float	frequency;
	private float	radius;

	public FuncCircle(float frequency, float radius) {
		this.frequency = frequency;
		this.radius = radius;
	}

	public float getFrequency() {
		return frequency;
	}

	public float getRadius() {
		return radius;
	}

	@Override
	public Vector2 exec(float theta, float phaseOffset, Vector2 res) {
		return res.set((float) Math.cos(theta * frequency + phaseOffset) * radius, (float) (Math.sin(theta * frequency + phaseOffset) * radius));
	}
}
