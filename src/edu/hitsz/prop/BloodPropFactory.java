package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;

import java.util.LinkedList;
import java.util.List;

public class BloodPropFactory implements PropFactory{
    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedY) {
        AbstractProp prop = new BloodProp(locationX,
                locationY,
                0,
                speedY);
        return prop;
    }
}
