# BadgeView
一个可以自由定制外观、支持拖拽消除的MaterialDesign风格Android BadgeView

![](https://github.com/qstumn/BadgeView/blob/master/demo.png?raw=true)

![](https://github.com/qstumn/BadgeView/blob/master/demo_gif.gif?raw=true)

##[Change Log](https://github.com/qstumn/BadgeView/releases)

###一些特性
* 随意定制外观，包括Badge位置、底色、阴影、文字颜色、大小、内外边距等

* Badge数字小于0时显示dot，等于0时隐藏整个Badge，在普通模式下超过99时显示99+，精确模式下显示具体值

* 支持设置文本内容

* 支持类似QQ的拖拽消除效果（默认关闭）

* 支持以动画的方式隐藏Badge

## how to use:
###1. gradle
```groovy
    compile 'q.rorbin:badgeview:1.0.7'
```

###2. code
```java
new QBadgeView(context).bindTarget(textview).setBadgeNumber(5);
```    

###3. 方法说明
  code | 说明
  --- | ---
setBadgeNumber | 设置Badge数字
setBadgeText | 设置Badge文本
setBadgeTextSize | 设置数值大小
setBadgeTextColor | 设置数值颜色
setExactMode | 设置是否显示精确模式数值
setBadgeGravity | 设置Badge相对于TargetView的位置
setGravityOffset | 设置外边距
setBadgePadding | 设置内边距
setBadgeBackgroundColor | 设置背景色
setShowShadow | 设置是否显示阴影
setOnDragStateChangedListener | 打开拖拽消除模式并设置监听
hide | 隐藏Badge

###4.在ListView或者RecyclerView中使用
可参考demo中[ListViewActivity](https://github.com/qstumn/BadgeView/blob/master/app/src/main/java/q/rorbin/badgeviewdemo/ListViewActivity.java)、[RecyclerViewActivity](https://github.com/qstumn/BadgeView/blob/master/app/src/main/java/q/rorbin/badgeviewdemo/RecyclerViewActivity.java)

###5.一些注意事项
* 请不要在xml中创建Badge
* Badge和TargetView绑定是采用替换TargetView的Parent方式实现的，同时将Parent的Id和TargetView的Id设置成一样来保证不会在RelativeLayout中出现位置错乱问题，所以在bindTarget后再次使用findViewById(TargetViewId)得到的会是Parent而不是TargetView，此时建议使用Badge.getTargetView方法来获取TargetView，如果您有更好的解决方式并愿意和我分享，请在Issues中提交给我或者给我发邮件，谢谢。

###6.更新计划

1 . 自定义复杂的背景
2 . 多种隐藏动画

####Change Log History
```
v1.0.6
1、添加了setBadgeText方法
2、添加了setGravityOffset(float x , float y)方法，可以用来分别设置x和y方向的偏移量
3、优化隐藏动画，现在严格按照Badge的的大小生成隐藏动画
4、修复Badge宽度过宽时只有触摸中心才能触发拖拽的BUG
5、修改setBadgeNumberSize为setBadgeTextSize，setBadgeNumberColor改为setBadgeTextColor

v1.0.5
添加了getTargetView方法

v1.0.4
1. 修复在hide、bindTarget在某些情况下会出现异常崩溃的BUG
2. BadgeGravity新增CENTER | TOP、CENTER | BOTTOM、CENTER | START、CENTER | END
3. demo中添加了在ListView、RecyclerView中使用的例子

v1.0.2
1. 修复了在ScrollView中bindTarget会导致拖拽消除时起始拖拽位置不正确的BUG
2. 减少了隐藏动画的碎片数量,降低内存占用
``` 

###5.Thanks For

https://github.com/mabeijianxi/stickyDots

#LICENSE
```
Copyright 2017, RorbinQiu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
