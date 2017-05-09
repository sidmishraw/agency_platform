/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.ultradome.factory
 * File: UltradomeFactory.java
 * 
 * @author sidmishraw
 *         Last modified: May 7, 2017 12:42:28 PM
 */
package edu.sjsu.sidmishraw.examples.ultradome.factory;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.sjsu.sidmishraw.examples.ultradome.core.MedicineBowl;
import edu.sjsu.sidmishraw.examples.ultradome.core.ShieldSkin;
import edu.sjsu.sidmishraw.examples.ultradome.core.Strike;
import edu.sjsu.sidmishraw.examples.ultradome.core.StrikeStrategy;
import edu.sjsu.sidmishraw.examples.ultradome.core.Weapon;
import edu.sjsu.sidmishraw.examples.ultradome.core.WeaponStrikeType;
import edu.sjsu.sidmishraw.examples.ultradome.core.WeaponType;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.examples.ultradome.factory.UltradomeFactory
 *
 */
public class UltradomeFactory {
	
	// param constants
	public static final String	WEAPONS			= "WEAPONS";
	public static final String	SHIELD_SKINS	= "SHIELD_SKINS";
	public static final String	MEDICINE_BOWLS	= "MEDICINE_BOWLS";
	
	// constants
	private static final String	HELL_SWORD		= "Hell Sword from hell >(|)<";
	private static final String	MAGIC_WAND		= "Angel's magic wand from hell }}----*";
	private static final String	POISON_WAND		= "Poison wand from hell @-----#";
	private static final String	POISON			= "Poison bomb from hell {%%}";
	private static final String	FLAME_THROWER	= "FlameThrower of hell @@``~~~";
	
	/**
	 * Makes a new weapon for the gladiator
	 * 
	 * @param weaponName
	 * @param type
	 * @param strikeStrategy
	 * @return Weapon
	 */
	public Weapon makeWeapon(String weaponName, WeaponType type, StrikeStrategy strikeStrategy) {
		
		Weapon weapon = new Weapon(weaponName, type);
		
		weapon.setStrikeStrategy(strikeStrategy);
		
		return weapon;
	}
	
	/**
	 * Makes the queue of weapons that is maintained by the world (Ultradome).
	 * 
	 * @return ConcurrentLinkedQueue<Weapon>
	 */
	public ConcurrentLinkedQueue<Weapon> makeWorldWeapons() {
		
		ConcurrentLinkedQueue<Weapon> weapons = new ConcurrentLinkedQueue<>();
		
		// My ultradome has 3 hell swords, 3 magic wands, 2 poison wands, 5
		// poisons, 2 flame throwers
		final int HELL_SWORD_COUNT = 3;
		final int MAGIC_WAND_COUNT = 3;
		final int POISON_WAND_COUNT = 2;
		final int POISON_COUNT = 5;
		final int FLAME_THROWER_COUNT = 2;
		
		// hell swords
		for (int i = 0; i < HELL_SWORD_COUNT; i++) {
			
			weapons.add(this.makeWeapon(HELL_SWORD, WeaponType.SWORD, () -> {
				
				Random random = new Random();
				
				Strike strike = new Strike(
						"A molten sword strike from hell with " + HELL_SWORD + ", deals dmg of both IRON and FIRE!",
						0.25F * (random.nextInt(10) + 1), WeaponStrikeType.FIRE, WeaponStrikeType.IRON);
				
				return strike;
			}));
		}
		
		// magic wands
		for (int i = 0; i < MAGIC_WAND_COUNT; i++) {
			
			weapons.add(this.makeWeapon(MAGIC_WAND, WeaponType.WAND, () -> {
				
				Random random = new Random();
				
				Strike strike = new Strike("Magic from " + MAGIC_WAND + " behold!", 0.28F * random.nextInt(10),
						WeaponStrikeType.MAGIC);
				
				return strike;
			}));
		}
		
		// poison wands
		for (int i = 0; i < POISON_WAND_COUNT; i++) {
			
			weapons.add(this.makeWeapon(POISON_WAND, WeaponType.WAND, () -> {
				
				Random random = new Random();
				
				Strike strike = new Strike("Posion Magic with " + POISON_WAND + " earns a bad reputation!",
						0.30F * (random.nextInt(10) + 1), WeaponStrikeType.MAGIC, WeaponStrikeType.CHEMICAL);
				
				return strike;
			}));
		}
		
		// poisons
		for (int i = 0; i < POISON_COUNT; i++) {
			
			weapons.add(this.makeWeapon(POISON, WeaponType.POISON, () -> {
				
				Random random = new Random();
				
				Strike strike = new Strike(POISON + " BOMB!", 0.50F * (random.nextInt(10) + 1),
						WeaponStrikeType.CHEMICAL);
				
				return strike;
			}));
		}
		
		// flame throwers
		for (int i = 0; i < FLAME_THROWER_COUNT; i++) {
			
			weapons.add(this.makeWeapon(FLAME_THROWER, WeaponType.FLAME_THROWER, () -> {
				
				Random random = new Random();
				
				// strikes between - 0.8F x {5, 6, ... 10} = 4, ... 8 HP
				Strike strike = new Strike(FLAME_THROWER + ", Burn baby BURNNN!", 0.80F * (random.nextInt(6) + 5),
						WeaponStrikeType.CHEMICAL);
				
				return strike;
			}));
		}
		
		return weapons;
	}
	
