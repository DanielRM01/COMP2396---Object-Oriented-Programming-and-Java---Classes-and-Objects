/**
 * This is the subclass SecurityGuard of the class Character.
 */
public class SecurityGuard extends Character {
	
	// Constructor
	/**
	 * This is the class' constructor constructor.
	 * @param name Name of the character
	 * @param energyLevel This is the energy level parameter of the character
	 * @param skillLevel This is the skill level parameter of the character
	 */
    public SecurityGuard(String name, int energyLevel, int skillLevel) {
    	 super(name, energyLevel, skillLevel);
    }
    
    /**
     * This method calls the boost() method of a SuperGun object.
     * @param w1 A SuperGun object with a specific name and power level. 
     */
    public void boostWeapon(SuperGun w1) {
    	w1.boost();
    }
    
}