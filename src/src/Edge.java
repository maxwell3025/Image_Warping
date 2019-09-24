package src;

public class Edge {
	int yStart;
	int yEnd;
	float x;
	float xStep;
	float TexCoordX;
	float TexCoordXStep;
	float TexCoordY;
	float TexCoordYStep;
	float oneOverZ;
	float oneOverZStep;
	float depth;
	float depthStep;
	float normalX;
	float normalXStep;
	float normalY;
	float normalYStep;
	float normalZ;
	float normalZStep;
	float posX;
	float posXStep;
	float posY;
	float posYStep;
	float posZ;
	float posZStep;

	public Edge(InterpolationData interpolation, Vertex min, Vertex max, int minVertIndex) {
		yStart = (int) Math.ceil(min.pos.y);
		yEnd = (int) Math.ceil(max.pos.y);
		float yDist = max.pos.y - min.pos.y;
		float xDist = max.pos.x - min.pos.x;
		xStep = xDist / yDist;
		float yPreStep = yStart - min.pos.y;
		x = min.pos.x + xStep * yPreStep;
		TexCoordXStep = interpolation.texCoordXYStep + interpolation.texCoordXXStep * xStep;
		TexCoordYStep = interpolation.texCoordYYStep + interpolation.texCoordYXStep * xStep;
		oneOverZStep = interpolation.oneOverZYStep + interpolation.oneOverZXStep * xStep;
		depthStep = interpolation.depthYStep + interpolation.depthXStep * xStep;
		normalXStep = interpolation.normalXYStep + interpolation.normalXXStep * xStep;
		normalYStep = interpolation.normalYYStep + interpolation.normalYXStep * xStep;
		normalZStep = interpolation.normalZYStep + interpolation.normalZXStep * xStep;
		posXStep = interpolation.posXYStep + interpolation.posXXStep * xStep;
		posYStep = interpolation.posYYStep + interpolation.posYXStep * xStep;
		posZStep = interpolation.posZYStep + interpolation.posZXStep * xStep;
		
		TexCoordX = interpolation.texCoordX[minVertIndex] + TexCoordXStep * yPreStep;
		TexCoordY = interpolation.texCoordY[minVertIndex] + TexCoordYStep * yPreStep;
		oneOverZ = interpolation.oneOverZ[minVertIndex] + oneOverZStep * yPreStep;
		depth = interpolation.depth[minVertIndex] + depthStep * yPreStep;
		normalX = interpolation.normalX[minVertIndex] + normalXStep * yPreStep;
		normalY = interpolation.normalY[minVertIndex] + normalYStep * yPreStep;
		normalZ = interpolation.normalZ[minVertIndex] + normalZStep * yPreStep;
		posX = interpolation.posX[minVertIndex] + posXStep * yPreStep;
		posY = interpolation.posY[minVertIndex] + posYStep * yPreStep;
		posZ = interpolation.posZ[minVertIndex] + posZStep * yPreStep;
	}

	public void step() {
		x += xStep;
		TexCoordX += TexCoordXStep;
		TexCoordY += TexCoordYStep;
		oneOverZ += oneOverZStep;
		depth+=depthStep;
		normalX+=normalXStep;
		normalY+=normalYStep;
		normalZ+=normalZStep;
		posX+=posXStep;
		posY+=posYStep;
		posZ+=posZStep;
	}
}
