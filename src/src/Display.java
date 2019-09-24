package src;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.sun.glass.events.KeyEvent;

import vectors.Matrix3D;
import vectors.Matrix4D;
import vectors.Point4D;

public class Display extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int width, height;
	BufferedImage frameBuffer;
	TriangleRenderer frame;
	BitMap tex = new BitMap(0, 0);
	boolean paintdone = false;
	Mesh mesh = new Mesh();
	Camera camera = new Camera(Point4D.Origin());
	boolean stillMouse = false;
	Point4D light = new Point4D(1, 1, 1, 0);
	long time = 0;
	Input input;
	float dt;

	public Display(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		frame = new TriangleRenderer(width, height);
		try {
			BufferedImage texture = ImageIO.read(new File(System.getProperty("user.dir")+"/res/texture.png"));
			tex = new BitMap(texture);
		} catch (IOException e) {
		}
		mesh = new OBJLoader("/res/bunny.obj",true).toMesh();
	}

	public void Init() {
		input = new Input(this);
	}

	public void UpdateGraphics() {
		frame.reset();
		frame.setFov(camera.fov);
		input.update(camera,1f);
		Matrix4D rotation = new Matrix4D(Matrix3D.roty(System.nanoTime()/1000000000.0f));
		Matrix4D translation = Matrix4D.translate(0, -0.05f,0);
		frame.drawMesh(camera.Transform(mesh.Transform(translation,Matrix4D.identity(),translation).Transform(rotation, rotation, rotation)), tex,new Point4D(0f, 0.05f, 0f, 1), 0.01f);
		frame.copyToImage(frameBuffer);
		if(input.keyPressed[KeyEvent.VK_C]){
			System.nanoTime();
		}
		paintdone = false;
		repaint();
		while (!paintdone) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
			}
		}
	}

	public void paint(Graphics g) {
		g.drawImage(frameBuffer, 0, 0, width, height, null);
		paintdone = true;
	}

}
