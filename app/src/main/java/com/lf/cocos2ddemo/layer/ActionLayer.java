package com.lf.cocos2ddemo.layer;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.ease.CCEaseOut;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCBezierBy;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCJumpBy;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.actions.interval.CCTintBy;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.types.CCBezierConfig;
import org.cocos2d.types.CGSize;

import java.util.ArrayList;

/**
 * 动作图层
 *
 * @author Kevin
 *
 */
public class ActionLayer extends CCLayer {

    private CCSprite sprite;
    private CCSprite sprite2;

    public ActionLayer() {
        sprite = CCSprite.sprite("z_1_attack_01.png");
        sprite.setAnchorPoint(ccp(0, 0));
        sprite.setPosition(0, 0);

        this.addChild(sprite);

        sprite2 = CCSprite.sprite("z_1_attack_01.png");
        sprite2.setAnchorPoint(ccp(0, 0));
        sprite2.setPosition(200, 100);

        // this.addChild(sprite2);

        // moveTo();
        // moveBy();
//         rotateBy();
        // rotateTo();
        // scale();
        // jump();
        // fade();
        // bezier();
        // ease();
        // blink();
        // tint();
        // demo();
        walk();
    }

    /**
     * 僵尸行走
     */
    private void walk() {
        sprite.setFlipX(true);
        CCMoveBy move = CCMoveBy.action(5, ccp(200, 0));
        sprite.runAction(move);

        // 初始化7帧图片
        ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
        String format = "z_1_%02d.png";// %02d表示两位数字,如果是个位,用0去补位,01,02,10,11
        for (int i = 1; i <= 7; i++) {
            frames.add(CCSprite.sprite(String.format(format, i))
                    .displayedFrame());
        }

        CCAnimation anim = CCAnimation.animation("walk", .2f, frames);// 参2表示每一帧显示时间
        CCAnimate animate = CCAnimate.action(anim);

        CCRepeatForever repeat = CCRepeatForever.action(animate);// 表示动画永远循环,
        // 需要有这句话,否则空指针异常

        sprite.runAction(repeat);
    }

    /**
     * 僵尸後空翻跳跃
     */
    private void demo() {
        sprite.setAnchorPoint(0.5f, 0.5f);
        CCJumpBy jump = CCJumpBy.action(2, ccp(100, 100), 150, 2);// 参3表示跳跃最高点,参4表示跳跃次数

        CCDelayTime delay = CCDelayTime.action(1);// 表示延时1秒执行, 也是一个CCAction
        CCRotateBy rotate = CCRotateBy.action(1, 360);

        CCSequence se = CCSequence.actions(delay, rotate);// 先延时,再旋转

        CCSpawn spawn = CCSpawn.actions(jump, se);// 多个动作同时执行

        CCSequence s = CCSequence.actions(spawn, spawn.reverse());

        CCRepeatForever repeat = CCRepeatForever.action(s);

        sprite.runAction(repeat);
    }

    /**
     * 颜色渐变
     */
    private void tint() {
        CCLabel label = CCLabel.labelWithString("那些年,在黑马苦逼的日子.....",
                "hkbd.ttf", 20);// 初始化一端文字, 参2表示字体类型,参3表示字体大小
        label.setColor(ccc3(100, 50, 0));

        CGSize winSize = CCDirector.sharedDirector().winSize();// 通过导演拿到屏幕尺寸
        label.setPosition(ccp(winSize.width / 2, winSize.height / 2));

        this.addChild(label);

        CCTintBy tint = CCTintBy.action(3, ccc3(50, -50, 100));
        CCSequence s = CCSequence.actions(tint, tint.reverse());

        CCRepeatForever repeat = CCRepeatForever.action(s);// 动作永远循环

        label.runAction(repeat);
    }

    /**
     * 闪烁
     */
    private void blink() {
        CCBlink action = CCBlink.action(10, 50);// 参2表示闪烁次数
        sprite.runAction(action);
    }

    /**
     * 加速度
     */
    private void ease() {
        CCMoveBy move = CCMoveBy.action(5, ccp(200, 0));
        // CCEaseIn action = CCEaseIn.action(move, 5);//参2表示速率, 渐快
        CCEaseOut action = CCEaseOut.action(move, 5);// 参2表示速率, 渐慢
        sprite.runAction(action);
    }

    /**
     * 贝塞尔曲线
     */
    private void bezier() {
        CCBezierConfig c = new CCBezierConfig();
        c.controlPoint_1 = ccp(100, 50);
        c.controlPoint_2 = ccp(150, 100);
        c.endPosition = ccp(200, 50);

        CCBezierBy action = CCBezierBy.action(3, c);
        sprite.runAction(action);
    }

    // 淡入淡出
    private void fade() {
        // CCFadeIn action = CCFadeIn.action(2);//淡入
        CCFadeOut action = CCFadeOut.action(2);// 淡出
        sprite.runAction(action);
    }

    /**
     * 跳跃
     */
    private void jump() {
        CCJumpBy action = CCJumpBy.action(2, ccp(100, 100), 150, 2);// 参3表示跳跃最高点,参4表示跳跃次数
        // action.reverse(),表示动作逆向执行!!!
        CCSequence s = CCSequence.actions(action, action.reverse());// 顺序执行一系列动作

        sprite.runAction(s);
    }

    /**
     * 缩放
     */
    private void scale() {
        CCScaleTo action = CCScaleTo.action(3, 2);// 参2表示缩放比例
        sprite.runAction(action);
    }
    /**
     * 旋转-逆时针
     */
    private void rotateTo() {
        CCRotateTo action = CCRotateTo.action(3, 270);// 逆时针旋转90度,达到目的就行
        sprite2.setAnchorPoint(0.5f, 0.5f);
        sprite2.runAction(action);
    }

    /**
     * 旋转-顺时针
     */
    private void rotateBy() {
        CCRotateBy action = CCRotateBy.action(3, 270);// 顺时针老老实实旋转270度
        sprite.setAnchorPoint(0.5f, 0.5f);
        sprite.runAction(action);
    }

    /**
     * 僵尸移动
     */
    private void moveTo() {
        CCMoveTo action = CCMoveTo.action(3, ccp(200, 0));
        sprite.runAction(action);
    }

    private void moveBy() {
        CCMoveBy action = CCMoveBy.action(3, ccp(200, 0));
        sprite.runAction(action);
    }

}
