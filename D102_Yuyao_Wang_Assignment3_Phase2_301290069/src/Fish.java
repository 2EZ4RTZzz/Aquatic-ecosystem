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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import processing.core.PVector;
import java.awt.Polygon;

public abstract class Fish {   //super class , 
	protected PVector loc,vel;   //keep 
	protected double bodyW,bodyH,scale;  //keep 
	protected Color changeColor;   //keep it
	protected int countThreeSecs;      //keep it
	protected float maxSpeed,sight;  //keepit 
	protected Arc2D.Double fov;  //keep 
	protected Area outline;      //keep it
	
	protected Food afcFood;  //not sure yet
	protected boolean find;
	protected boolean runaway;
	
	protected final int Dead = 0;
	protected final int Sick = 1;
	protected final int Full =2; //default
	protected final int OverWeight =3;
	
	protected final int SuperFull = 700;
	
	protected int state = Sick;
	protected float energy = SuperFull;
	
	protected String Fishname;
	
	protected int deathAnmiTime;
	
	// Constructor
	public Fish(PVector loc, int w, int h, double sc) {
		this.loc = loc;
		bodyW = w;
		bodyH = h;
		scale = sc;
		this.maxSpeed = Util.random(3, 5);
//		this.maxSpeed = 0;
		this.vel = Util.randomPVector(maxSpeed);
		this.changeColor = new Color((int)(1+Math.random()*255),(int)(1+Math.random()*255),(int)(1+Math.random()*255));
		deathAnmiTime =-1;

	}
	
	//abstract methods
	public abstract void setAttributes();
	
	public abstract void setOutline();    //set outline
	
	public abstract void drawFish(Graphics2D g2);
	
	
	
	
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


	public double getSize() {
		return bodyW*bodyH*scale;
	}
	
	public boolean detectCollision(Fish otherFish) { //鱼撞鱼
		boolean hit = false;
		if (getOutline().intersects(otherFish.getOutline().getBounds2D())
				&& otherFish.getOutline().intersects(getOutline().getBounds2D()))
			hit = true;
		return hit;
	}
	
	public boolean detectFov(Fish otherFish) {   //FOV hit another fish's boundray box
		boolean hit = false;
		if (getFOV().intersects(otherFish.getOutline().getBounds2D())) hit = true; return hit;
	}
	
	public void resolveCollision(Fish otherFish){   //this one is for the same lvl fish push away method.
		float angle = (float) Math.atan2(loc.y - otherFish.loc.y, loc.x - otherFish.loc.x);		
		//if current bug smaller, turn it away by the angle
		if(scale < otherFish.scale) {	
			vel = PVector.fromAngle(angle);
			vel.mult(maxSpeed);
			if(countThreeSecs == 0 ) {
				countThreeSecs=90;
			}
			//afcFood=food;   assignment_phase2_bonus
		}
		else {
			//Otherwise send the otherBug away in the opposite direction: angle+PI
			otherFish.vel = PVector.fromAngle(angle+(float)Math.PI);
			otherFish.vel.mult(maxSpeed);
			//otherFish.afcFood=food;            assignment_phase2_bonus
			if(otherFish.countThreeSecs ==0 )  otherFish.countThreeSecs=90;
		}
	}
	
	public void resolveCollisionOnlyForHunter(Fish otherFish){
		float angle = (float) Math.atan2(loc.y - otherFish.loc.y, loc.x - otherFish.loc.x);		
		otherFish.vel = PVector.fromAngle(angle+(float)Math.PI);
		otherFish.vel.mult(maxSpeed);
	}
	
