package hero;

import hero.Enum.Edition;
import hero.magic.casterClass.Caster_Class_Base;
import hero.otherTracker.OtherTracker_Base;

import java.util.ArrayList;

public class Unit {

    //String
    private final String image;
    private final String name;
    private final Edition game;
    //HP
    private int curr_hp;
    private int max_hp;

    //AS
    private final int[] ability_score = new int[6];
    private final int[] ability_mod = new int[6];

    //Buff Array
    private final ArrayList<Buff> buffArrayList = new ArrayList<>();
    private final ArrayList<Buff> debuffArrayList = new ArrayList<>();

    //OtherTracker Array
    private final ArrayList<OtherTracker_Base> otherTrackerArrayList = new ArrayList<>();


    //Spell
    private final ArrayList<Caster_Class_Base> casterClassList = new ArrayList<>();


    public Unit (String image, String name, Edition game, int[] ability_score, int max_hp) {
        this.image = image;
        this.name = name;
        this.game = game;
        this.max_hp = max_hp;
        curr_hp = max_hp;
        for (int i = 0; i < 6; i ++) {
            int abilityScore = ability_score[i];
            this.ability_score[i] = abilityScore;
            this.ability_mod[i] = modCalculator(abilityScore);
        }
    }

    /////////////////////
    //GETTER AND SETTER//
    /////////////////////

    //String getter
    public String getImage() {
        return image;
    }
    public String getName() {
        return name;
    }


    //HP getter and setter
    public int getCurr_hp() {
        return curr_hp;
    }
    public int getMax_hp() {
        return max_hp;
    }

    public void setCurr_hp(int curr_hp) {
        this.curr_hp = curr_hp;
    }
    public void setMax_hp(int max_hp) {
        this.max_hp = max_hp;
    }

    //AbilityScore Score and Modifier
    public int[] getAbility_score() {
        return ability_score;
    }
    public void setAbility_Score(int index, int newScore){
        ability_score[index] = newScore;
        ability_mod[index] = modCalculator(newScore);
    }
    public int[] getAbility_mod() {
        return ability_mod;
    }

    //Buff and debuff getter and setter
    public ArrayList<Buff> getBuffArrayList() {
        return buffArrayList;
    }
    public ArrayList<Buff> getDebuffArrayList() {
        return debuffArrayList;
    }

    //Add a buff to the correct list
    public void addBuff (Buff buff){
        if (buff.isBuff()) buffArrayList.add(buff);
        else debuffArrayList.add(buff);
    }

    //Tracker getter
    public ArrayList<OtherTracker_Base> getOtherTrackerArrayList() {
        return otherTrackerArrayList;
    }

    //Caster getter and adder
    public ArrayList<Caster_Class_Base> getCasterClassList() {
        return casterClassList;
    }



    ////////////////
    //OTHER METHOD//
    ////////////////

    /**
     * Calculate the Modifier of the given abilityScore.
     * Modifier is calculated by removing 10 from the AS and then halving the value.
     * @param abilityScore base value to evaluate
     * @return the calculated modifier
     */
    public static int modCalculator (int abilityScore) {
        int modifier = abilityScore;
        if (modifier%2 != 0) modifier-- ; //remove possible rounding error
        modifier = modifier - 10;
        return modifier/2;
    }


}


