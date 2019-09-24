package src;

import java.util.ArrayList;

import vectors.Matrix4D;

public class Mesh {
	ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	ArrayList<Integer> indices = new ArrayList<Integer>();

	public Mesh() {
	}

	public Mesh Transform(Matrix4D transform, Matrix4D normalTransform, Matrix4D worldPosTransform) {
		Mesh out = new Mesh();
		for (Vertex vertex : vertices) {
			out.addVertex(vertex.transform(transform, normalTransform, worldPosTransform));
		}
		for (int index : indices) {
			out.indices.add(index);
		}

		return out;
	}

	public int addVertex(Vertex vertex) {
		vertices.add(vertex);
		return vertices.size() - 1;
	}

	public void createTriangle(int a, int b, int c) {
		indices.add(a);
		indices.add(b);
		indices.add(c);
	}

	public void addTriangle(Vertex a, Vertex b, Vertex c) {
		createTriangle(addVertex(a), addVertex(b), addVertex(c));
	}
	public Triangle getTriangle(int index){
		return new Triangle(vertices.get(indices.get(index*3)),vertices.get(indices.get(index*3+1)),vertices.get(indices.get(index*3+2)));
	}
	public int getLength(){
		return indices.size()/3;
	}

}
