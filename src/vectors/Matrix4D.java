package vectors;

public class Matrix4D {
	public Point4D x;
	public Point4D y;
	public Point4D z;
	public Point4D w;

	public static final Matrix4D identity() {
		return new Matrix4D(new Point4D(1, 0, 0, 0), new Point4D(0, 1, 0, 0), new Point4D(0, 0, 1, 0),
				new Point4D(0, 0, 0, 1));
	}

	public Matrix4D(Point4D x, Point4D y, Point4D z, Point4D w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Matrix4D(Matrix3D a) {
		x = new Point4D(a.x.x, a.x.y, a.x.z, 0);
		y = new Point4D(a.y.x, a.y.y, a.y.z, 0);
		z = new Point4D(a.z.x, a.z.y, a.z.z, 0);
		w = new Point4D(0, 0, 0, 1);
	}

	public static Matrix4D translate(float x, float y, float z) {
		return new Matrix4D(new Point4D(1, 0, 0, 0), new Point4D(0, 1, 0, 0), new Point4D(0, 0, 1, 0),
				new Point4D(x, y, z, 1));
	}

	public Matrix4D transform(Matrix4D input) {
		return new Matrix4D(transform(input.x), transform(input.y), transform(input.z), transform(input.w));
	}

	public static Matrix4D screenTransform(float halfheight, float halfwidth) {
		return new Matrix4D(new Point4D(halfwidth, 0, 0, 0), new Point4D(0, -halfheight, 0, 0), new Point4D(0, 0, 1, 0),
				new Point4D(halfwidth, halfheight, 0, 1));
	}

	public Point4D transform(Point4D input) {
		Point4D xvec = x.scale(input.x);
		Point4D yvec = y.scale(input.y);
		Point4D zvec = z.scale(input.z);
		Point4D wvec = w.scale(input.w);
		return new Point4D(xvec.x + yvec.x + zvec.x + wvec.x, xvec.y + yvec.y + zvec.y + wvec.y,
				xvec.z + yvec.z + zvec.z + wvec.z, xvec.w + yvec.w + zvec.w + wvec.w);
	}

	public Matrix4D scale(float scale) {
		return new Matrix4D(x.scale(scale), y.scale(scale), z.scale(scale), w.scale(scale));
	}

	public static Matrix4D initPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		float tan = (float) Math.tan(fov / 2);
		float zRange = zNear - zFar;
		Matrix4D out = new Matrix4D(new Point4D(1.0f / (tan * aspectRatio), 0, 0, 0), 
									new Point4D(0, 1.0f / tan, 0, 0),
									new Point4D(0, 0, (-zNear - zFar) / zRange, 1), 
									new Point4D(0, 0, 2 * zFar * zNear / zRange, 0));

		return out;
	}

	public float determinant() {
		Matrix3D xmat = new Matrix3D(new Point3D(y.y, y.z, y.w), new Point3D(z.y, z.z, z.w), new Point3D(w.y, w.z, w.w)

		);

		Matrix3D ymat = new Matrix3D(new Point3D(x.y, x.z, x.w),

				new Point3D(z.y, z.z, z.w), new Point3D(w.y, w.z, w.w));

		Matrix3D zmat = new Matrix3D(new Point3D(x.y, x.z, x.w), new Point3D(y.y, y.z, y.w),

				new Point3D(w.y, w.z, w.w));

		Matrix3D wmat = new Matrix3D(new Point3D(x.y, x.z, x.w), new Point3D(y.y, y.z, y.w), new Point3D(z.y, z.z, z.w)

		);
		return xmat.determinant() * x.x - ymat.determinant() * y.x + zmat.determinant() * z.x
				- wmat.determinant() * w.x;
	}

	public Matrix4D inverse() {
		Matrix3D xx = new Matrix3D(new Point3D(y.y, y.z, y.w), new Point3D(z.y, z.z, z.w), new Point3D(w.y, w.z, w.w));
		Matrix3D xy = new Matrix3D(new Point3D(y.x, y.z, y.w), new Point3D(z.x, z.z, z.w), new Point3D(w.x, w.z, w.w));
		Matrix3D xz = new Matrix3D(new Point3D(y.x, y.y, y.w), new Point3D(z.x, z.y, z.w), new Point3D(w.x, w.y, w.w));
		Matrix3D xw = new Matrix3D(new Point3D(y.x, y.y, y.z), new Point3D(z.x, z.y, z.z), new Point3D(w.x, w.y, w.z));
		Matrix3D yx = new Matrix3D(new Point3D(x.y, x.z, x.w), new Point3D(z.y, z.z, z.w), new Point3D(w.y, w.z, w.w));
		Matrix3D yy = new Matrix3D(new Point3D(x.x, x.z, x.w), new Point3D(z.x, z.z, z.w), new Point3D(w.x, w.z, w.w));
		Matrix3D yz = new Matrix3D(new Point3D(x.x, x.y, x.w), new Point3D(z.x, z.y, z.w), new Point3D(w.x, w.y, w.w));
		Matrix3D yw = new Matrix3D(new Point3D(x.x, x.y, x.z), new Point3D(z.x, z.y, z.z), new Point3D(w.x, w.y, w.z));
		Matrix3D zx = new Matrix3D(new Point3D(x.y, x.z, x.w), new Point3D(y.y, y.z, y.w), new Point3D(w.y, w.z, w.w));
		Matrix3D zy = new Matrix3D(new Point3D(x.x, x.z, x.w), new Point3D(y.x, y.z, y.w), new Point3D(w.x, w.z, w.w));
		Matrix3D zz = new Matrix3D(new Point3D(x.x, x.y, x.w), new Point3D(y.x, y.y, y.w), new Point3D(w.x, w.y, w.w));
		Matrix3D zw = new Matrix3D(new Point3D(x.x, x.y, x.z), new Point3D(y.x, y.y, y.z), new Point3D(w.x, w.y, w.z));
		Matrix3D wx = new Matrix3D(new Point3D(x.y, x.z, x.w), new Point3D(y.y, y.z, y.w), new Point3D(z.y, z.z, z.w));
		Matrix3D wy = new Matrix3D(new Point3D(x.x, x.z, x.w), new Point3D(y.x, y.z, y.w), new Point3D(z.x, z.z, z.w));
		Matrix3D wz = new Matrix3D(new Point3D(x.x, x.y, x.w), new Point3D(y.x, y.y, y.w), new Point3D(z.x, z.y, z.w));
		Matrix3D ww = new Matrix3D(new Point3D(x.x, x.y, x.z), new Point3D(y.x, y.y, y.z), new Point3D(z.x, z.y, z.z));
		Matrix4D out = new Matrix4D(
				new Point4D(xx.determinant(), -yx.determinant(), zx.determinant(), -wx.determinant()),
				new Point4D(-xy.determinant(), yy.determinant(), -zy.determinant(), wy.determinant()),
				new Point4D(xz.determinant(), -yz.determinant(), zz.determinant(), -wz.determinant()),
				new Point4D(-xw.determinant(), yw.determinant(), -zw.determinant(), ww.determinant()));
		return out.scale(1 / determinant());
	}
}
