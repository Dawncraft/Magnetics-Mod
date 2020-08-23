package io.github.dawncraft.magnetics.jei;

import java.util.Map.Entry;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.api.recipe.LightningStrikeRecipeManager;
import io.github.dawncraft.magnetics.api.recipe.LightningStrikeRecipeManager.IRecipe;
import io.github.dawncraft.magnetics.block.ModBlocks;
import io.github.dawncraft.magnetics.item.ModItems;
import io.github.dawncraft.magnetics.recipe.LightningStrikeRecipeManagerImpl;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JEIPlugin
public class MagneticsJEIPlugin implements IModPlugin
{
    public static final ResourceLocation RECIPE_GUI = new ResourceLocation(MagneticsMod.MODID, "textures/gui/jei.png");
    private final static ISubtypeInterpreter NO_SUBTYPE = new ISubtypeInterpreter()
    {
        @Override
        public String apply(ItemStack itemStack)
        {
            return NONE;
        }
    };

    public MagneticsJEIPlugin() {}

    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.LIGHTNING_ARRESTER), LightningStrikeRecipeCategory.LIGHTNING_STRIKE);
        registry.handleRecipes(LightningStrikeRecipeManager.IRecipe.class, new IRecipeWrapperFactory<LightningStrikeRecipeManager.IRecipe>()
        {
            @Override
            public IRecipeWrapper getRecipeWrapper(LightningStrikeRecipeManager.IRecipe recipe)
            {
                for (Entry<Item, IRecipe> entry : LightningStrikeRecipeManagerImpl.INSTANCE.getRecipes().entrySet())
                {
                    if (entry.getValue() == recipe)
                    {
                        ItemStack input = new ItemStack(entry.getKey());
                        return new LightningStrikeRecipe(input, recipe.getResult(input));
                    }
                }
                return null;
            }
        }, LightningStrikeRecipeCategory.LIGHTNING_STRIKE);
        registry.addRecipes(LightningStrikeRecipeManagerImpl.INSTANCE.getRecipes().values(), LightningStrikeRecipeCategory.LIGHTNING_STRIKE);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
    {
        subtypeRegistry.registerSubtypeInterpreter(ModItems.MAGNET_SWORD, NO_SUBTYPE);
        subtypeRegistry.registerSubtypeInterpreter(ModItems.MAGNET_WAND, NO_SUBTYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        registry.addRecipeCategories(new LightningStrikeRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {}

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}
}
