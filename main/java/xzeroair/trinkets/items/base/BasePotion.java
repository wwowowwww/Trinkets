package xzeroair.trinkets.items.base;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.MagicStats;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.SurvivalCompat;

public class BasePotion extends Potion {

	protected static ResourceLocation ICON = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/potions/effects.png");

	protected String name;
	protected int duration;

	public BasePotion(String name, int duration, int color, int iconX, int iconY) {
		super(false, color);
		this.name = name;
		this.duration = duration;
		this.setIconIndex(iconX, iconY);
		this.setPotionName(Reference.MODID + ".effect." + name);
		this.setRegistryName(new ResourceLocation(Reference.MODID, name));
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration <= 1;
	}

	@Override
	public boolean isInstant() {
		return duration <= 0;
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {
		EntityProperties prop = Capabilities.getEntityRace(entity);
		if (prop != null) {
			MagicStats magic = prop.getMagic();
			if (name.equals(ModPotionTypes.restore)) {
				// TODO Maybe redo this?
				prop.setImbuedRace(EntityRaces.human);
				magic.setMana(magic.getMaxMana());
				prop.onUpdate();
				prop.sendInformationToAll();
			} else {
				if (name.equals(ModPotionTypes.advancedGlowing)) {
					prop.getMagic().setMana(prop.getMagic().getMaxMana());
				} else if (name.equals(ModPotionTypes.enhancedGlittering)) {
					prop.getMagic().addMana(prop.getMagic().getMaxMana() * 0.50F);
				} else {
					prop.getMagic().addMana(prop.getMagic().getMaxMana() * 0.25F);
				}
			}
		}
		float heals = 0;
		int thirst = 0;
		int saturation = 0;
		if (name.equals(ModPotionTypes.restore) || name.equals(ModPotionTypes.advancedGlowing)) {
			heals = entity.getMaxHealth();
			thirst = 20;
			saturation = 20;
		} else if (name.equals(ModPotionTypes.enhancedGlittering)) {
			entity.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 300, 0, false, false));
			heals = entity.getMaxHealth() * 0.5F;
			thirst = 10;
			saturation = 10;
		} else {
			heals = entity.getMaxHealth() * 0.25F;
			thirst = 5;
			saturation = 0;
		}
		if (heals > 0) {
			entity.heal(heals);
		}
		if (TrinketsConfig.SERVER.Potion.potion_thirst) {
			if (entity instanceof EntityPlayer) {
				SurvivalCompat.addThirst((EntityPlayer) entity, thirst, saturation);
				SurvivalCompat.clearThirst((EntityPlayer) entity);
			}
		}
	}

	@Override
	public boolean hasStatusIcon() {
		return false;
	}
}