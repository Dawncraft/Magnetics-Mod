package io.github.dawncraft.magnetics.jei;

import io.github.dawncraft.magnetics.MagneticsMod;
import io.github.dawncraft.magnetics.block.ModBlocks;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class LightningStrikeRecipeCategory implements IRecipeCategory<LightningStrikeRecipe>
{
    public static final String LIGHTNING_STRIKE = "magnetics.lightingStrike";
    protected static final int inputSlot = 0;
    protected static final int outputSlot = 1;

    private final String localizedName;
    private final IDrawable icon;
    private final IDrawable background;

    public LightningStrikeRecipeCategory(IGuiHelper guiHelper)
    {
        this.localizedName = I18n.translateToLocal("gui.magnetics.jei.category.lightingStrike");
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.LIGHTNING_ARRESTER));
        this.background = guiHelper.createDrawable(MagneticsJEIPlugin.RECIPE_GUI, 0, 0, 78, 44);
    }

    @Override
    public String getModName()
    {
        return MagneticsMod.NAME;
    }

    @Override
    public String getTitle()
    {
        return this.localizedName;
    }

    @Override
    public IDrawable getIcon()
    {
        return this.icon;
    }

    @Override
    public IDrawable getBackground()
    {
        return this.background;
    }

    @Override
    public String getUid()
    {
        return LIGHTNING_STRIKE;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, LightningStrikeRecipe recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(inputSlot, true, 0, 26);
        guiItemStacks.init(outputSlot, false, 60, 26);

        guiItemStacks.set(ingredients);
    }
}
