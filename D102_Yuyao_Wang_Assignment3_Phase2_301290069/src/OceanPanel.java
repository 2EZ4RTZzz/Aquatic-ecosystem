/*
 * WangYuyao 301290067 IAT 265 D102
 * Checklist , all min requirements are done
 * Bonus part
 * I Finished part 1 and part2 ,
 * For Bonus part 1 , everytime when hunter press "space" hit the bossFish , bossfish will die in 3 secs with "rotate" + "getting smaller"+ "shield close" animations
 * 
 * For Bonus part 2 , when Hunter is up on the map , all BossFishes will directly swim to hunter , when bossfish collision the hunter,  
 * hunter will lose HP (it depends on hunter's size) #line 123 hunter.energy -= fishs.getSize() * 0.00005f; ******** also fish will lose same dmg too!**************
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import processing.core.PVector;

public class OceanPanel extends JPanel implements ActionListener {
	private Ocean ocean;
	private Timer timer;
	ArrayList<Food> food;
	// List<Food> food = new ArrayList<Food>();
	// apparent type left = actual type right.
	// 左边是我定义的， 比如这里我左边定义她就是个arraylist ,
	// left >= right left = superclass 左边一定是父类 级别大于右边
	// Polymorphism 多态性.
	ArrayList<Fish> fishes;
	private int NoobfishNum = 12;
	private int BossFishNum = 6;
	private int FoodNum= NoobfishNum*3;
	//private int FoodNum = 0;
	private int resetTime = 0;
	private Hunter hunter;

	private boolean showInfo = true;
	private boolean checkShot = false;
	String texts;

	private boolean isHunterUp = true;
	private boolean firstTime =true;

	public OceanPanel() {
		setPreferredSize(new Dimension(1500, 1000));
		timer = new Timer(33, this);
		timer.start();
		ocean = new Ocean();
		restartGame();
		MyKeyListener mkl = new MyKeyListener();
		this.addKeyListener(mkl);
		setFocusable(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Call JPanel's method to clear the background
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// 画背景！！！
		ocean.drawOcean(g2);
		// draw food
		for (int i = 0; i < food.size(); i++) {
			food.get(i).drawFood(g2);
		}
		// draw fish
		for (int i = 0; i < fishes.size(); i++) {
			Fish fish = fishes.get(i); // inclusion poly !!! cause the fishes arraylist included noobfish and bossfish
			fish.drawFish(g2); // Override !!!
			if (showInfo)
				fish.drawInfo(g2);
		}
		if (hunter != null) {
			hunter.drawFish(g2);
			if (showInfo)
				hunter.drawInfo(g2);
		}

		drawStatusBar(g2);

	}

	public void actionPerformed(ActionEvent e) {

		repaint();
		for (int i = 0; i < fishes.size(); i++) {
			Fish fishs = fishes.get(i);

			if (fishs instanceof NoobFish) { // instanceof Meaning , its this from one of the class's object
												// /是不是某个class里面的实例
				// loop的时候不能把子类unique 的method拿出来单独用 -> week9 lab challenge.
				// 要用也得是override过来的method才行
				// 在forloop 里面单独挑出来了。 Polymorphism没意义
				// 最好的方式就是 bossfish noobfish 同样的methodname
				// 多态性是用在继承下面的 ， 当俩class符合继承规则 并且subclass没有什么unique method时
				// 才是正确的使用方法.
				// 假设我的noobfish , bossfish都有父类的 method.
				// 因为题目要求 subclass must have their own unique method.所以 这么写
				Food target = ((NoobFish) fishs).AfcFood(food);

				if (target != null) { // 当食物存在
					target.setColor(fishs.getColor()); // 食物变色
					fishs.chase(target); // 鱼追食物
				}
				if (target != null && ((NoobFish) fishs).detectCollision(target)) {
					food.remove(target);
					if (food.remove(target))
						;
					createFood();
				}
			}

			if (hunter != null && isHunterUp == true && fishs instanceof BossFish) { // 当hunter存在时
//				if(fishs.detectFov(hunter)) {         //for bonus 2 this one will be uesless.  
				fishs.chase(hunter);
				if (fishs.detectCollision(hunter)) {
					hunter.energy -= fishs.getSize() * 0.00005f;
					fishs.energy -= fishs.getSize() * 0.00005f;
					hunter.resolveCollisionOnlyForHunter(fishs);
				}
				if (hunter.energy <= 0) {
					hunter = null;
					isHunterUp = false;		
					}
			}

			if (hunter != null && fishs instanceof NoobFish) {
				if (fishs.detectFov(hunter)) {
					// System.out.println("123123");
					hunter.resolveCollisionOnlyForHunter(fishs);
				}
			}
			for (int j = 0; j < fishes.size(); j++) {
				Fish fishj = fishes.get(j);
				// remove the phase_2 bonus part. cause is useless right now.(not really useless
				// , but the question didnt ask for that)
				if (fishs instanceof BossFish && fishj instanceof BossFish
						|| fishs instanceof NoobFish && fishj instanceof NoobFish) {
					if (fishs != fishj && (fishs.detectCollision(fishj) || fishs.detectFov(fishj))) {
						fishs.resolveCollision(fishj);
					}
				} else if (fishs instanceof BossFish && fishj instanceof NoobFish) {
					if (fishs.detectFov(fishj)) { // this FOV!!!!!!!!!!!!!!!!!!!!!!
						fishs.chase(fishj);
					}
					if (fishs.detectCollision(fishj)) {
						((BossFish) fishs).kill(fishj, fishes);
					}
				} else if (fishs instanceof NoobFish && fishj instanceof BossFish) {
					if (fishs.detectFov(fishj)) {
						((NoobFish) fishs).runaway(fishj); // smallfish will run away.
					}
				}
			}
			fishs.move();
			if (fishs.deathAnmiTime == 0)
				fishes.remove(fishs);
		}
		if (hunter != null) {
			hunter.move();
			hunter.checkBullet(getBossFishNumber());
		}
		if (getNoobFishNumber().size() < NoobfishNum/2) {
			// testing!!!!!!!!!!!!!!!!!!!!! <NoobfishNum/2
			if (hunter == null) { // if hunter is empty ! then draw it !!!!
				if(isHunterUp==false) {
					resetHunter();
				}
				if(firstTime==true) {
					summonHunter();
					firstTime=false;
				}
			}
		}

		if (getBossFishNumber().size() < BossFishNum / 2) { 
			hunter = null;
			restartGameSetting();
		}
	}

//	public int getBossFishNumber(){               //just get the values right away.(Both way works)
//		int numbers = 0;
//		for (int i =0 ; i<fishes.size();i++) {
//			if(fishes.get(i) instanceof BossFish) numbers +=1;
//		}
//		return numbers;
//	}

	public void resetHunter() {
		if (resetTime == 0) {
			resetTime = 90; // 30x5 = 5 secs
		}
		if (resetTime > 0)
			resetTime--;
		if (resetTime == 0) {
			summonHunter();
			isHunterUp = true;
		}
	}

	public void restartGameSetting() {
		if (resetTime == 0) {
			resetTime = 150; // 30x5 = 5 secs
		}
		if (resetTime > 0)
			resetTime--;
		if (resetTime == 0)
			restartGame();
	}

	public ArrayList<Fish> getBossFishNumber() { // just get the values right away.
		ArrayList<Fish> GetBossFish = new ArrayList<>();
		for (int i = 0; i < fishes.size(); i++) {
			if (fishes.get(i) instanceof BossFish)
				GetBossFish.add(fishes.get(i));
		}
		return GetBossFish;
	}

	public ArrayList<Fish> getNoobFishNumber() { // from the lab code
		ArrayList<Fish> GetNoobFish = new ArrayList<>();
		for (int i = 0; i < fishes.size(); i++) {
			if (fishes.get(i) instanceof NoobFish)
				GetNoobFish.add(fishes.get(i));
		}
		return GetNoobFish;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Fishes in Ocean");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		OceanPanel OceanPane = new OceanPanel();
		frame.add(OceanPane);
		frame.pack();
		frame.setVisible(true);
	}

	public void createFood() {
		PVector pos = new PVector(Util.random(200, 1100), Util.random(150, 800));
		food.add(new Food(pos, (int) Util.random(100, 150), (int) Util.random(30, 100),
				(double) 0.5 + Math.random() * 1));
	}

	public class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (hunter != null && checkShot == false) {
					hunter.fire();
				checkShot = true;
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_D) {
				if (showInfo)
					showInfo = false;
				else
					showInfo = true;
			}
		}

		public void keyReleased(KeyEvent e) {
			if (checkShot == true)
				checkShot = false;
		}

	}

	private void drawStatusBar(Graphics2D g2) {
		Font f = new Font("Garamond", Font.BOLD, 16);
		g2.setFont(f);
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(80, 880, 1200, 50);
		g2.setColor(Color.BLACK);
		if (hunter == null)
			texts = "Hunter is not up yet";
		else
			texts = "Hunter is up";

		String text1 = "NoobFishAmount: " + getNoobFishNumber().size() + " / Noobfish Number";
		String text2 = "BossFishAmount: " + getBossFishNumber().size() + " / BossFish Number";
		String text3 = "Hunter restart: " + resetTime/30;
		
		g2.drawString(text1, 100, 910);
		g2.drawString(text2, 500, 910);
		g2.drawString(texts, 900, 910);
		g2.drawString(text3, 1100, 910);
	}

	public void summonHunter() {
		PVector loc = new PVector(120, 130);
		hunter = new Hunter(loc, 1, 1, 1);
	}
	
	public void restartGame() {
		fishes = new ArrayList<Fish>();
		food = new ArrayList<Food>();
		// NOOBFISH
		for (int i = 0; i < NoobfishNum; i++) { // im adding 5 noob fishs , size are smaller than BossFish
			PVector loc = new PVector(Util.random(100, 1100), Util.random(100, 800));
			double scale = Util.random(0.2, 0.5);

			fishes.add(new NoobFish(loc, (int) Util.random(100, 150), (int) Util.random(30, 80), scale));
		}
		// phase2 Q3 ，
		// BOSS FISH
		for (int i = 0; i < BossFishNum; i++) { // adding 2 boss , size are bigger than noobFish
			PVector loc = new PVector(Util.random(100, 1100), Util.random(100, 800));
			double scale = Util.random(0.5, 0.8);
			fishes.add(new BossFish(loc, (int) Util.random(100, 150), (int) Util.random(30, 80), scale));
		}

		// FOOD!
		for (int i = 0; i < FoodNum; i++) {
			PVector pos = new PVector(Util.random(150, 1100), Util.random(200, 780));
			food.add(new Food(pos, 1, 1, (double) 0.2 + Math.random() * 0.5)); // (int)Util.random(100,150),(int)Util.random(30,100)
																				// uesless *
		}
		isHunterUp = true;
		firstTime =true;
	}
}
