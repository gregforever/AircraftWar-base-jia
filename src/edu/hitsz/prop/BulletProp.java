package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.SlashShoot;
import edu.hitsz.strategy.StraightShoot;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author xuhe
 */
public class BulletProp extends AbstractProp {
    public BulletProp(){
    }
    public BulletProp(int locationX, int locationY, int speedX, int speedY){
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft, List<AbstractEnemy> enemyAircrafts, List<BaseBullet> enemyBullets) {
        heroAircraft.increaseShootNum(1);
        heroAircraft.changeStrategy(new SlashShoot());
        System.out.println("Bullet prop affected #_#");
        Runnable r = () -> {
            try{
                TimeUnit.SECONDS.sleep(10);
                if(heroAircraft.getShootNum() == 1) {
                    heroAircraft.changeStrategy(new StraightShoot());
                }else{
                    heroAircraft.increaseShootNum(-1);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        };
        new Thread(r).start();
    }

}
