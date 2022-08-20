
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;


public class Ocean {
	public final static int Ocean_X = 80;
	public final static int Ocean_Y = 80;
	public final static int Ocean_W = 1200;  //1000wEEK1   WEEK2 500
	public final static int Ocean_H = 800;  //450 WEEK1   WEEK2 300
	private int fly1,fly2,fly3;
	
	private Rectangle2D.Double background,bigCase;
	private Line2D.Double caseLine,line1,line2,line3,line4,line5;
	private Ellipse2D.Double caseBall,stone1,stone2,stone3,shall,shall1;
	private Arc2D.Double leftBackground,grass1,grass2,grass3,grass4,grass5,grass6,grass7,grass8,grass9,shallArc;
	private Arc2D.Double rightBackground;
	private Ellipse2D.Double Bubble1,Bubble2,Bubble3,Bubble4,Bubble5,Bubble6,Bubble7,Bubble8,Bubble9,Bubble10;
	
	public static Line2D.Double rightEdge;
	public static Line2D.Double leftEdge;
	public static Line2D.Double topEdge;
	public static Line2D.Double bottomEdge;
	
	public Ocean() {
		fly1=550;
		fly2=430;
		fly3=450;
		background = new Rectangle2D.Double();	
		leftBackground = new Arc2D.Double();
		bigCase = new Rectangle2D.Double();	
		caseLine = new Line2D.Double();
		caseBall = new Ellipse2D.Double();
		stone1 = new Ellipse2D.Double();
		stone2 = new Ellipse2D.Double();
		stone3 = new Ellipse2D.Double();
		grass1 = new Arc2D.Double();
		grass2 = new Arc2D.Double();
		grass3 = new Arc2D.Double();
		grass4 = new Arc2D.Double();
		grass5 = new Arc2D.Double();
		grass6 = new Arc2D.Double();
		grass7 = new Arc2D.Double();
		grass8 = new Arc2D.Double();
		grass9 = new Arc2D.Double();
		shall = new Ellipse2D.Double();
		shallArc = new Arc2D.Double();
		shall1 = new Ellipse2D.Double();
		line1 = new Line2D.Double();
		line2 = new Line2D.Double();
		line3 = new Line2D.Double();
		line4 = new Line2D.Double();
		line5 = new Line2D.Double();
		rightBackground = new Arc2D.Double();
		Bubble1 = new Ellipse2D.Double();
		Bubble2 = new Ellipse2D.Double();
		Bubble3 = new Ellipse2D.Double(); 
		Bubble4 = new Ellipse2D.Double();
		Bubble5 = new Ellipse2D.Double();
		Bubble6 = new Ellipse2D.Double();
		Bubble7 = new Ellipse2D.Double();
		Bubble8 = new Ellipse2D.Double();
		Bubble9 = new Ellipse2D.Double();
		Bubble10 = new Ellipse2D.Double();
		
		
		rightEdge = new Line2D.Double(Ocean_X+ Ocean_W, Ocean_Y, Ocean_X+Ocean_W, Ocean_Y + Ocean_H);
		leftEdge = new Line2D.Double(Ocean_X, Ocean_Y, Ocean_X, Ocean_Y+Ocean_H);
		topEdge = new Line2D.Double(Ocean_X, Ocean_Y, Ocean_X+Ocean_W, Ocean_Y);
		bottomEdge = new Line2D.Double(Ocean_X, Ocean_Y+Ocean_H, Ocean_X+Ocean_W, Ocean_Y + Ocean_H);
	}
	
