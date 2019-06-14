package sykent.com.sykentlib;

import android.animation.ValueAnimator;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private EScrollView mEScrollView;

    private final int startX = 0;
    private final int deltaX = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initListener();
    }


    private void initListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1).setDuration(1000);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fraction = animation.getAnimatedFraction();
                        mButton.setTranslationX((int) (startX + deltaX * fraction));
                    }
                });
                valueAnimator.start();
            }
        });
    }

    private void findView() {
        mButton = findViewById(R.id.bt_scroll);
        mEScrollView = findViewById(R.id.scv_test);
    }
}
