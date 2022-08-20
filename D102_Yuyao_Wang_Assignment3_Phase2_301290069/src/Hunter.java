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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;

import processing.core.PVector;

public class Hunter extends Fish{
	private Double body,body2,Death1;
	private Rectangle2D.Double body3;
	private ArrayList<Bullet> Bullets = new ArrayList<>();

	public Hunter (PVector loc, int w, int h, double sc) {
		super(loc,w,h,sc);
		
		
		body = new Ellipse2D.Double();
		Death1= new Ellipse2D.Double();
		body2 = new Ellipse2D.Double();
		body3= new Rectangle2D.Double();
		vel = new PVector(0,3);
		energy=5;
		Fishname="Hunter";
		setAttributes();
		setOutline();
	}
	public void setAttributes() {  
		body.setFrame(-40, -40, 80, 80);
		body2.setFrame(-30, -30, 60, 60);
//		body3.setFrame(-55, -25, 130, 50);
//		body4.setFrame(-55, -25, 130, 50);
		Death1.setFrame(-20,-20,40,40);
		body3.setFrame(-5,-5,60,10);
		
	}
	
	public void setOutline() {    //set outline
		outline = new Area();
		outline.add(new Area(body));
	}
	public void drawFish(Graphics2D g2) {    //override
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //画面撕裂 
		AffineTransform at = g2.getTransform();
		setAttributes();
		setOutline();
		g2.translate(loc.x, loc.y);   
		g2.setColor(changeColor);  
		//body  	
		
		g2.fill(body);
		g2.setColor(Color.red);
		g2.fill(body2);
		g2.setColor(Color.blue);
		g2.fill(body3);
		g2.setTransform(at);
//		g2.setColor(Color.PINK);
//		g2.draw(getOutline().getBounds2D());
		
		for(int i = 0 ; i < Bullets.size();i++) {
			Bullets.get(i).drawBullet(g2);  //press once = 1 bullet
		}
	}
	public void fire() {
		PVector pos = new PVector(loc.x,loc.y);  //loc is the location , pos is just for here
		PVector vel = new PVector(3,0);
		Bullets.add(new Bullet(pos,vel)); //实例化
	}
	
	public void move() {
		loc.add(this.vel);
		checkWall();
	}
	
	public void checkWall() {
		if(loc.y+bodyH > 850 || loc.y - bodyH < 120) {
			vel.y = -vel.y;
		}
		
	}
	
	public void checkBullet(ArrayList<Fish> BossFish) {
		Fish findBestFish = FindBossFish(BossFish);
		if(findBestFish != null) {
			for(int i = 0 ; i <Bullets.size();i++) {
				Bullet bulletHit = Bullets.get(i);
				if(bulletHit.hit(findBestFish)) {
					findBestFish.deathAnmiTime = 90;  //times out = dead . LOL trick way
					if(deathAnmiTime==0) {				
						BossFish.remove(findBestFish);  //remove fish
					}else {
						findBestFish.energy=0;
						deathAnmiTime--;
					}
					//BossFish.remove(findBestFish);   //remove the highest energy fish from the BossFishArrayList.	
					Bullets.remove(bulletHit);    //remove bullet	
				}
				PVector path = PVector.sub(findBestFish.loc,bulletHit.getPos());
				float angle =path.heading();
				bulletHit.setVel (PVector.fromAngle(angle).normalize().mult(10));
				bulletHit.move();
			}
		}
	}
	
	public Fish FindBossFish(ArrayList<Fish> BossFish) {
		if(BossFish.size() == 0) return null;
			Fish RealBossFish = BossFish.get(0);
			
			for(int i = 0 ;i <BossFish.size();i++) {
				if(BossFish.get(i).energy>RealBossFish.energy) {
					RealBossFish = BossFish.get(i);
				}
			}
			return RealBossFish;
	}
	
	public void drawInfo(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		g2.translate(loc.x, loc.y);
		
		//String name ="Hunter "+ String.format("%.2f", Fishname);
		String Energy ="Health "+ String.format("%.2f", energy);
		
		String Name = "    Hunter";
		
		Font f = new Font("Arial", Font.BOLD, 12);
		FontMetrics metrics = g2.getFontMetrics(f);//get font's width/ height and spacing something like that
		// its like setting the word's width/height...etc
		// 管字母的w/h/spacing. 
		
		float textWidth = metrics.stringWidth(Energy);
		float textHeight = metrics.getHeight();
		float margin = 1 , spacing =6;  
		//margin 文字和边框的距离 spacing 间距
		
		g2.setColor(new Color(255,255,255,60));
		g2.fillRect((int) (-textWidth/2 - margin)-5,(int)(-bodyH * scale * .75f - textHeight *2f - spacing*4f - margin *2f)-40,
				(int)(textWidth+margin*2f)+5,(int)(textHeight*2.5f+ spacing *4f+margin ));
		g2.setColor(Color.black);
		g2.drawString(Energy, -textWidth/2 , (float) (-bodyH *scale*0.75f - margin-(textHeight+spacing)*2f));
		g2.setColor(Color.blue);
		g2.drawString(Name, -textWidth/2, (float) (-bodyH *scale*0.75f - margin-(textHeight+spacing)*3f));
		g2.setTransform(at);	
	}
	
	
	
	
}

