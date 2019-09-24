package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import vectors.Point4D;

public class OBJLoader {
	List<Point4D> positions = new ArrayList<Point4D>();
	List<Point4D> normals = new ArrayList<Point4D>();
	List<Point4D> texcoords = new ArrayList<Point4D>();
	List<Integer> indices = new ArrayList<Integer>();

	public OBJLoader(String file, boolean relative) {
		String directory = System.getProperty("user.dir");
		StringBuilder stringBuilder = new StringBuilder();
		if (relative) {
			stringBuilder.append(directory);
		}
		stringBuilder.append(file);
		List<String> data = Load(stringBuilder.toString());
		normals.add(new Point4D(0, 1, 0));
		positions.add(new Point4D(0, 0, 0));
		texcoords.add(new Point4D(0, 0, 0));
		Format(data);
	}

	public List<String> Load(String directory) {
		File file = new File(directory);
		List<String> out = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				out.add(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return out;
	}

	public void Format(List<String> data) {
		for (int i = 0; i < data.size(); i++) {
			String line = data.get(i);
			if (line.charAt(0) == '#') {
				data.remove(i);
				i--;
			}
		}
		for (int i = 0; i < data.size(); i++) {
			String line = data.get(i);
			String[] tokens = line.split(" ");
			tokens = clean(tokens);
			if (tokens[0].equals("f")) {
				int[] a = faceToken(tokens[1]);
				int[] b = faceToken(tokens[2]);
				int[] c = faceToken(tokens[3]);
				Point4D avert = positions.get(a[0]);
				Point4D bvert = positions.get(b[0]);
				Point4D cvert = positions.get(c[0]);
				Point4D normal = (bvert.sub(avert)).cross(cvert.sub(avert));
				normals.set(a[0],normals.get(a[0]).add(normal));
				normals.set(b[0],normals.get(b[0]).add(normal));
				normals.set(c[0],normals.get(c[0]).add(normal));
				indices.add(a[0]);
				indices.add(b[0]);
				indices.add(c[0]);
				indices.add(a[1]);
				indices.add(b[1]);
				indices.add(c[1]);
				indices.add(a[2]);
				indices.add(b[2]);
				indices.add(c[2]);
			}
			if (tokens[0].equals("v")) {
				float x = Float.parseFloat(tokens[1]);
				float y = Float.parseFloat(tokens[2]);
				float z = Float.parseFloat(tokens[3]);
				positions.add(new Point4D(x,y,z));
				normals.add(new Point4D(0,0,0));
			}
			if (tokens[0].equals("vn")) {
				float x = Float.parseFloat(tokens[1]);
				float y = Float.parseFloat(tokens[2]);
				float z = Float.parseFloat(tokens[3]);
				normals.add(new Point4D(x,y,z));
				//normals.add(new Point4D(0,0,0));
			}
		}
	}
	private String[] clean(String[] in){
		int count = 0;
		for(int i = 0;i<in.length;i++){
			if(!in[i].equals("")){
				count++;
			}
		}
		String[] out = new String[count];
		int position=0;
		for(int i = 0;i<in.length;i++){
			if(!in[i].equals("")){
				out[position]=in[i];
				position++;
			}
		}
		return out;
	}
	public Mesh toMesh(){
		Mesh out = new Mesh();
		for(int i = 0;i<positions.size();i++){
			out.addVertex(new Vertex(positions.get(i),normals.get(i).normalized(),texcoords.get(0), positions.get(i)));
		}
		for(int i = 0;i<indices.size();i+=9){
			System.out.print(indices.get(i)+" ");
			System.out.print(indices.get(i+1)+" ");
			System.out.print(indices.get(i+2));
			System.out.println();
			out.createTriangle(indices.get(i), indices.get(i+1), indices.get(i+2));
		}
		return out;
	}

	private int[] faceToken(String in) {
		String[] split = in.split("/");
		if (split.length == 1) {
			String buf = split[0];
			split = new String[3];
			split[0]=buf;
			split[1] = "";
			split[2] = "";

		}
		int[] out = { 	interpret(split[0]),
						interpret(split[1]),
						interpret(split[2]) };
		return out;
	}

	private int interpret(String in) {
		try {
			return Integer.parseInt(in);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
