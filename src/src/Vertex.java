package src;

import vectors.Matrix4D;
import vectors.Point4D;

public class Vertex {
	Point4D pos;
	Point4D texCoords;
	Point4D normal;
	Point4D worldPos;

	public Vertex(Point4D position, Point4D norm, Point4D tex, Point4D worldPosition) {
		pos = position;
		texCoords = tex;
		normal = norm;
		worldPos = worldPosition;
	}

	public float cross(Vertex a, Vertex b) {
		return (a.pos.x - pos.x) * (b.pos.y - pos.y) - (a.pos.y - pos.y) * (b.pos.x - pos.x);
	}

	public Vertex transform(Matrix4D transform, Matrix4D normalTransform, Matrix4D worldPosTransform) {
		return new Vertex(transform.transform(pos), normalTransform.transform(normal), texCoords,worldPosTransform.transform(worldPos));
	}

	public Vertex PerspectiveDivide() {
		return new Vertex(new Point4D(pos.x / pos.w, pos.y / pos.w, pos.z / pos.w, pos.w), normal, texCoords,worldPos);
	}

	public Vertex Lerp(Vertex other, float lerpAmt) {
		return new Vertex(pos.Lerp(other.pos, lerpAmt), normal.Lerp(other.normal, lerpAmt),
				texCoords.Lerp(other.texCoords, lerpAmt),worldPos.Lerp(other.worldPos,lerpAmt));
	}

	public float Get(int index) {
		switch (index) {
		case 0:
			return pos.x;
		case 1:
			return pos.y;
		case 2:
			return pos.z;
		case 3:
			return pos.w;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	public boolean IsInsideViewFrustum()
	{
		return 
			Math.abs(pos.x) <= Math.abs(pos.w) &&
			Math.abs(pos.y) <= Math.abs(pos.w) &&
			Math.abs(pos.z) <= Math.abs(pos.w);
	}
}
