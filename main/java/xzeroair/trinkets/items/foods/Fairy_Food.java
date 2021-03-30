package xzeroair.trinkets.items.foods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.items.base.FoodBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.SurvivalCompat;

public class Fairy_Food extends FoodBase {

	public Fairy_Food(String name) {
		super(name, 5, 0);
		this.setAlwaysEdible();
		this.setUUID("ec989890-2bdb-42bc-a2a1-d6b10bb9a220");
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 16;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityProperties prop = Capabilities.getEntityRace(entityLiving);
			if (prop != null) {
				if (TrinketsConfig.SERVER.Food.food_effects) {
					if (!(prop.getImbuedRace().equals(EntityRaces.fairy))) {
						prop.setImbuedRace(EntityRaces.fairy);
						prop.sendInformationToAll();
					}
				}
				if (TrinketsConfig.SERVER.Potion.potion_thirst) {
					int amount = 5;
					if ((prop.getCurrentRace().equals(EntityRaces.fairy))) {
						amount = 10;
					}
					SurvivalCompat.addThirst(entityLiving, amount, amount);
					SurvivalCompat.clearThirst(entityLiving);
				}
			}
		}
		this.setCooldown(20);
		super.onItemUseFinish(stack, worldIn, entityLiving);
		return stack;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		final ItemStack itemstack = player.getHeldItem(handIn);
		boolean flag = this.getEdible();

		if (player.canEat(flag)) {
			player.setActiveHand(handIn);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
		} else {
			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}
}