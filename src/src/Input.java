package src;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import vectors.Point4D;

public class Input implements KeyListener, MouseMotionListener, MouseWheelListener {
	public boolean[] keyPressed = new boolean[65536];
	public boolean mouseInput = false;
	Component component;
	int mouseX = 0;
	int mouseY = 0;
	public float sensitivity = 0.25f;
	public float FOVSensitivity = 0.1f;
	int mouseWheelChange;
	double time = 0;
	public Input(Component component) {
		component.addKeyListener(this);
		component.addMouseMotionListener(this);
		component.addMouseWheelListener(this);
		for (Container container = component.getParent(); container != null; container = container.getParent()) {
			container.addKeyListener(this);
			container.addMouseMotionListener(this);
			container.addMouseWheelListener(this);
		}
		this.component = component;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyPressed[e.getKeyCode()] = true;
		if (e.getKeyCode() == KeyEvent.VK_E) {
			mouseInput = !mouseInput;
			int difX = 0;
			int difY = 0;
			for (Container container = component.getParent(); container != null; container = container.getParent()) {
				difX += container.getX();
				difY += container.getY();
			}
			try {
				new Robot().mouseMove(difX + (int) component.getPreferredSize().getWidth() / 2,
						difY + (int) component.getPreferredSize().getHeight() / 2);
			} catch (AWTException e1) {
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyPressed[e.getKeyCode()] = false;

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (mouseInput) {
			mouseX += e.getX() - (int) component.getPreferredSize().getWidth() / 2;
			mouseY += e.getY() - (int) component.getPreferredSize().getHeight() / 2;
			int difX = 0;
			int difY = 0;
			for (Container container = component.getParent(); container != null; container = container.getParent()) {
				difX += container.getX();
				difY += container.getY();
			}
			try {
				new Robot().mouseMove(difX + (int) component.getPreferredSize().getWidth() / 2,
						difY + (int) component.getPreferredSize().getHeight() / 2);
			} catch (AWTException e1) {
			}
		}
	}

	public void update(Camera camera,float speed) {
		int timeChange = (int) (System.nanoTime() - time);
		time = System.nanoTime();
		float dt = timeChange / 1000000000.0f;
		camera.yaw += mouseX * sensitivity*dt;
		camera.pitch += mouseY * sensitivity*dt;
		camera.fov+=mouseWheelChange *Math.PI*dt*FOVSensitivity;
		camera.applyTransform();
		if (keyPressed[KeyEvent.VK_W]) {
			camera.pos = camera.pos.add(camera.transform.inverse().transform(new Point4D(0, 0, speed * dt, 0)));
		}
		if (keyPressed[KeyEvent.VK_S]) {
			camera.pos = camera.pos.add(camera.transform.inverse().transform(new Point4D(0, 0, -speed * dt, 0)));
		}
		if (keyPressed[KeyEvent.VK_D]) {
			camera.pos = camera.pos.add(camera.transform.inverse().transform(new Point4D(speed * dt, 0, 0, 0)));
		}
		if (keyPressed[KeyEvent.VK_A]) {
			camera.pos = camera.pos.add(camera.transform.inverse().transform(new Point4D(-speed * dt, 0, 0, 0)));
		}
		mouseX = 0;
		mouseY = 0;
		mouseWheelChange = 0;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
mouseWheelChange+=e.getWheelRotation();
		
	}
}