	public void setAttributes() {
		background.setFrame(Ocean_X, Ocean_Y, Ocean_W, Ocean_H);
		leftBackground.setArc(-245,680,650,400, -270, -90,Arc2D.PIE);
		bigCase.setFrame(150, 760, 100, 100);
		caseLine.setLine(150, 790, 250, 790);
		caseBall.setFrame(190, 780, 20, 20);
		stone1.setFrame(350, 800, 150, 80);
		stone2.setFrame(480, 840, 110, 40);
		stone3.setFrame(570, 840, 150, 40);
		grass1.setArc(450,700,100,200,180,-90,Arc2D.OPEN);
		grass2.setArc(430,700,100,200,180,-90,Arc2D.OPEN);
		grass3.setArc(410,700,100,200,180,-90,Arc2D.OPEN);
		grass4.setArc(380,710,100,200,180,-90,Arc2D.OPEN);
		grass5.setArc(370,710,100,200,180,-90,Arc2D.OPEN);
		grass6.setArc(360,720,100,200,180,-90,Arc2D.OPEN);
		grass7.setArc(345,700,100,200,-270,-90,Arc2D.OPEN);
		grass8.setArc(315,720,100,200,-250,-90,Arc2D.OPEN);
		grass9.setArc(328,720,100,200,-230,-90,Arc2D.OPEN);
		shall.setFrame(600, 818, 60, 30);
		shallArc.setArc(600, 800, 60,60,0,180,Arc2D.OPEN);
		shall1.setFrame(600,828,60,20);
		line1.setLine(610, 830, 605, 812);
		line2.setLine(623, 828, 621, 800);
		line3.setLine(635, 828, 635, 800);
		line4.setLine(645, 829, 648, 807);	
		line5.setLine(655, 832, 660, 823);	
		rightBackground.setArc(680, 730, 1200, 300, -270, 90,Arc2D.PIE);
		Bubble1.setFrame(700, fly1, 20, 20);
		Bubble2.setFrame(200,fly3+30, 20, 20);
		Bubble3.setFrame(800, fly2, 20, 20);
		Bubble4.setFrame(950, fly3, 30, 30);
		Bubble5.setFrame(950, fly1-20, 40, 40);
		Bubble6.setFrame(650, fly2+50, 20, 20);
		Bubble7.setFrame(450, fly3+50, 20, 20);
		Bubble8.setFrame(730, fly1-20, 30, 30);
		Bubble9.setFrame(1000, fly2, 20, 20);
		Bubble10.setFrame(310, fly3+50, 25, 25);
	}
	
	public void drawOcean(Graphics2D g2) {	
		AffineTransform at = g2.getTransform();
		setAttributes();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//Background color
		g2.setColor(new Color(126, 213, 252));  //Background color
		g2.fill(background);
		g2.setColor(new Color(0, 0, 0));
		//left side background
		g2.setColor(new Color(252, 165, 3));
		g2.fill(leftBackground);
		//case
		g2.setColor(new Color(133, 108, 77));
		g2.draw(bigCase);
		g2.fill(bigCase);
		g2.setColor(new Color(38, 37, 34));
		g2.draw(caseLine);
		g2.fill(caseBall);
		//right side
		g2.setColor(new Color(255, 111, 0));
		g2.fill(stone1);
		g2.fill(stone2);
		g2.fill(stone3);
		//Sea grass
		g2.setColor(new Color(0, 122, 63));
		g2.draw(grass1);
		g2.draw(grass2);
		g2.draw(grass3);
		g2.draw(grass4);
		g2.draw(grass5);
		g2.draw(grass6);
		g2.draw(grass7);
		g2.draw(grass8);
		g2.draw(grass9);
		//shall
		g2.setColor(new Color(199, 235, 230));
		g2.fill(shall);
		g2.draw(shall);
		g2.fill(shallArc);
		g2.setColor(new Color(186, 191, 190));
		g2.draw(shall1);
		g2.fill(shall1);
		g2.setColor(Color.black);
		g2.draw(line1);
		g2.draw(line2);
		g2.draw(line3);
		g2.draw(line4);
		g2.draw(line5);
		//Bubble
		if(fly1>0) {
			fly1-= (int)1+Math.random()*2;
			if(fly1<100) {
				fly1=800;
			}
		} 
		if(fly2>0) {
			fly2-= (int)1+Math.random()*3;
			if(fly2<80) {
				fly2=700;
			}
		} 
		if(fly3>0) {
			fly3-= (int)1+Math.random()*4;
			if(fly3<90) {
				fly3=750;
			}
		} 
		g2.setColor(new Color(37, 184, 250));
		g2.draw(Bubble1);
		g2.setColor(new Color(78, 162, 181));
		g2.draw(Bubble2);
		g2.draw(Bubble3);		
		g2.draw(Bubble4);
		g2.draw(Bubble5);
		g2.setColor(new Color(0, 108, 171));
		g2.draw(Bubble6);
		g2.draw(Bubble7);
		g2.setColor(new Color(26, 158, 235));
		g2.draw(Bubble8);
		g2.draw(Bubble9);
		g2.draw(Bubble10);
		//right side background
		g2.setColor(new Color(166, 129, 28));
		g2.fill(rightBackground);
		g2.setTransform(at);
		
	}
}
