/**
 * This is the subclass BadGun of the class Weapon.
 */
public class BadGun extends Weapon{
	
   	/**
	 * This is the class' constructor.
	 * @param name Name of the weapon.
	 * @param power Power level of the weapon.
	 */
	public BadGun(String name, int power) {
		super(name, power);
	}
	/**
	 * This is the overriding method of the shoot() method. The gun is so bad that it can only shoot 80% of its original power.
	 * @return 80% of its original power.
	 */
	public int shoot() {
		return (int) Math.floor(getPower() * 0.8);
	}

}
