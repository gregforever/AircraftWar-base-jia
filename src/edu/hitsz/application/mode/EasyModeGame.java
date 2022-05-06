package edu.hitsz.application.mode;

import edu.hitsz.aircraft.BossFactory;
import edu.hitsz.aircraft.EliteFactory;
import edu.hitsz.aircraft.MobFactory;
import edu.hitsz.application.Game;

public class EasyModeGame extends Game {

    @Override
    public void initGame(){
        resetScore();
        heroAircraft.setHeroAircraftHp(800);
        heroAircraft.setPower(100);
        heroAircraft.setMutliNum(10);
        scoreIncrease = 10;
        mobPercent = 3;
        enemyMaxNumber = 5;
        BossFactory.bossHp = 500;
        EliteFactory.eliteHp = 50;
        MobFactory.mobHp = 30;
        bossPercent = 400;
        enemyShootTime = 3;
        heroShootTime = 1;
        gameModel = "easy";
    }

}
