package com.lf.cocos2ddemo.layer;

import android.view.MotionEvent;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 * 第一个图层
 */
public class FirstLayer extends CCLayer{
    private static final int TAG = 1;//精灵的标记

    public FirstLayer() {
        CCSprite sprite = CCSprite.sprite("z_1_attack_01.png");// 参数表示图片在assets目录下的相对路径

        sprite.setAnchorPoint(0, 0);// 设置锚点, 默认是(0.5,0.5)也就是默认锚点是在图片的中心位置
        sprite.setPosition(ccp(100, 0));// 设置僵尸的显示位置
        // sprite.setFlipX(true);//水平翻转
        // sprite.setFlipY(true);//垂直翻转
        // sprite.setOpacity(100);//设置不透明度,0-255,255表示完全不透明
        // sprite.setScale(2);//宽高变为2倍,面积是4倍
        // this.addChild(sprite);//添加一个精灵

        this.addChild(sprite, 1, TAG);//参2表示展现优先级,越大,越展示在上面, 默认是0

        CCSprite sprite2 = CCSprite.sprite("z_1_attack_01.png");
        sprite2.setAnchorPoint(0, 0);// 设置锚点, 默认是0.5,0.5
        sprite2.setPosition(ccp(100, 0));// 设置僵尸的显示位置
        sprite2.setScale(2);

        this.addChild(sprite2);

        setIsTouchEnabled(true);//打开点击事件, 默认是关闭的, 为了避免由于点击导致的bug
    }

    /**
     * 点击监听
     */
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        CGPoint convertTouchToNodeSpace = convertTouchToNodeSpace(event);// 转化为cocos2d的坐标体系
        // 判断该坐标点有没有落在僵尸上
        CCSprite sprite = (CCSprite) this.getChildByTag(TAG);// 通过tag找到僵尸对象

        //判断是否落在僵尸的矩形区域内
        if (CGRect.containsPoint(sprite.getBoundingBox(),
                convertTouchToNodeSpace)) {
            System.out.println("被点击了!!!");
            sprite.setVisible(false);//隐藏僵尸
            //sprite.removeSelf();//僵尸自杀
        }

        return super.ccTouchesBegan(event);
    }
}
