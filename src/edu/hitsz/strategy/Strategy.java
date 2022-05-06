package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;

import java.util.List;

/**
 * @author xuhe
 */
public interface Strategy {
     List<BaseBullet> shootFunction(AbstractAircraft abstractAircraft);
}
