package src;

import vectors.Point4D;

public class PointSource extends LightSource{
Point4D point;
float intensity;
	public PointSource(Point4D pos, float brightness) {
		point=pos;
		intensity = brightness;
	}

	@Override
	public Point4D light(Point4D pos) {
		Point4D dif = pos.sub(point);
		float dist = dif.dist();
		return dif.scale(1.0f/(dist*dist*dist)*intensity);
	}

}
