package com.elytradev.opaline.block;

import com.elytradev.opaline.Opaline;
import com.elytradev.opaline.block.fluids.BlockLazurite;
import com.elytradev.opaline.block.fluids.BlockOpaline;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static BlockDistiller DISTILLER = new BlockDistiller().setCreativeTab(Opaline.creativeTab);
    public static BlockInfuser INFUSER = new BlockInfuser().setCreativeTab(Opaline.creativeTab);
    public static BlockTriTank TRI_TANK = new BlockTriTank().setCreativeTab(Opaline.creativeTab);
    public static BlockCentrifuge CENTRIFUGE = new BlockCentrifuge().setCreativeTab(Opaline.creativeTab);

    public static Fluid fluidOpaline = new Fluid("opaline",
        new ResourceLocation("opaline", "blocks/fluids/opaline_still"),
        new ResourceLocation("opaline", "blocks/fluids/opaline_flowing"))
        .setDensity(1396) //the density of glitter glue
        .setTemperature(294) //approximately 69ºF
        .setRarity(EnumRarity.UNCOMMON);
    public static Fluid fluidLazurite = new Fluid("lazurite",
            new ResourceLocation("opaline", "blocks/fluids/lazurite_still"),
            new ResourceLocation("opaline", "blocks/fluids/lazurite_flowing"))
            .setDensity(1436) //the year Gutenberg invented the printing press
            .setTemperature(506) //451ºF
            .setRarity(EnumRarity.RARE);

    public static Block[] allBlocks = {
        DISTILLER, INFUSER, TRI_TANK, CENTRIFUGE
    };

    public static void register(IForgeRegistry<Block> registry) {
        for (Block block : allBlocks) {
            registry.register(block);
        }

        FluidRegistry.registerFluid(ModBlocks.fluidOpaline);
        BlockOpaline opaline = new BlockOpaline(fluidOpaline, "fluid_opaline");
        registry.register(opaline);
        fluidOpaline.setBlock(opaline);
        FluidRegistry.addBucketForFluid(ModBlocks.fluidOpaline);

        FluidRegistry.registerFluid(ModBlocks.fluidLazurite);
        BlockLazurite lazurite = new BlockLazurite(fluidLazurite, "fluid_lazurite");
        registry.register(lazurite);
        fluidLazurite.setBlock(lazurite);
        FluidRegistry.addBucketForFluid(ModBlocks.fluidLazurite);

        GameRegistry.registerTileEntity(DISTILLER.getTileEntityClass(), DISTILLER.getRegistryName().toString());
        GameRegistry.registerTileEntity(INFUSER.getTileEntityClass(), INFUSER.getRegistryName().toString());
        GameRegistry.registerTileEntity(TRI_TANK.getTileEntityClass(), TRI_TANK.getRegistryName().toString());
        GameRegistry.registerTileEntity(CENTRIFUGE.getTileEntityClass(), CENTRIFUGE.getRegistryName().toString());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (Block block: allBlocks) {
            if (block instanceof IBlockBase) registry.register(((IBlockBase)block).createItemBlock());
            else registry.register(new ItemBlock(block));
        }

    }

    public static void registerModels() {
        for (Block block: allBlocks) {
            if (block instanceof IBlockBase) ((IBlockBase)block).registerItemModel(Item.getItemFromBlock(block));
        }
    }
}
