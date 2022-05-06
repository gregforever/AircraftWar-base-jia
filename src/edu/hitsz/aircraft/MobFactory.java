package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class MobFactory implements EnemyFactory{

    public static int mobHp = 30;

    /**
     * 普通敌机生成方法 具体的创建者角色
     * @return
     * 返回普通敌机
     */
    @Override
    public AbstractEnemy createEnemy(){
        return new MobEnemy(
                (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                0,
                10,
                mobHp
        );
    };
}
