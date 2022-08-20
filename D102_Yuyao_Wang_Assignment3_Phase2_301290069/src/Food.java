
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import processing.core.PVector;

public class Food {

	private PVector pos, vel;
	private double bodyW, bodyH;
	private double scale;

	private Ellipse2D.Double head, eyes1, eyes2, eyes3, eyes4, body, foot1, foot2, foot3, foot4;
	private Line2D.Double tail;
	private Rectangle2D.Double box;
	private boolean isPressed;
	private Color foodColor;
	private Area outline;

	public Food(PVector pos, double bodyW, double bodyH, double scale) {
		// public Food(PVector pos, double sc,int size) {
		this.pos = pos;
		this.bodyW = bodyW;
		this.bodyH = bodyH;
		this.scale = scale;

		this.foodColor = Color.red;

		int speed1 = (int) (1 + Math.random() * 1.5);
		int speed2 = (int) (1 + Math.random() * 1.5);
		// vel = new PVector(0,0); //SEPEED
		vel = new PVector(speed1, speed2); // SEPEED

		// geom part.
		head = new Ellipse2D.Double();
		eyes1 = new Ellipse2D.Double();
		eyes2 = new Ellipse2D.Double();
		eyes3 = new Ellipse2D.Double();
		eyes4 = new Ellipse2D.Double();
		body = new Ellipse2D.Double();
		foot1 = new Ellipse2D.Double();
		foot2 = new Ellipse2D.Double();
		foot3 = new Ellipse2D.Double();
		foot4 = new Ellipse2D.Double();
		tail = new Line2D.Double();
		box = new Rectangle2D.Double();
		// 先运行get outline ,因为area null.
		setAttributes();
		setOutline();
	}

	public void setAttributes() {
		head.setFrame(-25, -3, 10, 10);
		eyes1.setFrame(-25, -5, 7, 7);
		eyes2.setFrame(-25, 3, 7, 7);
		eyes3.setFrame(-23, -3, 2, 2);
		eyes4.setFrame(-23, 5, 2, 2);
		body.setFrame(-20, -3, 40, 10);
		foot1.setFrame(5, -7, 8, 8);
		foot2.setFrame(5, 3, 8, 8);
		foot3.setFrame(-8, -7, 8, 8);
		foot4.setFrame(-8, 3, 8, 8);
		tail.setLine(19, 2, 30, 2);

		box.setFrame((int) -bodyW / 2, (int) -bodyH / 2, (int) bodyW, (int) bodyH);
	}

	private void setOutline() { // set outline  , eyes , body point actually dont need to add , but im lazy so i put all in the outline
		outline = new Area();
		outline.add(new Area(head));
		outline.add(new Area(eyes1));
		outline.add(new Area(eyes2));
		outline.add(new Area(eyes3));
		outline.add(new Area(eyes4));
		outline.add(new Area(body));
		outline.add(new Area(foot1));
		outline.add(new Area(foot2));
		outline.add(new Area(foot3));
		outline.add(new Area(foot4));
		outline.add(new Area(tail));
	}

	public Shape tookOutline() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.scale(scale, scale);
		return at.createTransformedShape(outline);
	}

	public PVector getPos() {
		return pos;
	}

	// 如果有动画 ， 那么setAttributes 全部丢draw 或者move里面。
	public void drawFood(Graphics2D g2) {
		// TODO Auto-generated method stub
		AffineTransform at = g2.getTransform();
		g2.translate(pos.x, pos.y);
//		enlarge();
		g2.scale(scale, scale);
		float angle = vel.heading();
		g2.rotate(angle + Math.PI);
		if (vel.x > 0) {
			g2.scale(1, -1);
		}
//		setAttributes();
//		setOutline();
		// face
		g2.setColor(Color.yellow);
		g2.fill(head);
		// eyes
		g2.setColor(Color.pink);
		g2.fill(eyes1);
		g2.fill(eyes2);
		// small eyes
		g2.setColor(Color.black);
		g2.draw(eyes3);
		g2.draw(eyes4);
		// body
		g2.setColor(foodColor);
		g2.fill(body);
		// foot
		g2.setColor(Color.orange);
		g2.fill(foot1);
		g2.fill(foot2);
		g2.setColor(Color.lightGray);
		g2.fill(foot3);
		g2.fill(foot4);
		// tail
		g2.setColor(Color.black);
		g2.draw(tail);

		g2.setColor(Color.red);
		// g2.draw(box);
		g2.setTransform(at);

	}

	public boolean checkMouseHit(MouseEvent e) {
		return (Math.abs(e.getX() - pos.x) < bodyW / 2) && (Math.abs(e.getY() - pos.y) < bodyH / 2);
	}

	public void setPos(int x, int y) {
		this.pos = new PVector(x, y);
	}

//	public void enlarge(double size) {
//		if (isPressed == true && bodyH * bodyW * scale < size / 3) {
//			// System.out.println("testing");
//			scale *= 1.1;
//		}
//	}

//	public void click(MouseEvent e) {
//		isPressed = false;
//		if (e.getClickCount() == 1)
//			isPressed = true;
//	}
//
//	public void realsed(MouseEvent e) {
//		isPressed = false;
//	}

	public void setColor(Color color) {

		this.foodColor = color;
	}

	public float getSize() {
		return (float) scale*2;
	}

}
