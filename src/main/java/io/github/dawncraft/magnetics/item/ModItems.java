package io.github.dawncraft.magnetics.item;

import io.github.dawncraft.magnetics.ConfigLoader;
import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.api.item.ItemArmorSuit;
import io.github.dawncraft.magnetics.api.item.ItemModAxe;
import io.github.dawncraft.magnetics.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Register mod items.
 *
 * @author QingChenW
 */
@Mod.EventBusSubscriber(modid = MagneticsMod.MODID)
public final class ModItems
{
    public static final Item.ToolMaterial MAGNET_TOOL = EnumHelper.addToolMaterial("MAGNET", 2, 285, 6.0F, 2.0F, 11);
    public static final ItemArmor.ArmorMaterial MAGNET_ARMOR = EnumHelper.addArmorMaterial("MAGNET", MagneticsMod.MODID + ":" + "magnet", 17, new int[] { 1, 5, 4, 2 }, 11, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);

    // Redstone
    public static Item MAGNET_DOOR;
    public static Item MAGNET_CARD;

    // Materials
    public static Item MAGNET;
    public static Item MAGNET_INGOT;
    public static Item MAGNET_BALL;

    // Tools
    public static Item MAGNET_AXE;
    public static Item MAGNET_PICKAXE;
    public static Item MAGNET_SHOVEL;
    public static Item MAGNET_HAMMER;
    public static Item MAGNET_HOE;

    // Compat
    public static Item MAGNET_HELMET;
    public static Item MAGNET_CHESTPLATE;
    public static Item MAGNET_LEGGINGS;
    public static Item MAGNET_BOOTS;
    public static Item MAGNET_SWORD;
    public static Item MAGNET_WAND;

    // Other mods
    @ObjectHolder("ic2:re_battery")
    public static final Item RE_BATTERY = null;
    @ObjectHolder("ic2:advanced_re_battery")
    public static final Item ADVANCED_RE_BATTERY = null;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        boolean flag = ConfigLoader.useSeparateCreativeTab;

        MAGNET_TOOL.setRepairItem(new ItemStack(MAGNET_INGOT));

        event.getRegistry().registerAll(
                createItemForBlock(ModBlocks.MAGNET_ORE),
                createItemForBlock(ModBlocks.MAGNET_BLOCK),
                createItemForBlock(ModBlocks.MAGNET_CHEST),
                createItemForBlock(ModBlocks.MAGNET_RAIL),
                createItemForBlock(ModBlocks.LIGHTNING_ARRESTER),

                MAGNET_DOOR = new ItemMagnetDoor().setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.REDSTONE).setTranslationKey("magnetDoor").setRegistryName("magnet_door"),

                MAGNET = new Item().setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.MATERIALS).setTranslationKey("magnet").setRegistryName("magnet"),
                MAGNET_INGOT = new Item().setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.MATERIALS).setTranslationKey("magnetIngot").setRegistryName("magnet_ingot"),
                MAGNET_BALL = new ItemMagnetBall().setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.MISC).setTranslationKey("magnetBall").setRegistryName("magnet_ball"),

                MAGNET_CARD = new ItemMagnetCard().setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.REDSTONE).setTranslationKey("magnetCard").setRegistryName("magnet_card"),

                MAGNET_AXE = new ItemModAxe(MAGNET_TOOL, 8.0F, -3.2F).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.TOOLS).setTranslationKey("magnetAxe").setRegistryName("magnet_axe"),
                MAGNET_PICKAXE = new ItemPickaxe(MAGNET_TOOL).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.TOOLS).setTranslationKey("magnetPickaxe").setRegistryName("magnet_pickaxe"),
                MAGNET_SHOVEL = new ItemSpade(MAGNET_TOOL).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.TOOLS).setTranslationKey("magnetSpade").setRegistryName("magnet_shovel"),
                MAGNET_HOE = new ItemHoe(MAGNET_TOOL).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.TOOLS).setTranslationKey("magnetHoe").setRegistryName("magnet_hoe"),
                // MAGNET_HAMMER = new ItemHammer(MAGNET_TOOL).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.TOOLS).setTranslationKey("magnetHammer").setRegistryName("magnet_hammer"),
                MAGNET_SWORD = new ItemMagnetSword(MAGNET_TOOL).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.COMBAT).setTranslationKey("magnetSword").setRegistryName("magnet_sword"),
                MAGNET_WAND = new ItemMagnetWand(MAGNET_TOOL).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.COMBAT).setTranslationKey("magnetWand").setRegistryName("magnet_wand"),
                MAGNET_HELMET = new ItemArmorSuit(MAGNET_ARMOR, MAGNET_ARMOR.ordinal(), EntityEquipmentSlot.HEAD).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.COMBAT).setTranslationKey("magnetHelmet").setRegistryName("magnet_helmet"),
                MAGNET_CHESTPLATE = new ItemArmorSuit(MAGNET_ARMOR, MAGNET_ARMOR.ordinal(), EntityEquipmentSlot.CHEST).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.COMBAT).setTranslationKey("magnetChestplate").setRegistryName("magnet_chestplate"),
                MAGNET_LEGGINGS = new ItemArmorSuit(MAGNET_ARMOR, MAGNET_ARMOR.ordinal(), EntityEquipmentSlot.LEGS).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.COMBAT).setTranslationKey("magnetLeggings").setRegistryName("magnet_leggings"),
                MAGNET_BOOTS = new ItemArmorSuit(MAGNET_ARMOR, MAGNET_ARMOR.ordinal(), EntityEquipmentSlot.FEET).setCreativeTab(flag ? ModCreativeTabs.MAGNETICS : CreativeTabs.COMBAT).setTranslationKey("magnetBoots").setRegistryName("magnet_boots")
                );
    }

    /**
     * Create an item for the block
     *
     * @param block
     * @return An item for the block
     */
    private static Item createItemForBlock(Block block)
    {
        return new ItemBlock(block).setRegistryName(block.getRegistryName());
    }
}
