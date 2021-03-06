package com.elytradev.opaline;

import com.elytradev.concrete.inventory.IContainerInventoryHolder;
import com.elytradev.concrete.inventory.gui.client.ConcreteGui;
import com.elytradev.concrete.network.NetworkContext;
import com.elytradev.opaline.block.ModBlocks;
import com.elytradev.opaline.client.OpalineTab;
import com.elytradev.opaline.container.CentrifugeContainer;
import com.elytradev.opaline.container.DistillerContainer;
import com.elytradev.opaline.container.InfuserContainer;
import com.elytradev.opaline.container.TriTankContainer;
import com.elytradev.opaline.item.ModItems;
import com.elytradev.opaline.network.PacketButtonClick;
import com.elytradev.opaline.proxy.CommonProxy;
import com.elytradev.opaline.tile.TileEntityCentrifuge;
import com.elytradev.opaline.tile.TileEntityDistiller;
import com.elytradev.opaline.tile.TileEntityInfuser;
import com.elytradev.opaline.tile.TileEntityTriTank;
import com.elytradev.opaline.util.OpalineLog;
import com.elytradev.opaline.util.OpalineRecipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


@Mod(modid = Opaline.modId, name = Opaline.name, version = Opaline.version)
public class Opaline {
    public static final String modId = "opaline";
    public static final String name  = "Opaline";
    public static final String version = "@VERSION@";

    public static NetworkContext CONTEXT;

    @Mod.Instance(modId)
    public static Opaline instance;

    public static final OpalineTab creativeTab = new OpalineTab();

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @SidedProxy(serverSide = "com.elytradev.opaline.proxy.CommonProxy", clientSide = "com.elytradev.opaline.proxy.ClientProxy")
    public static CommonProxy proxy;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OpalineLog.info(name + " is loading!");

        CONTEXT = NetworkContext.forChannel("opaline");
        CONTEXT.register(PacketButtonClick.class);

        MinecraftForge.EVENT_BUS.register(OpalineRecipes.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new IGuiHandler() {
            public static final int DISTILLER = 0;
            public static final int INFUSER = 1;
            public static final int TRI_TANK = 2;
            public static final int CENTRIFUGE = 3;
            @Nullable
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                switch (ID) {
                    case DISTILLER:
                        return new DistillerContainer(
                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                                (TileEntityDistiller)world.getTileEntity(new BlockPos(x,y,z)));
                    case INFUSER:
                        return new InfuserContainer(
                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                                (TileEntityInfuser)world.getTileEntity(new BlockPos(x,y,z)));
                    case TRI_TANK:
                        return new TriTankContainer(
                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                                (TileEntityTriTank)world.getTileEntity(new BlockPos(x,y,z)));
                    case CENTRIFUGE:
                        return new CentrifugeContainer(
                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                                (TileEntityCentrifuge)world.getTileEntity(new BlockPos(x,y,z)));
                    default:
                        return null;
                }

            }

            @Nullable
            @Override
            @SideOnly(Side.CLIENT)
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                switch (ID) {
                    case DISTILLER:
                        DistillerContainer distillerContainer = new DistillerContainer(
                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                                (TileEntityDistiller)world.getTileEntity(new BlockPos(x,y,z)));
                        return new ConcreteGui(distillerContainer);
                    case INFUSER:
                        InfuserContainer infuserContainer = new InfuserContainer(
                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                                (TileEntityInfuser)world.getTileEntity(new BlockPos(x,y,z)));
                        return new ConcreteGui(infuserContainer);
                    case TRI_TANK:
                        TriTankContainer multiTankContainer = new TriTankContainer(
                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                                (TileEntityTriTank) world.getTileEntity(new BlockPos(x,y,z)));
                        return new ConcreteGui(multiTankContainer);
                    case CENTRIFUGE:
                        CentrifugeContainer centrifugeContainer = new CentrifugeContainer(
                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                                (TileEntityCentrifuge) world.getTileEntity(new BlockPos(x,y,z)));
                        return new ConcreteGui(centrifugeContainer);
                    default:
                        return null;
                }

            }
        });
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //MinecraftForge.EVENT_BUS.register(new SoundRegisterListener());
        //MinecraftForge.EVENT_BUS.register(LightHandler.class);
        ModItems.registerOreDict(); // register oredict
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            ModItems.registerModels();
            ModBlocks.registerModels();
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            ModBlocks.register(event.getRegistry());
        }
    }
}
