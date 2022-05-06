package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.Prop;

import java.util.List;

/**
 * 敌对飞机的接口 产品角色
 * @author xuhe
 */

public interface Enemy {
    /**
     * 敌机生成道具方法，可生成道具对象必须实现
     * @return
     * 可生成道具对象需实现，返回道具
     * 不可生成道具对象空实现，返回null
     */
    List<AbstractProp> addProp();
}
