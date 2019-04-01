package com.lf.cocos2ddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

/**
 * Create by LF
 * Data: 2019/4/1
 * Describe:
 */
public class DemoActivity extends AppCompatActivity {
    private CCDirector director;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CCGLSurfaceView view = new CCGLSurfaceView(this);//创建一个SurfaceView,类似导演眼前的小屏幕

        setContentView(view);

        //获取导演单例类
        director = CCDirector.sharedDirector();
        director.attachInView(view);//开启绘制线程

        CCScene scene = CCScene.node();//创建一个场景对象


    }
}
