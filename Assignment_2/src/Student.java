/**
 * This is the subclass Student of the class Character
 */
public class Student extends Character {
	private boolean hidden;
	
	/**
	 * This is the class' constructor.
	 * @param name Name of the character
	 * @param energyLevel This is the energy level parameter of the character
	 * @param skillLevel This is the skill level parameter of the character
	 * @param hidden This is the hidden state of the student. They can be hidden or not.
	 * 
	 */
    public Student(String name, int energyLevel, int skillLevel) {
    	 super(name, energyLevel, skillLevel);
    	 this.hidden = false;
    }
    
    // Hide method
    /**
     * This method changes the state of the Student to be i hiding.
     */
    public void hide() {
    	this.hidden = true;
    }
    
    // Overriding hurt()
    /**
     * This is the overriding method of hurt(). If the Student is in a hiding state they avoid any damage.
     * @param attackAmount An integer value indicating how much damage the character is taking. 
     * @return attackAmount Returns the attack amount dependent on whether the Student avoided the attack or not.
     */
    public int hurt(int attackAmount) {
    	if (this.hidden) {
    		//No damage
    		attackAmount = 0;
    		//Reset status of hidden
    		this.hidden = false;
    	}
    	else {
    		int newEnergy = getEnergyLevel() - attackAmount;
    		setEnergyLevel(newEnergy);
    	}
    	return attackAmount;
    }
}
