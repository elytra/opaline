package com.elytradev.opaline.item;

import com.elytradev.opaline.Opaline;
import com.elytradev.opaline.block.ModBlocks;
import com.elytradev.opaline.util.FluidEnchantmentHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentKnockback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class itemDebugger extends ItemBase {

    public itemDebugger(String name) {
        super(name);
        setMaxStackSize(1);
        setCreativeTab(Opaline.creativeTab);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if (player.isSneaking()) {
            if (te != null) {
                if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)) {
                    IFluidHandler tank = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
                    IFluidTankProperties[] properties = tank.getTankProperties();
                    for (IFluidTankProperties prop : properties) {
                        if (prop.getContents() == null) {
                            FluidStack enchLazurite = new FluidStack(ModBlocks.fluidLazurite, prop.getCapacity() / 2);
                            FluidEnchantmentHelper.addFluidEnchantment(enchLazurite, new EnchantmentData(Enchantment.getEnchantmentByLocation("protection"), 0));
                            FluidEnchantmentHelper.addFluidEnchantment(enchLazurite, new EnchantmentData(Enchantment.getEnchantmentByLocation("sharpness"), 0));

                            tank.fill(enchLazurite, true);
                            return EnumActionResult.SUCCESS;
                        } else {
                            if (!world.isRemote) {
                                player.sendMessage(new TextComponentTranslation("opaline.msg.tankFull"));
                                return EnumActionResult.FAIL;
                            }
                        }
                    }
                } else {
                    if (!world.isRemote) {
                        player.sendMessage(new TextComponentTranslation("opaline.msg.noTank"));
                    }
                    return EnumActionResult.FAIL;
                }
            } else {
                if (!world.isRemote) {
                    player.sendMessage(new TextComponentTranslation("opaline.msg.noTE"));
                }
                return EnumActionResult.FAIL;
            }
        }
        return EnumActionResult.PASS;
    }
}
