package vectors;

public class Matrix2D {
	public Point2D x;
	public Point2D y;

	public static final Matrix2D identity() {
		return new Matrix2D(new Point2D(1, 0), new Point2D(0, 1));
	}

	public Matrix2D(Point2D x, Point2D y) {
		this.x = x;
		this.y = y;
	}

	public Matrix2D transform(Matrix2D input) {
		return new Matrix2D(transform(input.x), transform(input.y));
	}

	public Point2D transform(Point2D input) {
		Point2D xvec = x.scale(input.x);
		Point2D yvec = y.scale(input.y);
		return new Point2D(xvec.x + yvec.x, xvec.y + yvec.y);
	}

	public Matrix2D scale(float scale) {
		return new Matrix2D(x.scale(scale), y.scale(scale));
	}

	public float determinant() {
		return x.x * y.y - x.y * y.x;
	}

	public Matrix2D inverse() {
		float invdet = 1 / determinant();
		return new Matrix2D(new Point2D(x.x * invdet, -x.y * invdet), new Point2D(-y.x * invdet, y.y * invdet));
	}

	public static Matrix2D roation(float radians) {
		return new Matrix2D(new Point2D((float)Math.cos(radians), (float)-Math.sin(radians)),
				new Point2D((float)Math.sin(radians), (float)Math.cos(radians)));
	}

}
