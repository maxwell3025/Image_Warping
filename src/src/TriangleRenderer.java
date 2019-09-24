package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import vectors.Matrix4D;
import vectors.Point3D;
import vectors.Point4D;

public class TriangleRenderer extends BitMap {
	private Matrix4D screenTransform;

	private Matrix4D perspectiveTransform;
	private float[] zBuffer;
	private int background = 0xff000000;
	private float fov = (float) Math.PI*0.5f;
	private PointSource lightsource = new PointSource(new Point4D(0,10,0),1f);
	public TriangleRenderer(int width, int height) {
		super(width, height);
		screenTransform = Matrix4D.screenTransform(height / 2, width / 2);
		zBuffer = new float[width * height];
		Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);
	}

	public void reset() {
		Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);
		fill(background);
	}

	public void drawMesh(Mesh mesh, BitMap tex, Point4D Lightpos, float intensity) {
		lightsource.point=Lightpos;
		lightsource.intensity=intensity;
		Mesh toDraw = mesh.Transform(perspectiveTransform, Matrix4D.identity(),Matrix4D.identity());
		for (int i = 0;i<toDraw.getLength();i++) {
			Triangle triangle = toDraw.getTriangle(i);
			DrawTriangle(triangle.a, triangle.b, triangle.c, tex);
		}
	}

	public void DrawTriangle(Vertex v1, Vertex v2, Vertex v3, BitMap texture) {
		if (v1.IsInsideViewFrustum() && v2.IsInsideViewFrustum() && v3.IsInsideViewFrustum()) {
			FillTriangle(v1, v2, v3, texture);
			return;
		}

		List<Vertex> vertices = new ArrayList<>();
		List<Vertex> auxillaryList = new ArrayList<>();

		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);

		if (ClipPolygonAxis(vertices, auxillaryList, 0) && ClipPolygonAxis(vertices, auxillaryList, 1)
				&& ClipPolygonAxis(vertices, auxillaryList, 2)) {
			Vertex initialVertex = vertices.get(0);

			for (int i = 1; i < vertices.size() - 1; i++) {
				FillTriangle(initialVertex, vertices.get(i), vertices.get(i + 1), texture);
			}
		}
	}

	private boolean ClipPolygonAxis(List<Vertex> vertices, List<Vertex> auxillaryList, int componentIndex) {
		ClipPolygonComponent(vertices, componentIndex, 1.0f, auxillaryList);
		vertices.clear();

		if (auxillaryList.isEmpty()) {
			return false;
		}

		ClipPolygonComponent(auxillaryList, componentIndex, -1.0f, vertices);
		auxillaryList.clear();

		return !vertices.isEmpty();
	}

	private void ClipPolygonComponent(List<Vertex> vertices, int componentIndex, float componentFactor,
			List<Vertex> result) {
		Vertex previousVertex = vertices.get(vertices.size() - 1);
		float previousComponent = previousVertex.Get(componentIndex) * componentFactor;
		boolean previousInside = previousComponent <= previousVertex.pos.w;

		Iterator<Vertex> it = vertices.iterator();
		while (it.hasNext()) {
			Vertex currentVertex = it.next();
			float currentComponent = currentVertex.Get(componentIndex) * componentFactor;
			boolean currentInside = currentComponent <= currentVertex.pos.w;

			if (currentInside ^ previousInside) {
				float lerpAmt = (previousVertex.pos.w - previousComponent)
						/ ((previousVertex.pos.w - previousComponent) - (currentVertex.pos.w - currentComponent));

				result.add(previousVertex.Lerp(currentVertex, lerpAmt));
			}

			if (currentInside) {
				result.add(currentVertex);
			}

			previousVertex = currentVertex;
			previousComponent = currentComponent;
			previousInside = currentInside;
		}
	}

	public void FillTriangle(Vertex a, Vertex b, Vertex c, BitMap tex) {
		Vertex min = a.transform(screenTransform, Matrix4D.identity(),Matrix4D.identity()).PerspectiveDivide();
		Vertex mid = b.transform(screenTransform, Matrix4D.identity(),Matrix4D.identity()).PerspectiveDivide();
		Vertex max = c.transform(screenTransform, Matrix4D.identity(),Matrix4D.identity()).PerspectiveDivide();
		if (min.pos.y > mid.pos.y) {
			Vertex buf = min;
			min = mid;
			mid = buf;
		}
		if (mid.pos.y > max.pos.y) {
			Vertex buf = max;
			max = mid;
			mid = buf;
		}
		if (min.pos.y > mid.pos.y) {
			Vertex buf = min;
			min = mid;
			mid = buf;
		}
		scanTriangle(min, mid, max, min.cross(mid, max) < 0, tex);
		// drawLine((int)min.pos.x,(int)min.pos.y,(int)mid.pos.x,(int)mid.pos.y,0xffff0000);
		// drawLine((int)mid.pos.x,(int)mid.pos.y,(int)max.pos.x,(int)max.pos.y,0xffff0000);
		// drawLine((int)max.pos.x,(int)max.pos.y,(int)min.pos.x,(int)min.pos.y,0xffff0000);
	}

	private void scanTriangle(Vertex min, Vertex mid, Vertex max, boolean handedness, BitMap tex) {
		InterpolationData interpolation = new InterpolationData(min, mid, max);
		Edge topToBottom = new Edge(interpolation, min, max, 0);
		Edge topToMiddle = new Edge(interpolation, min, mid, 0);
		Edge middleToBottom = new Edge(interpolation, mid, max, 1);
		int top = topToMiddle.yStart;
		int bottom = topToMiddle.yEnd;
		Edge left = topToBottom;
		Edge right = topToMiddle;
		if (handedness) {
			Edge buf = left;
			left = right;
			right = buf;
		}
		for (int y = top; y < bottom; y++) {
			drawScanLine(interpolation, left, right, y, tex);
			left.step();
			right.step();
		}
		top = middleToBottom.yStart;
		bottom = middleToBottom.yEnd;
		left = topToBottom;
		right = middleToBottom;
		if (handedness) {
			Edge buf = left;
			left = right;
			right = buf;
		}
		for (int y = top; y < bottom; y++) {
			drawScanLine(interpolation, left, right, y, tex);
			left.step();
			right.step();
		}

	}

	private void drawScanLine(InterpolationData interpolation, Edge left, Edge right, int y, BitMap tex) {
		int xStart = (int) Math.ceil(left.x);
		int xEnd = (int) Math.ceil(right.x);
		float xPreStep = xStart - left.x;
		float xDist = right.x - left.x;
		float texCoordXXStep = (right.TexCoordX - left.TexCoordX) / xDist;
		float texCoordYXStep = (right.TexCoordY - left.TexCoordY) / xDist;
		float oneOverZXStep = (right.oneOverZ - left.oneOverZ) / xDist;
		float depthXStep = (right.depth - left.depth) / xDist;
		float normalXXStep = (right.normalX - left.normalX) / xDist;
		float normalYXStep = (right.normalY - left.normalY) / xDist;
		float normalZXStep = (right.normalZ - left.normalZ) / xDist;
		float posXXStep = (right.posX - left.posX) / xDist;
		float posYXStep = (right.posY - left.posY) / xDist;
		float posZXStep = (right.posZ - left.posZ) / xDist;
		
		float texCoordX = left.TexCoordX + xPreStep * texCoordXXStep;
		float texCoordY = left.TexCoordY + xPreStep * texCoordYXStep;
		float oneOverZ = left.oneOverZ + xPreStep * oneOverZXStep;
		float depth = left.depth + xPreStep * depthXStep;
		float normalX = left.normalX + xPreStep * normalXXStep;
		float normalY = left.normalY + xPreStep * normalYXStep;
		float normalZ = left.normalZ + xPreStep * normalZXStep;
		float posX = left.posX + xPreStep * posXXStep;
		float posY = left.posY + xPreStep * posYXStep;
		float posZ = left.posZ + xPreStep * posZXStep;
		
		for (int x = xStart; x < xEnd; x++) {
			int index = x + y * width;
			if ((x < 0 != x < width) && (y < 0 != y < height)) {
				if (depth < zBuffer[index]) {
					float z = 1.0f / oneOverZ;
					zBuffer[index] = depth;
					Point3D normal3d = new Point3D(normalX*z,normalY*z,normalZ*z);
					normal3d = normal3d.scale(1.0f/normal3d.dist());
					Point4D normal = new Point4D(normal3d.x,normal3d.y,normal3d.z);
					Point4D pos = new Point4D(posX*z,posY*z,posZ*z);
					pos.add(new Point4D(0,0,0));
					int srcx = (int) (texCoordX * tex.width * z);
					int srcy = (int) (texCoordY * tex.height * z);
					if ((x < 0 != x < width) & (y < 0 != y < height)) {
						setRGB(x, y, shade(tex.getRGB(srcx, srcy), clamp(normal.dot(new Point4D(0,1,0)))*0.8f+0.2f) | 0xff000000);
					}
				}
			}
			texCoordX += texCoordXXStep;
			texCoordY += texCoordYXStep;
			oneOverZ += oneOverZXStep;
			depth += depthXStep;
			normalX +=normalXXStep;
			normalY +=normalYXStep;
			normalZ +=normalZXStep;
			posX +=posXXStep;
			posY +=posYXStep;
			posZ +=posZXStep;

		}
	}
	private float clamp(float a){
		if(a<0)return 0;
		if(a>1)return 1;
		return a;
	}

	private int shade(int color, float value) {
		int a = (color >> 24) & 0xff;
		int r = (color >> 16) & 0xff;
		int g = (color >> 8) & 0xff;
		int b = color & 0xff;
		float value2 = value>1?value-1:0;
		int aOut = clamp255((int) ((a * value)+value2*256));
		int rOut = clamp255((int) ((r * value)+value2*256));
		int gOut = clamp255((int) ((g * value)+value2*256));
		int bOut = clamp255((int) ((b * value)+value2*256));
		return (aOut << 24) | (rOut << 16) | (gOut << 8) | bOut;
	}

	private int clamp255(int a) {
		if (a > 255)
			return 255;
		if (a < 0)
			return 0;
		return a;
	}
	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		perspectiveTransform = Matrix4D.initPerspective(fov, (float) width / (float) height, 0.0001f, 1000);
		this.fov = fov;
	}

}
