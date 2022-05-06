package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;

import java.util.List;

public interface PropFactory {
    /**
     * 道具生成方法，所有道具对象必须实现
     * @return
     * 返回生成的道具
     */
    AbstractProp createProp(int locationX, int locationY, int speedY);
}