	public void chase(Food target) {  // overloading this for the foods.
		if (countThreeSecs > 0) return;
		PVector path = PVector.sub(target.getPos(), loc);
		vel = path.limit(maxSpeed); //limit 值限制住多少多少， 每次执行chase都会让值^值， 所以limit让他限制在maxspeed之内
	}
	public void chase(Fish otherFishs) {  // same method name ,different parameters -> overloading
		PVector path = PVector.sub(otherFishs.getPos(), loc);
		//vel.normalize().mult(30);
		vel = path.limit(maxSpeed); //limit 值限制住多少多少， 每次执行chase都会让值^值， 所以limit让他限制在maxspeed之内
		find=true;		
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
	
	public ArrayList<Food> RemoveOldFoods(ArrayList<Food> food){  //phase2_bonus
		ArrayList<Food> newFood = new ArrayList<Food>();
		for(int i = 0 ;  i < food.size(); i++) {
			if(afcFood != food.get(i)) {
				newFood.add(food.get(i));
			}
		}
		return newFood;
	}

	public void move() {
		if(countThreeSecs>0) {
			countThreeSecs--;
			if(countThreeSecs == 0) {
				runaway=false;
			}
		}
		PVector wallSteerAccel = wallPushForce().div((float)scale);
		//float maxSpeed = vel.mag();
		vel.add(wallSteerAccel);
		
		if(find) {
			vel.normalize().mult(maxSpeed*2);
		}
		else if(runaway) {
			vel.normalize().mult(maxSpeed*1.5f);
		}
		else
			if(state==Sick) {
				vel.normalize().mult(maxSpeed/2);
			}else {
			vel.normalize().mult(maxSpeed);
			}
	//	System.out.println(state);
		energy -= vel.mag()*scale*0.5;
		if(energy < 0 ) {
			energy = 0;
		}
		if(energy>800&&energy<950) {
			state = OverWeight;                //BUG1 full的时候会变色
		}else if (energy > SuperFull*0.7 && energy<=800) {
			state = Full;
		}else if(energy < SuperFull*0.3 && energy>0) {
			state = Sick;
		}else if(energy==0){
			state = Dead;
		}
		
		//System.out.println(state);
		if(state == OverWeight) {
			//scale+= (energy-SuperFull)*0.000001f*scale;
			scale+= 0.003*scale;
		}else if(state == Sick) {
			vel.normalize().mult(maxSpeed/2);
		}else if(state == Dead){
			if(deathAnmiTime == -1) {
				deathAnmiTime = 90;
			}
			vel.mult(0);
		}
		if(deathAnmiTime > 0) {
			deathAnmiTime--;
		}
		loc.add(vel);
		}
	
	public void drawInfo(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		g2.translate(loc.x, loc.y);
		
		String name ="Name: "+ String.format("%.2f", Fishname);
		String size ="Size: "+ String.format("%.2f", scale);
		String speed ="Speed: "+ String.format("%.2f", vel.mag());
		String Energy ="Energy: "+ String.format("%.2f", energy);
		String FishStage = "stage: " +state;
		
		Font f = new Font("Arial", Font.BOLD, 13);
		FontMetrics metrics = g2.getFontMetrics(f);//get font's width/ height and spacing something like that
		// its like setting the word's width/height...etc
		// 管字母的w/h/spacing. 
		
		float textWidth = metrics.stringWidth(Energy);
		float textHeight = metrics.getHeight();
		float margin = 1 , spacing =6;  
		//margin 文字和边框的距离 spacing 间距
		
		g2.setColor(new Color(255,255,255,60));
		g2.fillRect((int) (-textWidth/2 - margin),(int)(-bodyH * scale * .75f - textHeight *2f - spacing*4f - margin *2f)-40,
				(int)(textWidth+margin*2f),(int)(textHeight*2.5f+ spacing *4f+margin *1f)+40);
		g2.setColor(Color.blue);
		g2.drawString(this.FishType(), -metrics.stringWidth(this.FishType()) / 2, (float)(-bodyH *scale*0.75f - margin - (textHeight+spacing)*4f));
		g2.setColor(Color.black);
		g2.drawString(size, -textWidth/2 , (float) (-bodyH *scale*0.75f - margin - (textHeight+spacing)*2f));
		g2.drawString(speed, -textWidth/2 , (float) (-bodyH *scale*0.75f - margin - (textHeight+spacing)*1f));
		g2.drawString(Energy, -textWidth/2 , (float) (-bodyH *scale*0.75f - margin));
		g2.drawString(FishStage, -textWidth/2 , (float) (-bodyH *scale*0.75f - margin - (textHeight+spacing)*3f));
		
		g2.setTransform(at);	
	}
	
	private String FishType() {
		String type = "unknown animal";
		if (this instanceof BossFish)
			type = "BossFish";
		else if (this instanceof NoobFish)
			type = "NoobFish";
		return type;
		}
	}
