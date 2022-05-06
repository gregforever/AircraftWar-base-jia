package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossFactory implements EnemyFactory{

    public static int bossHp = 500;

    @Override
    /**
     * 头目敌机生成方法 具体的创建者角色
     * @return
     * 返回头目敌机
     */
    public AbstractEnemy createEnemy(){
        return new BossEnemy(
                (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                5,
                0,
                bossHp
        );
    };
}
