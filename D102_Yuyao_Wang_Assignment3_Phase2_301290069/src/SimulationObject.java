import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;

import processing.core.PVector;

public abstract class SimulationObject {
	protected PVector loc,vel;
	protected double bodyW,bodyH,scale;
	protected Color changeColor; 
	protected float maxSpeed,sight;
	protected Arc2D.Double fov;  
	protected Area outline; 
	
	public SimulationObject(PVector loc, int w, int h, double sc) {
		this.loc = loc;
		bodyW = w;
		bodyH = h;
		scale = sc;
		this.vel = Util.randomPVector(maxSpeed);
		this.changeColor = new Color((int)(1+Math.random()*255),(int)(1+Math.random()*255),(int)(1+Math.random()*255));
		setAttributes();
		setOutline();
	}
	public void setAttributes() {
	}
	private void setOutline() {    //set outline
	}
	protected Shape getOutline() { // can do the public / protected
		AffineTransform at = new AffineTransform();
		at.translate(loc.x, loc.y);
		at.rotate(vel.heading());
		at.scale(scale, scale);
		return at.createTransformedShape(outline);
	}
	public Shape getFOV() {  //get fov outline 
		AffineTransform at = new AffineTransform();
		at.translate(loc.x, loc.y);
		at.rotate(vel.heading());
		at.scale(scale, scale);
		return at.createTransformedShape(fov);
		}
	public void drawFish(Graphics2D g2) {  
	}
	public double getSize() {
		return bodyW*bodyH*scale;
	}
	public Color getColor() {
		return changeColor;
	}
		
	public PVector getPos() {  
		return loc;
	}
	PVector wallPushForce() {
		PVector force = new PVector();
		float wallCoef = 50.0f;	
		// compute force based on distance from edge
		double distance = 0;
		
		distance = Ocean.leftEdge.ptLineDist(loc.x, loc.y) - bodyW * scale;
		force.add(new PVector((float) (+wallCoef / Math.pow(distance, 2)), 0.0f)); //left wall force
		
		distance = Ocean.rightEdge.ptLineDist(loc.x, loc.y) - bodyW * scale;
		force.add(new PVector((float) (-wallCoef / Math.pow(distance, 2)), 0.0f)); //right wall force
		
		distance = Ocean.topEdge.ptLineDist(loc.x, loc.y) - bodyH * scale;
		force.add(new PVector(0.0f, (float) (+wallCoef / Math.pow(distance, 2))));	//top wall force
		
		distance = Ocean.bottomEdge.ptLineDist(loc.x, loc.y) - bodyH * scale;
		force.add(new PVector(0.0f, (float) (-wallCoef / Math.pow(distance, 2))));	//bottom wall force
		
		return force;
	}
}
