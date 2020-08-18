笔记

- Block类的onBlockActivated方法会执行两次,分别是主手和副手

- 服务端的播放声音是除去了传入的玩家的,因为向传入的玩家播放声音交给了客户端,如果你在服务端播放,别传玩家进去!

- 门的方块掉落不在harvestBlock中,是在neighborChanged中处理的

- 写te的时候别忘了覆盖shouldRefresh!不覆写会导致更新方块状态同时更新te,然后te里存的数据就没啦

- 注册药水时调用registerPotionAttributeModifier要在setPotionName之后

- 如果想让方块徒手掉落物品,但用工具挖掘时更快的话调用Block类的setHarvestLevel("工具种类", 0)
