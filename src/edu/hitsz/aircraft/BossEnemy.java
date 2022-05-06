package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.MusicThread;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.*;
import edu.hitsz.strategy.Context;
import edu.hitsz.strategy.SlashShoot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 头目敌机
 * 可射击
 *
 * @author xuhe
 */
public class BossEnemy extends AbstractEnemy {

    /** 攻击方式 */
    private int shootNum = 3;     //子弹一次发射数量
    private int power = 20;       //子弹伤害
    private int direction = 1;  //子弹射击方向 (向下：1，向上：-1)

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public int getDirection() {
        return direction;
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        Context context = new Context(new SlashShoot());
        return context.executeStrategy(this);
    }

    @Override
    public int getPower(){ return power; }

    @Override
    public int getShootNum(){
        return shootNum;
    }

    @Override
    public int getMultiNum(){return 5; }

    @Override
    /**
     * 头目敌机生成道具方法
     * @return
     * 返回 null
     */
    public List<AbstractProp> addProp(){
        int theTypeOfProp = getRandomInt(0,3);
        int loopTime = getRandomInt(1,3);
        List<AbstractProp> res = new LinkedList<>();
        PropFactory propFactory;
        AbstractProp prop;
        for(int i = 0; i < loopTime; i++){
            if(theTypeOfProp <= 2){
                //BloodProp
                if(theTypeOfProp == 0) {
                    propFactory = new BloodPropFactory();
                }
                //BombProp
                else if (theTypeOfProp == 1){
                    propFactory = new BombPropFactory();
                }
                //BulletProp
                else{
                    propFactory = new BulletPropFactory();
                }
                prop = propFactory.createProp(this.locationX,this.locationY,direction*5);
                res.add(prop);
            }
            theTypeOfProp = getRandomInt(0,3);
        }
        return res;
    }

    @Override
    public void update(){}
}
