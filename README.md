# 磁学Mod
# Magnetics Mod

基于Forge开发

以GPLv3协议开源

API部分以LGPLv3协议开源

其模型,贴图,音效等资源仅以 CC 署名(BY) - 非商业性使用(NC) - 相同方式共享(SA) 协议供wiki和教程编写者使用

但其中包括但不限于合成表,战利品表,进度等资源依旧以GPLv3协议开源

## 笔记

- Block类的onBlockActivated方法会执行两次,分别是主手和副手

- 服务端的播放声音是除去了传入的玩家的,因为向传入的玩家播放声音交给了客户端,如果你在服务端播放,别传玩家进去!

- 门的方块掉落不在harvestBlock中,是在neighborChanged中处理的

- 写te的时候别忘了覆盖shouldRefresh!不覆写会导致更新方块状态同时更新te,然后te里存的数据就没啦

- 注册药水时调用registerPotionAttributeModifier要在setPotionName之后

- 如果想让方块用工具挖掘的话调用Block类的setHarvestLevel(工具种类, 等级)

- 升级ForgeGradle后FML报错: https://stackoverflow.com/questions/68377027/minecraft-forge-mod-loader-fml-loading-and-crashing-mc
