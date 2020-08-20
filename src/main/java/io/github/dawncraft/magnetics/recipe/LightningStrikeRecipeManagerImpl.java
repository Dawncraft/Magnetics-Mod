package io.github.dawncraft.magnetics.recipe;

import java.util.HashMap;
import java.util.Map;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.api.recipe.LightningStrikeRecipeManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LightningStrikeRecipeManagerImpl extends LightningStrikeRecipeManager
{
    public static final LightningStrikeRecipeManagerImpl INSTANCE = new LightningStrikeRecipeManagerImpl();

    private final Map<Item, IRecipe> recipes = new HashMap<>();

    public Map<Item, IRecipe> getRecipes()
    {
        return this.recipes;
    }

    @Override
    public boolean hasRecipe(Item input)
    {
        return this.recipes.containsKey(input);
    }

    @Override
    public void addRecipe(Item input, IRecipe recipe)
    {
        if (!this.hasRecipe(input)) this.recipes.put(input, recipe);
        else MagneticsMod.logger().error("Has already registered recipe for item: " + input.getRegistryName());
    }

    @Override
    public ItemStack getResult(ItemStack input)
    {
        if (this.hasRecipe(input.getItem()))
        {
            return this.recipes.get(input.getItem()).getResult(input);
        }
        return input;
    }
}
