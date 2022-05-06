package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.*;
import edu.hitsz.publisher.BombPublisher;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends JPanel {

    //game单例
    private Game game;
    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    private final List<AbstractEnemy> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> Props;

    private EnemyFactory enemyFactory;
    private BombPublisher bombPublisher;

    protected int enemyMaxNumber = 5;

    private boolean gameOverFlag = false;
    private static int score = 0;
    protected int scoreIncrease = 10;
    private int time = 0;
    private boolean bossFlag = false;
    public static String gameModel = "easy";
    protected int mobPercent = 3;
    protected int bossPercent = 400;
    private int enemyShootCount = 0;
    private int heroShootCount = 0;
    protected int enemyShootTime = 3;
    protected int heroShootTime = 1;
    private int mobPercentIncrease = 0;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;
    private MusicThread[] musicThread = new MusicThread[2];
    public static boolean bulletMusicFlag = false;

    protected void resetScore(){
        score = 0;
    }

    public Game() {
        //英雄机单例模式
        heroAircraft = HeroAircraft.getHeroAircraft();
        bombPublisher = BombPublisher.getBombPublisher();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        Props = new LinkedList<>();
        this.initGame();

        //Scheduled 线程池，用于定时任务调度
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        };
        executorService = new ScheduledThreadPoolExecutor(1, threadFactory);

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                //生成敌机
                enemyGenerate();
                // 飞机射出子弹
                shootAction();
                if(!"easy".equals(gameModel) && mobPercentIncrease > 10){
                    mobPercent += 1;
                    EliteFactory.eliteHp += 10;
                    MobFactory.mobHp += 10;
                    System.out.println("精英敌机生成概率："+ 1/(double)mobPercent);
                    System.out.println("精英敌机血量提升："+ EliteFactory.eliteHp/(double)80);
                    System.out.println("普通敌机血量提升："+ MobFactory.mobHp/(double)60);
                    mobPercentIncrease = 0;
                }else{mobPercentIncrease++;}
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            //道具移动
            propMoveAction();

            // 撞击检测
            crashCheckAction();

            cycleMusic();
            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                new MusicThread("src/videos/game_over.wav").start();
                executorService.shutdown();
                MusicThread.allRunFlag = false;
                gameOverFlag = true;
                System.out.println("Game Over!");
                synchronized (Main.changeObject){
                    Main.changeObject.notify();
                }
            }

        };

        musicThread[0] = new MusicThread("src/videos/bgm.wav");
        musicThread[0].start();
        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */

        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    public void increaseScore(){
        score += scoreIncrease;
    }

    //敌机生成 包括头目敌机 精英敌机 普通敌机
    private void enemyGenerate(){
        if (enemyAircrafts.size() < enemyMaxNumber || score % 200 == 0) {
            // 头目敌机 精英敌机 工厂模式生成
            if(score != 0 && score % bossPercent == 0 && !bossFlag && !"easy".equals(gameModel)){
                enemyAircrafts.clear();
                enemyBullets.clear();
                if("hard".equals(gameModel)){
                    BossFactory.bossHp += 200;
                    System.out.println("头目敌机血量倍率："+BossFactory.bossHp/(double)1000);
                }
                enemyFactory = new BossFactory();
                bossFlag = true;
                musicThread[1] = new MusicThread("src/videos/bgm_boss.wav");
                musicThread[1].start();
            }else if(getRandomInt(1, mobPercent) == 1){
                enemyFactory = new EliteFactory();
            }else{
                enemyFactory = new MobFactory();
            }
            AbstractEnemy enemyAircraft = (AbstractEnemy) enemyFactory.createEnemy();
            if(!(enemyAircraft instanceof BossEnemy)){
                bombPublisher.subscribe(enemyAircraft);
            }
            enemyAircrafts.add(enemyAircraft);
        }
    }

    private void cycleMusic(){
        if(musicThread[0] == null || !musicThread[0].isAlive()){
            musicThread[0] = new MusicThread("src/videos/bgm.wav");
            musicThread[0].start();
        }
        if(bossFlag && (musicThread[1] == null || !musicThread[1].isAlive()) ){
            musicThread[1] = new MusicThread("src/videos/bgm_boss.wav");
            musicThread[1].start();
        }
    }

    public static String getGameModel(){
        return gameModel;
    }
    public static int getScore(){
        return score;
    }

    //抽象方法，用于构建难度模板
    protected abstract void initGame();

    /**
     * 产生int型随机数
     * 范围  [min,max]
     */
    private int getRandomInt(int min,int max){
        return min + (int)(Math.random()*(max-min+1));
    }

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // TODO 敌机射击
        if(enemyShootCount % enemyShootTime == 0){
            for(AbstractAircraft enemyAircraft : enemyAircrafts) {
                if(bulletMusicFlag) {
                    new MusicThread("src/videos/bullet.wav").start();
                }
                List<BaseBullet> bullets = enemyAircraft.shoot();
                for(BaseBullet bullet : bullets){
                    bombPublisher.subscribe(bullet);
                }
                enemyBullets.addAll(bullets);
            }
            enemyShootCount++;
        }else{enemyShootCount++;}
        // 英雄射击
        if(heroShootCount % heroShootTime == 0){
            if(bulletMusicFlag) {
                new MusicThread("src/videos/bullet.wav").start();
            }
            List<BaseBullet> bullets = heroAircraft.shoot();
            heroBullets.addAll(bullets);
            heroShootCount++;
        }else {heroShootCount++;}
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propMoveAction() {
        for (AbstractProp prop : Props) {
            prop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for(BaseBullet bullet : enemyBullets){
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
                new MusicThread("src/videos/bullet_hit.wav").start();
            }
        }
        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    new MusicThread("src/videos/bullet_hit.wav").start();
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        if(enemyAircraft instanceof BossEnemy){
                            bossFlag = false;
                            musicThread[1].thisRunFlag = false;
                        }
                        Props.addAll(enemyAircraft.addProp());
                        score += scoreIncrease;
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for(AbstractProp prop : Props){
            if (prop.notValid()) {
                // 已被消耗的道具，不再检测
                // 避免一个道具重复使用的判定
                continue;
            }
            if(prop.crash(heroAircraft)){
                if(prop instanceof BombProp){
                    new MusicThread("src/videos/bomb_explosion.wav").start();
                }else {
                    new MusicThread("src/videos/get_supply.wav").start();
                }
                prop.effect(heroAircraft,enemyAircrafts,enemyBullets);
                prop.vanish();
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        for (BaseBullet bullet : enemyBullets){
            if(bullet.notValid()){bombPublisher.unsubscribe(bullet);}
        }
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        for (AbstractAircraft enemyAircraft : enemyAircrafts){
            if(enemyAircraft.notValid()){bombPublisher.unsubscribe(enemyAircraft);}
        }
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        Props.removeIf(AbstractFlyingObject::notValid);
    }

    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0,   this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyBullets);


        paintImageWithPositionRevised(g,Props);

        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

}
