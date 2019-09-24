package src;

public class InterpolationData {
	float[] texCoordX = new float[3];
	float[] texCoordY = new float[3];
	float[] oneOverZ = new float[3];
	float[] depth = new float[3];
	float[] normalX = new float[3];
	float[] normalY = new float[3];
	float[] normalZ = new float[3];
	float[] posX = new float[3];
	float[] posY = new float[3];
	float[] posZ = new float[3];
	float texCoordXXStep;
	float texCoordXYStep;
	float texCoordYXStep;
	float texCoordYYStep;
	float oneOverZXStep;
	float oneOverZYStep;
	float depthXStep;
	float depthYStep;
	float normalXXStep;
	float normalXYStep;
	float normalYXStep;
	float normalYYStep;
	float normalZXStep;
	float normalZYStep;
	float posXXStep;
	float posXYStep;
	float posYXStep;
	float posYYStep;
	float posZXStep;
	float posZYStep;
	public InterpolationData(Vertex min, Vertex mid, Vertex max) {
		float oneOverdX = 1.0f / (((mid.pos.x - max.pos.x) * (min.pos.y - max.pos.y))
				- ((min.pos.x - max.pos.x) * (mid.pos.y - max.pos.y)));
		float oneOverdY = -oneOverdX;
		oneOverZ[0]=1.0f/min.pos.w;
		oneOverZ[1]=1.0f/mid.pos.w;
		oneOverZ[2]=1.0f/max.pos.w;
		depth[0]=min.pos.z;
		depth[1]=mid.pos.z;
		depth[2]=max.pos.z;
		normalX[0]=min.normal.x*oneOverZ[0];
		normalX[1]=mid.normal.x*oneOverZ[1];
		normalX[2]=max.normal.x*oneOverZ[2];
		normalY[0]=min.normal.y*oneOverZ[0];
		normalY[1]=mid.normal.y*oneOverZ[1];
		normalY[2]=max.normal.y*oneOverZ[2];
		normalZ[0]=min.normal.z*oneOverZ[0];
		normalZ[1]=mid.normal.z*oneOverZ[1];
		normalZ[2]=max.normal.z*oneOverZ[2];
		posX[0]=min.worldPos.x*oneOverZ[0];
		posX[1]=mid.worldPos.x*oneOverZ[1];
		posX[2]=max.worldPos.x*oneOverZ[2];
		posY[0]=min.worldPos.y*oneOverZ[0];
		posY[1]=mid.worldPos.y*oneOverZ[1];
		posY[2]=max.worldPos.y*oneOverZ[2];
		posZ[0]=min.worldPos.z*oneOverZ[0];
		posZ[1]=mid.worldPos.z*oneOverZ[1];
		posZ[2]=max.worldPos.z*oneOverZ[2];
		texCoordX[0]=min.texCoords.x*oneOverZ[0];
		texCoordX[1]=mid.texCoords.x*oneOverZ[1];
		texCoordX[2]=max.texCoords.x*oneOverZ[2];
		texCoordY[0]=min.texCoords.y*oneOverZ[0];
		texCoordY[1]=mid.texCoords.y*oneOverZ[1];
		texCoordY[2]=max.texCoords.y*oneOverZ[2];
		oneOverZXStep = CalcXStep(oneOverZ, min,mid,max,oneOverdX);
		oneOverZYStep = CalcYStep(oneOverZ, min,mid,max,oneOverdY);
		depthXStep = CalcXStep(depth, min,mid,max,oneOverdX);
		depthYStep = CalcYStep(depth, min,mid,max,oneOverdY);
		texCoordXXStep = CalcXStep(texCoordX, min,mid,max,oneOverdX);
		texCoordXYStep = CalcYStep(texCoordX, min,mid,max,oneOverdY);
		texCoordYXStep = CalcXStep(texCoordY, min,mid,max,oneOverdX);
		texCoordYYStep = CalcYStep(texCoordY, min,mid,max,oneOverdY);
		normalXXStep = CalcXStep(normalX, min,mid,max,oneOverdX);
		normalXYStep = CalcYStep(normalX, min,mid,max,oneOverdY);
		normalYXStep = CalcXStep(normalY, min,mid,max,oneOverdX);
		normalYYStep = CalcYStep(normalY, min,mid,max,oneOverdY);
		normalZXStep = CalcXStep(normalZ, min,mid,max,oneOverdX);
		normalZYStep = CalcYStep(normalZ, min,mid,max,oneOverdY);
		posXXStep = CalcXStep(posX, min,mid,max,oneOverdX);
		posXYStep = CalcYStep(posX, min,mid,max,oneOverdY);
		posYXStep = CalcXStep(posY, min,mid,max,oneOverdX);
		posYYStep = CalcYStep(posY, min,mid,max,oneOverdY);
		posZXStep = CalcXStep(posZ, min,mid,max,oneOverdX);
		posZYStep = CalcYStep(posZ, min,mid,max,oneOverdY);
	}
	private float CalcXStep(float[] values, Vertex min, Vertex mid, Vertex max, float oneOverdX) {
		return (((values[1] - values[2]) * (min.pos.y - max.pos.y))
				- ((values[0] - values[2]) * (mid.pos.y - max.pos.y))) * oneOverdX;
	}

	private float CalcYStep(float[] values, Vertex min, Vertex mid, Vertex max, float oneOverdY) {
		return (((values[1] - values[2]) * (min.pos.x - max.pos.x))
				- ((values[0] - values[2]) * (mid.pos.x - max.pos.x))) * oneOverdY;
	}
}
