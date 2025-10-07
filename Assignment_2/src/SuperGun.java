/**
 * This is the subclass SuperGun of the class Weapon.
 */
public class SuperGun extends Weapon {
	private boolean boosted;
	
	/**
	 * This is the class' constructor
	 * @param name Name of the weapon.
	 * @param power Power level of the weapon.
	 * @param boosted State of the weapon. Either boosted or not.
	 */
	public SuperGun(String name, int power) {
		super(name, power);
		this.boosted = false;
	}
	/**
	 * This is the boosting method of the SuperGun. Sets the boosted state to true.
	 * @param boosted State of the weapon. Either boosted or not.
	 */
	public void boost() {
    	this.boosted = true;
	}
	
	/**
	 * This is the overriding method of the shoot() method. If the SuperGun is boosted the power is doubled.
	 * @param boosted State of the weapon. Either boosted or not.
	 * @return power Returns the power of the shot dependent on whether the SuperGun is boosted or not.
	 */
	public int shoot() {
		if (this.boosted) {
			// Reset boost
			this.boosted = false;
			// Weapon's power is boosted by a factor of 2
			return getPower() * 2;
			}
		else {
			return getPower();
		}
		
		
	}
	
	

}
