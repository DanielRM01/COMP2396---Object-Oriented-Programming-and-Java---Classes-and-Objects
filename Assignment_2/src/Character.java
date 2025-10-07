/**
 * Returns a Character object that can be used in the OfficeCombat scenario. 
 */
public class Character {
    private String characterName;
    private int skillLevel;
    private int energyLevel;
    
    /**
     * This is the class' constructor.
     * @param name Name of the character
     * @param energyLevel This is the energy level parameter of the character
     * @param skillLevel This is the skill level parameter of the character
     */
    public Character(String name, int energyLevel, int skillLevel) {
        this.characterName = name;
        this.skillLevel = skillLevel;
        this.energyLevel = energyLevel;
    }
    
    /**
     * The get method for the character name
     * @return characterName This returns the name of the character
     */
    public String getName() {
    	return this.characterName;
    }
    
    /**
     * The get method for the skill level of the character
     * @return skillLevel This returns the skill level of the character
     */
    public int getSkillLevel() {
    	return this.skillLevel;
    }
    
    /**
     * The get method for the skill level of the character
     * @return energyLevel This returns the current energy level of the character
     */
    public int getEnergyLevel() {
    	return this.energyLevel;
    }
    
    // Adding a set method for energy level given the restriction of having private fields
    /**
     * This method sets the energy level of the character. This set method for energy level is added given the restriction of having private fields.
     * @param energyLevel This is the energy level parameter of the character
     */
    public void setEnergyLevel(int energyLevel) {
    	this.energyLevel = energyLevel;
    }
    
    /**
     * This method is resembling an attack being dealt to the character. 
     * It deals damage to the character by reducing the energy level by a certain attack amount.
     * @param attackAmount An integer value indicating how much damage the character is taking. 
     * @return attackAmount This returns the attack amount
     */
    public int hurt(int attackAmount) {
    	this.energyLevel -= attackAmount;
    	return attackAmount;
    }
    
    /**
     * This method calculates the attack amount a character can deliver by usage of a specific weapon.
     * @param Weapon w1 A Weapon object with a specific name and power level.
     * @return integer The method returns the attack amount which is the sum of the weapon's power and the character's skill level. 
     */
    public int attack(Weapon w1) {
    	return w1.shoot() + this.skillLevel;
    }
    
    /**
     * This method evaluates whether the character is losing or not. If the energy level drop to zero or below they lose.
     * @return boolean Either true or false given the energy level state.
     */
    public boolean isLose() {
    	if (this.energyLevel > 0) {return false;}
    	else {return true;}
    }
}