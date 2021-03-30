package xzeroair.trinkets.items.base;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class FoodBase extends ItemFood implements IsModelLoaded {

	private UUID uuid;
	private int cooldown = 0;
	private boolean canEat = true;

	public FoodBase(String name, int heal, float saturation) {
		super(heal, saturation, false);
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setCreativeTab(Trinkets.trinketstab);
		ModItems.foods.ITEMS.add(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		TranslationHelper.addTooltips(stack, worldIn, tooltip);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return TranslationHelper.addTextColorFromLangKey(super.getItemStackDisplayName(stack));
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = UUID.fromString(uuid);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (cooldown > 0) {
			canEat = false;
			cooldown--;
		} else {
			if (canEat == false) {
				canEat = true;
			}
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public boolean getEdible() {
		return canEat;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}
}