	/**
	 * Makes the shield skins for Ultradome world
	 * 
	 * @return ConcurrentLinkedQueue<ShieldSkin>
	 */
	public ConcurrentLinkedQueue<ShieldSkin> makeWorldShieldSkins() {
		
		ConcurrentLinkedQueue<ShieldSkin> shieldSkins = new ConcurrentLinkedQueue<>();
		
		// 5 IRON shields
		// 6 CHEMICAL shields
		// 10 MAGIC shields
		// 9 FIRE shields
		final int IRON_SHIELD_COUNT = 5;
		final int CHEMICAL_SHIELD_COUNT = 6;
		final int MAGIC_SHIELD_COUNT = 10;
		final int FIRE_SHIELD_COUNT = 9;
		
		// iron shields
		for (int i = 0; i < IRON_SHIELD_COUNT; i++) {
			
			shieldSkins.add(new ShieldSkin(incomingStrike -> {
				
				Random random = new Random();
				
				int randomint = random.nextInt(10);
				
				// only reduce the IRON type strikes, else let it pass
				if (incomingStrike.getStrikeType().contains(WeaponStrikeType.IRON)) {
					
					float oldStrength = incomingStrike.getStrength();
					
					float newStrength = oldStrength - (oldStrength * (0.085F * randomint));
					
					incomingStrike.setStrength(newStrength);
				}
				
				return incomingStrike;
			}));
		}
		
		// chemical attack shields
		for (int i = 0; i < CHEMICAL_SHIELD_COUNT; i++) {
			
			shieldSkins.add(new ShieldSkin(incomingStrike -> {
				
				Random random = new Random();
				
				int randomint = random.nextInt(10);
				
				// only reduce the IRON type strikes, else let it pass
				if (incomingStrike.getStrikeType().contains(WeaponStrikeType.CHEMICAL)) {
					
					float oldStrength = incomingStrike.getStrength();
					
					float newStrength = oldStrength - (oldStrength * (0.076F * randomint));
					
					incomingStrike.setStrength(newStrength);
				}
				
				return incomingStrike;
			}));
		}
		
		// magic attack shields
		for (int i = 0; i < MAGIC_SHIELD_COUNT; i++) {
			
			shieldSkins.add(new ShieldSkin(incomingStrike -> {
				
				Random random = new Random();
				
				int randomint = random.nextInt(10);
				
				// only reduce the IRON type strikes, else let it pass
				if (incomingStrike.getStrikeType().contains(WeaponStrikeType.MAGIC)) {
					
					float oldStrength = incomingStrike.getStrength();
					
					float newStrength = oldStrength - (oldStrength * (0.0789F * randomint));
					
					incomingStrike.setStrength(newStrength);
				}
				
				return incomingStrike;
			}));
		}
		
		// fire attack shields
		for (int i = 0; i < FIRE_SHIELD_COUNT; i++) {
			
			shieldSkins.add(new ShieldSkin(incomingStrike -> {
				
				Random random = new Random();
				
				int randomint = random.nextInt(10);
				
				// only reduce the IRON type strikes, else let it pass
				if (incomingStrike.getStrikeType().contains(WeaponStrikeType.FIRE)) {
					
					float oldStrength = incomingStrike.getStrength();
					
					float newStrength = oldStrength - (oldStrength * (0.0825F * randomint));
					
					incomingStrike.setStrength(newStrength);
				}
				
				return incomingStrike;
			}));
		}
		
		return shieldSkins;
	}
	
	/**
	 * Makes the medicine bowls for the world of ultradome
	 * 
	 * @return ConcurrentLinkedQueue
	 */
	public ConcurrentLinkedQueue<MedicineBowl> makeWorldMedicineBowls() {
		
		ConcurrentLinkedQueue<MedicineBowl> medicineBowls = new ConcurrentLinkedQueue<>();
		
		// creating 6 medicine bowls
		final int MEDICINE_BOWL25_COUNT = 1;
		final int MEDICINE_BOWL40_COUNT = 1;
		
		// heals 25 HP
		for (int i = 0; i < MEDICINE_BOWL25_COUNT; i++) {
			
			medicineBowls.add(new MedicineBowl());
		}
		
		// heals 40 HP
		for (int i = 0; i < MEDICINE_BOWL40_COUNT; i++) {
			
			medicineBowls.add(new MedicineBowl(40));
		}
		
		return medicineBowls;
	}
}
