/*
 * WangYuyao 301290067 IAT 265 D102
 * Checklist , all min requirements are done
 * Bonus part
 * I Finished part 1 and part2 ,
 * For Bonus part 1 , everytime when hunter press "space" hit the bossFish , bossfish will die in 3 secs with "rotate" + "getting smaller"+ "shield close" animations
 * 
 * For Bonus part 2 , when Hunter is up on the map , all BossFishes will directly swim to hunter , when bossfish collision the hunter,  
 * hunter will lose HP (it depends on hunter's size) #line 123 hunter.energy -= fishs.getSize() * 0.00005f;
 * When hunter's energy(hp) goes to 0 , hunter will die but it will reswpan again in 3 secs , there's a count down timer on the bottom right side.
 * if hunter is dead , all the bossfish will become normal again to catch the noobfishes.
 * 
 * Part 3 . 
 * my idea is like , create a method called triangleBossFishes();
 * there will be a if statement , (the bossfish wont automatic hit the hunter , util hunter shot the first bullet to kill one of the bossFish)
 * there are 6 bossfishes , if one bossfishes gets shot , all other bossfish will start swim to the Hunter , but the leader bossfish will be the 
 * Highest energy BossFish , and other BossFishes will call " chase" method to follow the highest Energy fishes. 
 * (use the for loop to get highest energy fish , we took this question during our quiz2 , part 1 ,question3)
 * 
 * but there's a issues, during the moving period , the highest energybossfish may changes . because when leaderBossFish swim to the hunter ,if any other BossFish
 * eats the small noobfish , that bossFish could become a new highest energy bossfish , then fish groups will rotate again... LOL
 * 
 * once the hunter gets killed , all the BossFishes will become normal again ( just write whole stateents in one if statements)
 * (this is pseudo code ， may i get at least 0.25 -0.5 point on this part 3 ? hahahhaha if not allgood hahahahha)
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import processing.core.PVector;

public class BossFish extends Fish {

	private Arc2D.Double face;
	private Ellipse2D.Double body,eyes,fin1,fin2,mouth,hand,tail1,tail2,deathAnmi1,deathAnmi2;
	private Polygon fin3;	
	private Line2D.Double line1,line2,line3,line4;
	private int randomFin;
	
	private Ellipse2D.Double broken1,broken2,broken3,broken4;
	String Fishname= "BossFish";
	private int y=0;

	
	
	public BossFish(PVector loc, int w, int h, double sc) {
		super(loc,w,h,sc);
		
		randomFin = (int) (1+Math.random()*50);
		body = new Ellipse2D.Double();
		face = new Arc2D.Double();
		eyes = new Ellipse2D.Double();
		tail1 = new Ellipse2D.Double();
		tail2 = new Ellipse2D.Double();
		fin1 = new Ellipse2D.Double();
		fin2 = new Ellipse2D.Double();
		mouth = new Ellipse2D.Double();
		hand = new Ellipse2D.Double();
		line1 = new Line2D.Double();
		line2 = new Line2D.Double();
		line3 = new Line2D.Double();
		line4 = new Line2D.Double();
		
		deathAnmi1 = new Ellipse2D.Double();
		deathAnmi2 = new Ellipse2D.Double();
		
		countThreeSecs=90;
		
		broken1 = new Ellipse2D.Double();
		broken2 = new Ellipse2D.Double();
		broken3 = new Ellipse2D.Double();
		broken4 = new Ellipse2D.Double();
		setAttributes();
		setOutline();
	}
	
	public void setAttributes() {
		body.setFrame((int)-55, (int)-25, (int)130, (int)50);
		face.setArc(23,-20,45,40,-270,180,Arc2D.OPEN);
		mouth.setFrame(55, 0, 10, 10);
		eyes.setFrame(40,-8,6,6);
		tail1.setFrame(-70,-15,20, 10);
		tail2.setFrame(-70, 5, 20, 10);
		fin1.setFrame(0, 0, 20, 20);
		fin2.setFrame(-20,-20,10,10);
		int[] XArray1= {-10,10,-5};
		int[] yArray1= {0,-15,15};
		fin3 = new Polygon(XArray1, yArray1,XArray1.length);
		
		hand.setFrame(0,-40,20,20);
		
		line1.setLine(-50,-30,60,-30);
		line2.setLine(30,-40,30,-20);
		line3.setLine(30,-40,50,-40); //upper
		line4.setLine(50,-20,30,-20); //lower
		//FOV 
		//sight = (float)bodyW * maxSpeed * .15f;
		sight = 130;
		fov = new Arc2D.Double(-sight, -sight, sight * 2, sight * 2, -55, 110, Arc2D.PIE);
		
		deathAnmi1.setFrame(0,0,100,y);
		deathAnmi2.setFrame(-80,-50,y,100);		
		broken1.setFrame(20,20,30,40);
		
		
	}
	
	public void setOutline() {    //set outline
		outline = new Area();
		outline.add(new Area(body));
		outline.add(new Area(tail1));
		outline.add(new Area(tail2));
		outline.add(new Area(line1));
		outline.add(new Area(line2));
		outline.add(new Area(line3));
		outline.add(new Area(line4));
		outline.add(new Area(hand));
	}
	
	public void drawFish(Graphics2D g2) {     //override
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //画面撕裂 
		AffineTransform at = g2.getTransform();
		g2.translate(loc.x, loc.y);   
		//System.out.println(loc.x);
		g2.scale(scale, scale);
		float angle = vel.heading(); 
		g2.rotate(angle);
		if (vel.x < 0) g2.scale(1, -1);
		setAttributes();
		setOutline();
		g2.setColor(changeColor);  
		//body
		if(state == Sick) {
			g2.setColor(new Color(57, 94, 49));
		}else if(state == Dead) {
			g2.setColor(new Color(42, 59, 39));
			if(scale > 0) 
			this.scale-=0.009;
			if(deathAnmiTime>50) {
				this.loc.y-=4;
				double randomAngle=(Math.random()*Math.PI)+1;
				g2.rotate(randomAngle);
				g2.setColor(Color.red);
			}
		}
		g2.fill(body);
		//mouth	 
		g2.draw(face);
		g2.setColor(new Color(227, 93, 100)); 
		g2.fill(mouth);
		//eyes
		g2.setColor(new Color(0,0,0));
		g2.fill(eyes);
		
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
		//special weapons		
		g2.setColor(new Color(0,0,0));
		g2.fill(hand);
		//line can not use fill?? wtf???
		g2.setColor(Util.randomColor());
		g2.draw(line1);
		g2.draw(line2);
		g2.draw(line3);
		g2.draw(line4);
		g2.setColor(new Color(0,0,0));
		g2.fill(tail1);
		g2.fill(tail2);		
		if(state == Dead) {
			if(y<170) {
				y+=2.5;
			}

			g2.setColor(Util.randomColor());
			g2.fill(deathAnmi2);
		}
		g2.setTransform(at);
		
//		g2.setColor(Color.PINK);
//		g2.draw(getOutline().getBounds2D());
	}
	
	public void kill(Fish other ,ArrayList<Fish> fishes) {
		fishes.remove(other);
		energy += vel.mag()*other.getSize()*0.01;
		find=false;
		//energy += NoobFish.getSize()*10;
	}
	
	
}
