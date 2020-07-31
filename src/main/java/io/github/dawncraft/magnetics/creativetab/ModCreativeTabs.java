package io.github.dawncraft.magnetics.creativetab;

import io.github.dawncraft.magnetics.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Mod's creative tabs
 * 
 * @author QingChenW
 */
public class ModCreativeTabs
{
    public static final CreativeTabs MAGNETICS;

    static
    {
        MAGNETICS = new CreativeTabs("Magnetics")
        {
            @Override
            public ItemStack createIcon()
            {
                return new ItemStack(ModItems.MAGNET);
            }
        };
    }
}
