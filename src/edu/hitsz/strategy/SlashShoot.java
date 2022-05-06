package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class SlashShoot implements Strategy{
    @Override
    public List<BaseBullet> shootFunction(AbstractAircraft abstractAircraft){
        int x = abstractAircraft.getLocationX();
        int y = abstractAircraft.getLocationY() + abstractAircraft.getDirection()*2;
        int speedY = abstractAircraft.getSpeedY() + abstractAircraft.getDirection()*abstractAircraft.getMultiNum();
        int shootNum = abstractAircraft.getShootNum();
        int power = abstractAircraft.getPower();
        List<BaseBullet> res = new LinkedList<>();
        for(int i=0; i<abstractAircraft.getShootNum(); i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            if(abstractAircraft instanceof HeroAircraft){
                res.add(new HeroBullet(x + (i*2 - shootNum + 1)*10, y, i*2 - shootNum + 1, speedY, power));
            }else {
                res.add(new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, i*2 - shootNum + 1, speedY, power));
            }
        }
        return res;
    }
}
