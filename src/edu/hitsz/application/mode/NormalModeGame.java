package edu.hitsz.application.mode;

import edu.hitsz.aircraft.BossFactory;
import edu.hitsz.aircraft.EliteFactory;
import edu.hitsz.aircraft.MobFactory;
import edu.hitsz.application.Game;

public class NormalModeGame extends Game {

    @Override
    public void initGame(){
        resetScore();
        heroAircraft.setHeroAircraftHp(600);
        heroAircraft.setPower(70);
        heroAircraft.setMutliNum(5);
        scoreIncrease = 30;
        mobPercent = 6;
        enemyMaxNumber = 8;
        BossFactory.bossHp = 800;
        EliteFactory.eliteHp = 80;
        MobFactory.mobHp = 60;
        bossPercent = 200;
        enemyShootTime = 2;
        heroShootTime = 2;
        gameModel = "normal";
    }

}
