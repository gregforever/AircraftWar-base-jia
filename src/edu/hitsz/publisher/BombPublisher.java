package edu.hitsz.publisher;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.Game;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.BombProp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BombPublisher {

    private static BombPublisher bombPublisher;
    private List<AbstractAircraft> enemyAircrafts = new ArrayList<>();
    private List<BaseBullet> enemyBullets = new ArrayList<>();

    private BombPublisher(){}

    /**
     * 采用单例模式
     * 实例化对client隐蔽
     * 单例化通过双重检查锁定
     */
    public static BombPublisher getBombPublisher(){
        if(bombPublisher == null){
            synchronized (BombPublisher.class){
                if(bombPublisher == null){
                    bombPublisher = new BombPublisher();
                }
            }
        }
        return bombPublisher;
    }

    //观察者列表 包含敌机与敌机子弹
    public void subscribe(AbstractFlyingObject abstractFlyingObject){
        if(abstractFlyingObject instanceof BaseBullet){
            enemyBullets.add((BaseBullet)abstractFlyingObject);
        }else{
            enemyAircrafts.add((AbstractAircraft) abstractFlyingObject);
        }
    }

    public void unsubscribe(AbstractFlyingObject abstractFlyingObject){
        if(abstractFlyingObject instanceof BaseBullet){
            enemyBullets.remove((BaseBullet)abstractFlyingObject);
        }else{
            enemyAircrafts.remove((AbstractAircraft) abstractFlyingObject);
        }
    }

    public void notifyAllSubscriber(){
        for(AbstractAircraft enemyAircraft : enemyAircrafts){
            enemyAircraft.update();
            Main.game.increaseScore();
        }
        for(BaseBullet enemyBullet : enemyBullets){
            enemyBullet.update();
        }
        enemyAircrafts = new ArrayList<>();
        enemyBullets = new ArrayList<>();
    }

    public void bombEffect(){
        notifyAllSubscriber();
    }

}
