package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.Context;
import edu.hitsz.strategy.StraightShoot;
import edu.hitsz.strategy.Strategy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控,单例模式
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /** 英雄机单例 实例*/
    private volatile static HeroAircraft heroAircraft;
    /** 攻击方式 */
    private int shootNum = 1;     //子弹一次发射数量
    private int power = 30;       //子弹伤害
    private static int direction = -1;  //子弹射击方向 (向下：1，向上：-1)
    private int mutliNum = 5;
    Context context = new Context(new StraightShoot());

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public void setMutliNum(int mutliNum){
        this.mutliNum = mutliNum;
    }

    public void setPower(int power){
        this.power = power;
    }

    public void setHeroAircraftHp(int hp){
        heroAircraft.hp = hp;
    }

    @Override
    public int getDirection() {
        return direction;
    }

    public static void resetHeroAircraft(){
        heroAircraft = null;
    }

    /**
     * 采用单例模式
     * 实例化对client隐蔽
     * 单例化通过双重检查锁定
     */
    public static HeroAircraft getHeroAircraft(){
        if(heroAircraft == null){
            synchronized (HeroAircraft.class){
                if(heroAircraft == null){
                    heroAircraft = new HeroAircraft(
                            Main.WINDOW_WIDTH / 2,
                            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                            0, 0, 800);
                }
            }
        }
        return heroAircraft;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    public void increaseShootNum(int increase){
        this.shootNum = this.shootNum + increase;
    }

    @Override
    public int getShootNum(){
        return shootNum;
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        return context.executeStrategy(this);
    }

    @Override
    public int getPower(){ return power; }

    @Override
    public int getMultiNum(){return mutliNum; }

    public void changeStrategy(Strategy strategy){
        this.context.setStrategy(strategy);
    }
}
