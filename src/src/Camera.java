package src;

import vectors.Matrix3D;
import vectors.Matrix4D;
import vectors.Point4D;

public class Camera {
	Point4D pos;
	Matrix4D transform = Matrix4D.identity();
	float yaw;
	float pitch;
	float roll;
	float fov = (float)(Math.PI*0.5);

	public Camera(Point4D pos) {
		this.pos = pos;
	}

	public void applyTransform() {
		if (pitch > Math.PI / 2) {
			pitch = (float) Math.PI / 2;
		}
		if (pitch < -Math.PI / 2) {
			pitch = (float) -Math.PI / 2;
		}
		Matrix3D rotation = Matrix3D.rotz(roll).transform(Matrix3D.rotx(pitch).transform(Matrix3D.roty(yaw)));
		transform = new Matrix4D(rotation);
	}

	public Mesh Transform(Mesh mesh) {
		Mesh out = new Mesh();
		for (Vertex vertex : mesh.vertices) {
			out.addVertex(Transform(vertex));
		}
			out.indices.addAll(mesh.indices);
		return out;
	}

	public Triangle Transform(Triangle triangle) {
		return new Triangle(Transform(triangle.a), Transform(triangle.b), Transform(triangle.c));
	}

	public Vertex Transform(Vertex vertex) {
		return new Vertex(vertex.pos.sub(pos), vertex.normal, vertex.texCoords,vertex.worldPos).transform(transform,
				Matrix4D.identity(),Matrix4D.identity());
	}

}
