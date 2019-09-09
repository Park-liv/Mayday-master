package application.minseong.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private ImageView splash_top,splash_below;
    private TextView splash_text,splash_text2;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_top = (ImageView)findViewById(R.id.splash_top);
        splash_below = (ImageView)findViewById(R.id.splash_below);
        splash_text = (TextView)findViewById(R.id.splash_text);
        splash_text2 = (TextView)findViewById(R.id.splash_text2);

        animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.pushdown);
        splash_top.setAnimation(animation);

        animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.pushdown);
        splash_text.setAnimation(animation);

        animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.pushright);
        splash_text2.setAnimation(animation);

        animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.pushright);
        splash_below.setAnimation(animation);



        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(4000);
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                super.run();
            }
        };

        thread.start();
    }
}
