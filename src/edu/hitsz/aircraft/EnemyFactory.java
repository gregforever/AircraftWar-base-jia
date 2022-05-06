package edu.hitsz.aircraft;

import edu.hitsz.prop.AbstractProp;

/**
 * 敌对飞机工厂的接口 创建者角色
 * @author xuhe
 */

public interface EnemyFactory {
    /**
     * 敌机生成方法 抽象的创建者角色
     * @return
     * 全部对象需实现，返回敌机
     */
    AbstractEnemy createEnemy();
}
