# BadgeView
[ ![Download](https://api.bintray.com/packages/qstumn/maven/badgeview/images/download.svg) ](https://bintray.com/qstumn/maven/badgeview/_latestVersion)

一个可以自由定制外观、支持拖拽消除的MaterialDesign风格Android BadgeView

![](https://github.com/qstumn/BadgeView/blob/master/demo.png?raw=true)

### 一些特性
* 随意定制外观，包括Badge位置、底色、边框、阴影、文字颜色(支持透明色)、大小、内外边距等

* Badge数字小于0时显示dot，等于0时隐藏整个Badge，在普通模式下超过99时显示99+，精确模式下显示具体值

* 支持设置文本内容

* 支持设置图片背景

* 支持类似QQ的拖拽消除效果（默认关闭）

* 支持以动画的方式隐藏Badge

![](https://github.com/qstumn/BadgeView/blob/master/demo_gif.gif?raw=true)

## how to use:
### 1. gradle
```groovy
    compile 'q.rorbin:badgeview:1.1.2'
```
VERSION_CODE : [here](https://github.com/qstumn/BadgeView/releases)

### 2. code
```java
new QBadgeView(context).bindTarget(textview).setBadgeNumber(5);
```    

### 3. 方法说明
  code | 说明
  --- | ---
setBadgeNumber | 设置Badge数字
setBadgeText | 设置Badge文本
setBadgeTextSize | 设置文本字体大小
setBadgeTextColor | 设置文本颜色
setExactMode | 设置是否显示精确模式数值
setBadgeGravity | 设置Badge相对于TargetView的位置
setGravityOffset | 设置外边距
setBadgePadding | 设置内边距
setBadgeBackgroundColor | 设置背景色
setBadgeBackground | 设置背景图片
setShowShadow | 设置是否显示阴影
setOnDragStateChangedListener | 打开拖拽消除模式并设置监听
stroke | 描边
hide | 隐藏Badge

### 4.在ListView或者RecyclerView中使用
可参考demo中[ListViewActivity](https://github.com/qstumn/BadgeView/blob/master/app/src/main/java/q/rorbin/badgeviewdemo/ListViewActivity.java)、[RecyclerViewActivity](https://github.com/qstumn/BadgeView/blob/master/app/src/main/java/q/rorbin/badgeviewdemo/RecyclerViewActivity.java)

### 5.一些注意事项
* 请不要在xml中创建Badge
* Badge和TargetView绑定是采用替换TargetView的Parent方式实现的，同时将Parent的Id和TargetView的Id设置成一样来保证不会在RelativeLayout中出现位置错乱问题，所以在bindTarget后再次使用findViewById(TargetViewId)得到的会是Parent而不是TargetView，此时建议使用Badge.getTargetView方法来获取TargetView，如果您有更好的解决方式并愿意和我分享，请在Issues中提交给我或者给我发邮件，谢谢。

### 6.更新计划
添加富文本内容

### 5.Thanks For

https://github.com/mabeijianxi/stickyDots

# LICENSE
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
