package hero;

import java.util.ArrayList;

public class Unit {

    //String
    private final String image;
    private final String name;
    private final String game;
    //HP
    private int curr_hp;
    private int max_hp;

    //AS
    //In teoria da modificare, è meglio forse usare un Json per supportare varie traduzioni
    private final String[] ability_name = new String[] {"Str", "Dex", "Con", "Int", "Wis", "Cha"};
    private final int[] ability_score = new int[6];
    private final int[] ability_mod = new int[6];

    //Buff Array
    private final ArrayList<Buff> buffArrayList = new ArrayList<>();
    private final ArrayList<Buff> debuffArrayList = new ArrayList<>();

    public Unit (String image, String name, String game, int[] ability_score, int max_hp) {
        this.image = image;
        this.name = name;
        this.game = game;
        this.max_hp = max_hp;
        curr_hp = max_hp;
        for (int i = 0; i < 6; i ++) {
            this.ability_score[i] = ability_score[i];
            modCalculator(i);
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
    public String getGame() {
        return game;
    }
    public String[] getAbility_name() {
        return ability_name;
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
        modCalculator(index);
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

    ////////////////
    //OTHER METHOD//
    ////////////////

    /**
     * Called every time an Ability Score is modified to update the modifier.
     * Modifier is calculated removing 10 from the AS and then halving the value.
     * @param index Position in the array of the Ability Score
     */
    private void modCalculator (int index) {
        int temp = ability_score[index];
        if (temp%2 != 0) temp-- ; //remove possible rounding error
        temp = temp - 10;
        ability_mod[index] =  temp/2;
    }


}


