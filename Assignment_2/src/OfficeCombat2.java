import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class OfficeCombat2 {
	
	// To test if all fields in a class is not public
	public static boolean areAllFieldsPrivate(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isPrivate(field.getModifiers())) {
                return false;
            }
        }
        return true;
    }
	
	public static void main(String[] args) throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader inData = new BufferedReader(isr);
			
		// Input combat data
		String c1_info[] = inData.readLine().split(" ");
		String c2_info[] = inData.readLine().split(" ");
		String w1_info[] = inData.readLine().split(" ");
		String w2_info[] = inData.readLine().split(" ");
		
		SecurityGuard c1 = new SecurityGuard(c1_info[0], Integer.valueOf(c1_info[1]), Integer.valueOf(c1_info[2]));
		Student c2 = new Student(c2_info[0], Integer.valueOf(c2_info[1]), Integer.valueOf(c2_info[2]));
		SuperGun w1 = new SuperGun(w1_info[0], Integer.valueOf(w1_info[1]));
		BadGun w2 = new BadGun(w2_info[0], Integer.valueOf(w2_info[1]));
		
		// Start fighting
		System.out.println("Now fighting: " + c1.getName() + " VS " + c2.getName());
		System.out.println("Skill level of " + c1.getName() + ": " + c1.getSkillLevel());
		System.out.println("Skill level of " + c2.getName() + ": " + c2.getSkillLevel());
		System.out.println("Energy level of " + c1.getName() + ": " + c1.getEnergyLevel());
		System.out.println("Energy level of " + c2.getName() + ": " + c2.getEnergyLevel());
		System.out.println("----------------------------");
		
		int round = 0;
		while (!c1.isLose() && !c2.isLose()) {
			if (round % 2 == 0) {
				int attackAmount = c1.attack(w1);
				System.out.println(c1.getName() + " makes an attack by " + w1.getName() + "!");
				
				int hurtAmount = c2.hurt(attackAmount);
				if (hurtAmount == 0) {
					System.out.println(c2.getName() + " hides from the attack!");
				}
				else {
					System.out.println(c2.getName() + " takes a hurt amount of " + hurtAmount + "! Remaining energy becomes " + c2.getEnergyLevel() + ".");
				}
				
				if (round % 3 == 0) {
					c2.hide();
				}
			} 
			else {
				int attackAmount = c2.attack(w2);
				int hurtAmount = c1.hurt(attackAmount);
				
				System.out.println(c2.getName() + " makes an attack by " + w2.getName() + "!");
				System.out.println(c1.getName() + " takes a hurt amount of " + hurtAmount + "! Remaining energy becomes " + c1.getEnergyLevel() + ".");
				
				if (round % 3 == 0) {
					c1.boostWeapon(w1);
					System.out.println(c1.getName() + " boost the " + w1.getName() + "!");
				}
			}
			round++;
		}
		
		if (c1.isLose()) {
			System.out.println(c2.getName() + " wins! The examination paper is stolen!");
		}
		else {
			System.out.println(c1.getName() + " wins! The examination paper is secured!");
		}
		
		// Test if all class fields are private
		System.out.println("----------------------------");
		System.out.println("Test if all the class fields are private");
		System.out.println("Character: " + areAllFieldsPrivate(Character.class));
		System.out.println("Student: " + areAllFieldsPrivate(Student.class));
		System.out.println("SecurityGuard: " + areAllFieldsPrivate(SecurityGuard.class));
		System.out.println("Weapon: " + areAllFieldsPrivate(Weapon.class));
		System.out.println("SuperGun: " + areAllFieldsPrivate(SuperGun.class));
		System.out.println("BadGun: " + areAllFieldsPrivate(BadGun.class));
	}
}