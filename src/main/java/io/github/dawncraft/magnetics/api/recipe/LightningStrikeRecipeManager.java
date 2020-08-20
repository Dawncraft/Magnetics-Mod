package io.github.dawncraft.magnetics.api.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 *
 *
 * @author QingChenW
 */
public abstract class LightningStrikeRecipeManager
{
    public abstract boolean hasRecipe(Item input);

    public abstract void addRecipe(Item input, IRecipe recipe);

    public abstract ItemStack getResult(ItemStack input);

    public static interface IRecipe
    {
        boolean matches(ItemStack input);
        ItemStack getResult(ItemStack input);
    }

    public static class ItemRecipe implements IRecipe
    {
        private final Item input;
        private final ItemStack output;

        public ItemRecipe(Item input, ItemStack output)
        {
            this.input = input;
            this.output = output;
        }

        @Override
        public boolean matches(ItemStack input)
        {
            return input.getItem() == this.input;
        };

        @Override
        public ItemStack getResult(ItemStack input)
        {
            return this.output.copy();
        }
    }

    private static LightningStrikeRecipeManager instance;
    public static LightningStrikeRecipeManager getInstance()
    {
        if (instance == null)
        {
            try
            {
                Class<?> implClass = Class.forName("io.github.dawncraft.magnetics.recipe.LightningStrikeRecipeManagerImpl");
                instance = (LightningStrikeRecipeManager) implClass.getDeclaredField("INSTANCE").get(null);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Cannot find implementation", e);
            }
        }
        return instance;
    }
}
