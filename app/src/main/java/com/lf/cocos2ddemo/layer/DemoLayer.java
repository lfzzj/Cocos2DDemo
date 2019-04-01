package com.lf.cocos2ddemo.layer;

import android.view.MotionEvent;

import com.lf.cocos2ddemo.R;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCJumpBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCTMXObjectGroup;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.particlesystem.CCParticleSnow;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.util.CGPointUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 小案例
 * 在一个九曲十八弯的小路上，一个僵尸冒着大雪前行。最后雪停了，僵尸高兴的跳起了骑马舞
 */
public class DemoLayer extends CCLayer {

    private ArrayList<CGPoint> mPoints;
    private CCSprite sprite;

    private int index;// 表示僵尸当前的位置
    private int speed = 80;// 僵尸移动速度
    private CCTMXTiledMap map;
    private CCParticleSystem system;

    public DemoLayer() {
        sprite = CCSprite.sprite("z_1_01.png");
        sprite.setFlipX(true);//水平翻转
        sprite.setScale(0.5);//缩放
        sprite.setAnchorPoint(0.5f, 0);// 设置锚点为两脚之间
        loadMap();

        setIsTouchEnabled(true);// 打开触摸事件开关
    }

    /**
     * 加载地图
     */
    private void loadMap() {
        map = CCTMXTiledMap.tiledMap("map.tmx");

        // 保证地图触摸可以正常移动
        map.setAnchorPoint(0.5f, 0.5f);// 注意:地图锚点设置为中心位置,默认是0,0
        map.setPosition(ccp(map.getContentSize().width / 2,
                map.getContentSize().height / 2));// 设置位置为地图的中心点

        // 初始化所有的坐标点
        mPoints = new ArrayList<CGPoint>();
        CCTMXObjectGroup objectGroupNamed = map.objectGroupNamed("road");// 根据名字,找到objectgroup
        ArrayList<HashMap<String, String>> objects = objectGroupNamed.objects;// 所有点的集合

        for (HashMap<String, String> hashMap : objects) {
            Integer x = Integer.parseInt(hashMap.get("x"));
            Integer y = Integer.parseInt(hashMap.get("y"));

            mPoints.add(ccp(x, y));
        }

        this.addChild(map);

        sprite.setPosition(mPoints.get(0));// 设置僵尸初始位置
        map.addChild(sprite);// 把僵尸添加給地圖

        // CCFollow follow = CCFollow.action(sprite);//参数表示跟随的对象
        // map.runAction(follow);//地图跟随僵尸,如果要实现地图跟随效果,需要把地图的锚点和位置改为默认值

        walk();// 播放行走的动画
        moveNext();// 开始行走
        particleSystem();
    }

    /**
     * 粒子系统
     */
    private void particleSystem() {
        system = CCParticleSnow.node();// 初始化下雪效果
        // CCParticleSystem system = CCParticleFire.node();
        // system.setScale(2);//大小
        // system.setSpeed(10);//速度
        system.setTexture(CCTextureCache.sharedTextureCache().addImage(
                "snow.png"));// 设置雪花图片
        this.addChild(system, 1);
    }

    /**
     * 如果要通过反射调用此方法,必须是public, CCCallFunc.action(this, "moveNext")
     */
    public void moveNext() {
        index++;
        if (index < mPoints.size()) {
            CCMoveTo move = CCMoveTo.action(
                    CGPointUtil.distance(mPoints.get(index - 1),
                            mPoints.get(index))
                            / speed, mPoints.get(index));// 用距离除以速度得到时间,保证僵尸匀速前进

            CCSequence s = CCSequence.actions(move,
                    CCCallFunc.action(this, "moveNext"));// 通过反射调用方法,实现类似递归循环

            sprite.runAction(s);
        } else {
            System.out.println("僵尸走到头了...");
            system.stopSystem();// 停止粒子系统
            dance();
        }
    }

    /**
     * 僵尸跳舞
     */
    private void dance() {
        sprite.setAnchorPoint(0.5f, 0.5f);
        CCJumpBy jump = CCJumpBy.action(1, ccp(-20, 10), 20, 3);
        CCRotateBy rotate = CCRotateBy.action(1, 360);
        CCSpawn spawn = CCSpawn.actions(jump, rotate);

        CCSequence s = CCSequence.actions(spawn, spawn.reverse());
        CCRepeatForever repeat = CCRepeatForever.action(s);

        sprite.runAction(repeat);

        SoundEngine engine = SoundEngine.sharedEngine();
        engine.playSound(CCDirector.theApp, R.raw.psy, true);
    }

    /**
     * 僵尸行走
     */
    private void walk() {
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
     * 响应触摸移动事件
     */
    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        map.touchMove(event, map);// 地图移动
        return super.ccTouchesMoved(event);
    }

    /**
     * 点击屏幕,游戏暂停
     */
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        this.onExit();// 游戏暂停
        this.getParent().addChild(new PauseLayer());// 给父控件添加暂停图层
        return super.ccTouchesBegan(event);
    }

    /**
     * 暂停图层
     *
     * @author Kevin
     *
     */
    class PauseLayer extends CCLayer {
        private CCSprite heart;

        public PauseLayer() {
            heart = CCSprite.sprite("heart.png");
            CGSize winSize = CCDirector.sharedDirector().winSize();
            heart.setPosition(winSize.width / 2, winSize.height / 2);

            this.addChild(heart);

            setIsTouchEnabled(true);
        }

        @Override
        public boolean ccTouchesBegan(MotionEvent event) {
            CGPoint convertTouchToNodeSpace = convertTouchToNodeSpace(event);

            // 小心脏被点击
            if (CGRect.containsPoint(heart.getBoundingBox(),
                    convertTouchToNodeSpace)) {
                DemoLayer.this.onEnter();// 游戏继续
                this.removeSelf();// 移除暂停图层
            }

            return super.ccTouchesBegan(event);
        }
    }
}
