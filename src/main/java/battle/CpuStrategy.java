package battle;

import model.Attack;
import model.Pokemon;

public interface CpuStrategy {

    Attack chooseAttack(Pokemon self);

}
