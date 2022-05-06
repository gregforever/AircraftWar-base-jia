package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteFactory implements EnemyFactory{

    public static int eliteHp = 50;

    /**
     * 精英敌机生成方法 具体的创建者角色
     * @return
     * 返回精英敌机
     */
    @Override
    public AbstractEnemy createEnemy(){
        if(Math.random() < 0.5){
            return new EliteEnemy(
                    (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                    (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                    5,
                    10,
                    eliteHp
            );
        }else{
            return new EliteEnemy(
                    (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                    (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                    -5,
                    10,
                    eliteHp
            );
        }

    };
}
