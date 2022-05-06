package edu.hitsz.application.mode;

import edu.hitsz.aircraft.BossFactory;
import edu.hitsz.aircraft.EliteFactory;
import edu.hitsz.aircraft.MobFactory;
import edu.hitsz.application.Game;

public class HardModeGame extends Game {

    @Override
    public void initGame(){
        resetScore();
        heroAircraft.setHeroAircraftHp(400);
        heroAircraft.setPower(50);
        heroAircraft.setMutliNum(1);
        scoreIncrease = 50;
        mobPercent = 9;
        enemyMaxNumber = 11;
        BossFactory.bossHp = 800;
        EliteFactory.eliteHp = 80;
        MobFactory.mobHp = 60;
        bossPercent = 300;
        enemyShootTime = 1;
        heroShootTime = 3;
        gameModel = "hard";
    }

}
