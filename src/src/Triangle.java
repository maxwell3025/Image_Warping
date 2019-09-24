package src;

import src.Triangle;
import src.Vertex;
import vectors.Matrix4D;

public class Triangle {
	Vertex a;
	Vertex b;
	Vertex c;

	public Triangle(Vertex v1, Vertex v2, Vertex v3) {
		a = v1;
		b = v2;
		c = v3;
	}
	public Triangle transform(Matrix4D transform, Matrix4D normalTransform, Matrix4D worldPosTransform) {
		return new Triangle(a.transform(transform,normalTransform, worldPosTransform), b.transform(transform,normalTransform, worldPosTransform), c.transform(transform,normalTransform, worldPosTransform));
	}

}
