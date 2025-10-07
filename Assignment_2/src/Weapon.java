/**
 * Returns a Weapon object that can be used in the OfficeCombat scenario. 
 * 
 * @param weaponName Name of the weapon
 * @param power Power level of the weapon
 */
public class Weapon {	
	private String weaponName;
    private int power;
    
    // Constructor anticipating all variables are supplied
   	/**
	 * This is the class' constructor.
	 * @param name Name of the weapon.
	 * @param power Power level of the weapon.
	 */
    public Weapon(String name, int power) {
        this.weaponName = name;
        this.power = power;
        }
    
    // 'Get' methods
  	/** 
	 * This method gets the name of the weapon.
	 * @return weaponName This returns the name of the weapon.
	 */
    public String getName() {
    	return this.weaponName;
    }
    /** 
	 * This method gets the power of the weapon.
	 * @return power This returns the name of the weapon.
	 */
    public int getPower() {
    	return this.power;
    }
    
    // 'Action' of the weapon
 	/**
	 * This methods resembles usage of the weapon. An attack using the weapon.
	 * @return power This method returns the power level of the weapon.
	 */
    public int shoot() {
    	return this.power;
    }
}
