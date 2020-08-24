package io.github.dawncraft.magnetics.api.item;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

/**
 * IItemCard
 *
 * @since 1.1.0
 * @author QingChenW
 */
public interface IItemCard
{
    @Nullable
    String getData(ItemStack stack, String key);

    void setData(ItemStack stack, String key, String value);

    public static class WriteException extends RuntimeException
    {
        /**
         * read-only
         * write-protect
         * card-broken
         */
        public String reason;

        public WriteException(String reason)
        {
            super("Can't write data into the card: " + reason);
            this.reason = reason;
        }
    }
}
