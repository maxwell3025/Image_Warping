package vectors;

public class Point3D {
	public float x;
	public float y;
	public float z;
	public static final Point3D Origin = new Point3D(0, 0, 0);

	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static float dotProduct(Point3D a, Point3D b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	public Point3D add(Point3D a) {
		return new Point3D(a.x + x, a.y + y, a.z + z);
	}

	public Point3D sub(Point3D a) {
		return new Point3D(x - a.x, y - a.y, z - a.z);
	}

	public Point3D scale(float scale) {
		return new Point3D(x * scale, y * scale, z * scale);
	}

	public float dist() {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}

	@Override
	public boolean equals(Object Obj) {
		if (Obj.getClass() == Point3D.class) {
			Point3D obj = (Point3D) Obj;
			return (x == obj.x) && (y == obj.y) && (z == obj.z);
		}
		return false;
	}

}
