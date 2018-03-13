# SuperLike
仿今日头条点赞喷射表情动画

主要用于点赞防抖事件的交互提示，这里只提供交互View 防抖功能可以根据Rx自行实现
## 演示
 ![image](https://github.com/Qiu800820/SuperLike/blob/master/img/Animation.gif)

## 使用
```javascript
compile 'com.sum.slike:library:0.2.0'
```


## 参数含义

<table>
  <tdead>
    <tr>
      <th align="center">自定义属性名字</th>
      <th align="center">参数含义</th>
    </tr>
  </tdead>
  <tbody>
    <tr>
      <td align="center">eruption_element_amount</td>
      <td align="center">一次喷射的element数量</td>
    </tr>
    <tr>
      <td align="center">max_eruption_total</td>
      <td align="center">最大同时喷射次数</td>
    </tr>
    </tbody>
</table>

## 代码演示
### 1.基础样式(666 + 文字)
```java
    BitmapProvider.Provider provider = new BitmapProvider.Builder(this).build();
    superLikeLayout.setProvider(provider);
    superLikeLayout.launch(x, y);
```
![image](https://github.com/Qiu800820/SuperLike/blob/master/img/default_style.png)
### 2.自定义样式(随机emoji + 自定义文字)
 * `DrawableArray`是喷射Emoji的Drawable。
 * `NumberDrawableArray`是连续点赞数字Drawable(0~10)
 * `LevelDrawableArray`是连续点赞等级Drawable(目前分为3个等级 0~10, 10~20, 20~30+)
```java
	BitmapProvider.Provider provider = new BitmapProvider.Builder(this)
                        .setDrawableArray(new int[]{R.mipmap.emoji_1, R.mipmap.emoji_2, R.mipmap.emoji_3, R.mipmap.emoji_4, R.mipmap.emoji_5, R.mipmap.emoji_6,
                                R.mipmap.emoji_7, R.mipmap.emoji_8, R.mipmap.emoji_9, R.mipmap.emoji_10, R.mipmap.emoji_11, R.mipmap.emoji_12, R.mipmap.emoji_13,
                                R.mipmap.emoji_14, R.mipmap.emoji_15, R.mipmap.emoji_16, R.mipmap.emoji_17, R.mipmap.emoji_18, R.mipmap.emoji_19, R.mipmap.emoji_20})
                        .setNumberDrawableArray(new int[]{R.mipmap.multi_digg_num_0, R.mipmap.multi_digg_num_1, R.mipmap.multi_digg_num_2, R.mipmap.multi_digg_num_3,
                                R.mipmap.multi_digg_num_4, R.mipmap.multi_digg_num_5, R.mipmap.multi_digg_num_6, R.mipmap.multi_digg_num_7,
                                R.mipmap.multi_digg_num_8, R.mipmap.multi_digg_num_9})
                        .setLevelDrawableArray(new int[]{R.mipmap.multi_digg_word_level_1, R.mipmap.multi_digg_word_level_2, R.mipmap.multi_digg_word_level_3})
                        .build();
    superLikeLayout.setProvider(provider);
    superLikeLayout.launch(x, y);
```
![image](https://github.com/Qiu800820/SuperLike/blob/master/img/custom_style.png)
### 联系方式
 * 邮箱地址： qiujunsen@163.com, qiujunsen@gmail.com