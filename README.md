# BadgeView
一个可以自由定制外观、支持拖拽消除的MaterialDesign风格Android BadgeView

![](https://github.com/qstumn/BadgeView/blob/master/demo.png?raw=true)

![](https://github.com/qstumn/BadgeView/blob/master/demo_gif.gif?raw=true)

##Change Log

```
v1.0.3
1. 修复在hide、bindTarget在某些情况下会出现异常崩溃的BUG
2. BadgeGravity新增CENTER | TOP、CENTER | BOTTOM、CENTER | START、CENTER | END
``` 

###一些特性
* 随意定制外观，包括Badge位置、底色、阴影、文字颜色、大小、内外边距等

* Badge数字小于0时显示dot，等于0时隐藏整个Badge，在普通模式下超过99时显示99+，精确模式下显示具体值

* 支持类似QQ的拖拽消除效果（默认关闭）

* 支持以动画的方式隐藏Badge

## how to use:
###1. gradle
```
    compile 'q.rorbin:badgeview:1.0.3'
```

###2. code
```
new QBadgeView(context).bindTarget(textview).setBadgeNumber(5);
```    
注：请不要在xml中创建QBadgeView

###3. 方法说明
  code | 说明
  --- | ---
setBadgeNumber | 设置数值
setBadgeNumberSize | 设置数值大小
setBadgeNumberColor | 设置数值颜色
setExactMode | 设置是否显示精确模式数值
setBadgeGravity | 设置Badge相对于TargetView的位置
setGravityOffset | 设置外边距
setBadgePadding | 设置内边距
setBadgeBackgroundColor | 设置背景色
setShowShadow | 设置是否显示阴影
setOnDragStateChangedListener | 打开拖拽消除模式并设置监听
hide | 隐藏Badge
       
###4.更新计划

1 . 显示文字
2 . 自定义复杂的背景
3 . 多种隐藏动画

####Change Log History
```
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
