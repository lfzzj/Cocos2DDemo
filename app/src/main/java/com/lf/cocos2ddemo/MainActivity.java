package com.lf.cocos2ddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lf.cocos2ddemo.layer.DemoLayer;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends AppCompatActivity {

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

//        CCLayer layer = CCLayer.node();//创建一个图层对象

//        FirstLayer firstLayer = new FirstLayer();
//        ActionLayer actionLayer = new ActionLayer();
        DemoLayer demoLayer = new DemoLayer();
        scene.addChild(demoLayer);//给场景添加图层

        director.runWithScene(scene);//导演运行场景
    }

    @Override
    protected void onResume() {
        super.onResume();
        director.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        director.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        director.end();
    }
}
