package vectors;

public class Matrix3D {
	public Point3D x;
	public Point3D y;
	public Point3D z;

	public static final Matrix3D identity() {
		return new Matrix3D(new Point3D(1, 0, 0), new Point3D(0, 1, 0), new Point3D(0, 0, 1));
	}

	public Matrix3D(Point3D x, Point3D y, Point3D z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Matrix3D transform(Matrix3D input) {
		return new Matrix3D(transform(input.x), transform(input.y), transform(input.z));
	}

	public Point3D transform(Point3D input) {
		Point3D xvec = x.scale(input.x);
		Point3D yvec = y.scale(input.y);
		Point3D zvec = z.scale(input.z);
		return new Point3D(xvec.x + yvec.x + zvec.x, xvec.y + yvec.y + zvec.y, xvec.z + yvec.z + zvec.z);
	}

	public Matrix3D scale(float scale) {
		return new Matrix3D(x.scale(scale), y.scale(scale), z.scale(scale));
	}

	public float determinant() {
		Matrix2D xmat = new Matrix2D(new Point2D(y.y, y.z), new Point2D(z.y, z.z));
		Matrix2D ymat = new Matrix2D(new Point2D(x.y, x.z), new Point2D(z.y, z.z));
		Matrix2D zmat = new Matrix2D(new Point2D(x.y, x.z), new Point2D(y.y, y.z));
		return xmat.determinant() * x.x - ymat.determinant() * y.x + zmat.determinant() * z.x;
	}

	public Matrix3D inverse() {
		Matrix2D xx = new Matrix2D(new Point2D(y.y, y.z), new Point2D(z.y, z.z));
		Matrix2D yx = new Matrix2D(new Point2D(x.y, x.z), new Point2D(z.y, z.z));
		Matrix2D zx = new Matrix2D(new Point2D(x.y, x.z), new Point2D(y.y, y.z));
		Matrix2D xy = new Matrix2D(new Point2D(y.x, y.z), new Point2D(z.x, z.z));
		Matrix2D yy = new Matrix2D(new Point2D(x.x, x.z), new Point2D(z.x, z.z));
		Matrix2D zy = new Matrix2D(new Point2D(x.x, x.z), new Point2D(y.x, y.z));
		Matrix2D xz = new Matrix2D(new Point2D(y.x, y.y), new Point2D(z.x, z.y));
		Matrix2D yz = new Matrix2D(new Point2D(x.x, x.y), new Point2D(z.x, z.y));
		Matrix2D zz = new Matrix2D(new Point2D(x.x, x.y), new Point2D(y.x, y.y));
		Matrix3D out = new Matrix3D(new Point3D(xx.determinant(), -yx.determinant(), zx.determinant()),
				new Point3D(-xy.determinant(), yy.determinant(), -zy.determinant()),
				new Point3D(xz.determinant(), -yz.determinant(), zz.determinant()));
		return out.scale(1 / determinant());
	}

	public static Matrix3D rotx(float radians) {
		return new Matrix3D(new Point3D(1, 0, 0), new Point3D(0, (float)Math.cos(radians), (float)-Math.sin(radians)),
				new Point3D(0, (float)Math.sin(radians), (float)Math.cos(radians)));
	}

	public static Matrix3D roty(float radians) {
		return new Matrix3D(new Point3D((float)Math.cos(radians), 0, (float)Math.sin(radians)), new Point3D(0, 1, 0),
				new Point3D((float)-Math.sin(radians), 0, (float)Math.cos(radians)));
	}

	public static Matrix3D rotz(float radians) {
		return new Matrix3D(new Point3D((float)Math.cos(radians), (float)-Math.sin(radians), 0),
				new Point3D((float)Math.sin(radians), (float)Math.cos(radians), 0), new Point3D(0, 0, 1));
	}

	public static Matrix3D arbrot(Point3D axis) {
		Matrix3D transformation = rotz((float)-Math.atan2(axis.y, axis.x));
		transformation = rotx((float)-Math.atan2(new Point2D(axis.x, axis.y).dist(), axis.z)).transform(transformation);
		Matrix3D output = rotz(axis.dist()).transform(transformation);
		output = transformation.inverse().transform(output);
		return output;
	}
}
