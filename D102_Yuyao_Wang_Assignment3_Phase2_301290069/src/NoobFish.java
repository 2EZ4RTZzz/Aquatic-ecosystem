import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import processing.core.PVector;

public class NoobFish extends Fish{
	private Arc2D.Double face;
	private Ellipse2D.Double body,eyes,tail,fin1,fin2,mouth;
	private Polygon fin3;	
	private int randomFin;
	
	String Fishname= "NoobFish";
	
	

	public NoobFish (PVector loc, int w, int h, double sc) {
		super(loc,w,h,sc);
		
		randomFin = (int) (1+Math.random()*50);
		body = new Ellipse2D.Double();
		face = new Arc2D.Double();
		eyes = new Ellipse2D.Double();
		tail = new Ellipse2D.Double();
		fin1 = new Ellipse2D.Double();
		fin2 = new Ellipse2D.Double();
		mouth = new Ellipse2D.Double();
		
		
		setAttributes();
		setOutline();
	}
	public void setAttributes() {  
		body.setFrame(-55, -25, 130, 50);
		face.setArc(23,-20,45,40,-270,180,Arc2D.OPEN);
		mouth.setFrame(55, 0, 10, 10);
		eyes.setFrame(40,-8,6,6);
		tail.setFrame(-70, -20, 30, 40);
		fin1.setFrame(0, 0, 20, 20);
		fin2.setFrame(-20,-20,10,10);
		int[] XArray1= {-10,10,-5};
		int[] yArray1= {0,-15,15};
		fin3 = new Polygon(XArray1, yArray1,XArray1.length);
		//FOV 
		//sight = (float)bodyW * maxSpeed * .15f;
		sight = 120;
		fov = new Arc2D.Double(-sight, -sight, sight * 2, sight * 2, -55, 110, Arc2D.PIE);
	}
	
	public void setOutline() {    //set outline
		outline = new Area();
		outline.add(new Area(body));
		outline.add(new Area(tail));
	}
	public void drawFish(Graphics2D g2) {    //override
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //画面撕裂 
		AffineTransform at = g2.getTransform();
		g2.translate(loc.x, loc.y);   
		//System.out.println(loc.x);
		g2.scale(scale, scale);
		float angle = vel.heading(); 
		g2.rotate(angle);
		if (vel.x < 0) g2.scale(1, -1);
		double randomAngle=(Math.random()*Math.PI);
		if(state == Dead) {
			g2.rotate(randomAngle);
			if(scale > 0) 
			this.scale-=0.009;
		}

		if (vel.x < 0) g2.scale(1, -1);
		setAttributes();
		setOutline();
		g2.setColor(changeColor);  
		//body  	
		if(state == Sick) {
			g2.setColor(new Color(57, 94, 49));
		}else if(state == Dead) {
			g2.setColor(new Color(42, 59, 39));
		}
		g2.fill(body);
		g2.setColor(new Color(32, 247, 175));
		//mouth	 
		g2.draw(face);
		g2.setColor(new Color(227, 93, 100)); 
		g2.fill(mouth);
		//eyes
		g2.setColor(new Color(0,0,0));
		g2.fill(eyes);
		
		//tail 
		g2.setColor(new Color(207, 50, 107));
		g2.fill(tail);
		
		//fish rgb fin!! 
		if(randomFin>25) {
			g2.setColor(new Color((int)(Math.random()*255),(int)Math.random()*255,(int)Math.random()*255));
			g2.fill(fin3);		
		}
		g2.setColor(new Color(0,(int)(Math.random()*255),0));
		g2.fill(fin1);
		g2.fill(fin2);
		//FOV and box
		g2.setColor(Color.yellow);
		g2.draw(fov);
		g2.setTransform(at);
		
//		g2.setColor(Color.PINK);
//		g2.draw(getOutline().getBounds2D());
	}
	 
	//unique mehtod , for the noobfish , when noobfish decte the boss fish , noobfish should runaway .
	public void runaway(Fish other) {
		float angle = (float) Math.atan2(loc.y -other.loc.y, loc.x - other.loc.x);		
		//if(scale<other.scale) {
			vel = PVector.fromAngle(angle);
			vel.mult(maxSpeed);
			if(countThreeSecs==0) {
				countThreeSecs=90;
				}
		runaway = true;
	}
	
	public Food AfcFood(ArrayList<Food> food) {     //for the assignment3 , this method is only use on the NoobFish , cause only the noobfishs eats the foods
		food = RemoveOldFoods(food);
		if (food.size() == 0)    
			return null;

		Food afcFood = food.get(0);
		float afcDist = afcFood.getSize() / PVector.dist(this.getPos(), afcFood.getPos());

		for (Food s : food)
			if (s.getSize() / PVector.dist(this.getPos(), s.getPos()) > afcDist) {
				afcFood = s;
				afcDist = (s.getSize() / PVector.dist(this.getPos(), s.getPos()));
			}
		return afcFood;
	}
	
	public boolean detectCollision(Food target) {  //可以move到noobfish里面，因为也只有noobfish会吃小食物
		boolean hit = false;
		//System.out.println(hit);
		if (getOutline().intersects(target.tookOutline().getBounds2D()) && target.tookOutline().intersects(getOutline().getBounds2D()))
			hit = true;
		if(hit == true) {
			energy += vel.mag()*target.getSize()*5;
		}
		return hit;
		//增加energy
	}
	
}
