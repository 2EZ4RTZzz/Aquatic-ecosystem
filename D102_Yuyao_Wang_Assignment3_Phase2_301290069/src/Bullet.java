import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import processing.core.PVector;

public class Bullet {
	private PVector loc,vel;
	private double bodyW,bodyH;
	private Ellipse2D.Double body;
	private Area outline;
	private double scale;
	public PVector setVel;

	public Bullet(PVector loc, PVector vel) {
		this.vel=vel;
		this.loc=loc;
		this.bodyW=10;
		this.bodyH=10;
		this.scale=1;
		body = new Ellipse2D.Double();
		setAttributes();
		setOutline();		
	}
	public void setAttributes() {  
		body.setFrame(-bodyW/2+55,-bodyH/2,10,10);
	}
	
	public void setOutline() {    //set outline
		outline = new Area();
		outline.add(new Area(body));
	}
	public void drawBullet(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //画面撕裂 
		AffineTransform at = g2.getTransform();
		g2.translate(loc.x, loc.y); 
		g2.scale(scale ,scale);
		setAttributes();
		setOutline();
		g2.setColor(Util.randomColor());
		g2.fill(body);
		g2.setTransform(at);
	}
	public void move() {
		loc.add(vel);
	}
	public Shape getOutline() { // can do the public / protected
		AffineTransform at = new AffineTransform();
		at.translate(loc.x, loc.y);
		at.rotate(vel.heading());
		at.scale(scale,scale);
		return at.createTransformedShape(outline);
	}
	public boolean hit(Fish other) {
		return (getOutline().intersects(other.getOutline().getBounds2D())|| other.getOutline().intersects(getOutline().getBounds2D()));  //double check
//		return true;
	}
	
	public PVector getPos() {
		return loc;
	}
	public void setVel(PVector vel) {
		this.vel = vel;
	}
}
