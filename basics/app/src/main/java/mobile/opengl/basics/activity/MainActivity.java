package mobile.opengl.basics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import mobile.opengl.basics.R;

public class MainActivity extends AppCompatActivity {

    private final static String[] SAMPLE_NAMES = {
            "1 - White Triangle",
            "2a - Colors",
            "2b - Indices",
            "3a - Rectangles",
            "3b - Rectangles Depth",
            "4a - Projection Matrix",
            "4b - View Matrix",
            "4c - Model Matrix",
            "4d - Animation"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GLActivity.class);
                StringBuilder sampleName = new StringBuilder();
                sampleName.append(((Button) v).getText());
                i.putExtra(GLActivity.INTENT_SAMPLE_NAME, sampleName.toString().replaceAll(" |-",
                        ""));
                startActivity(i);
            }
        };

        LinearLayout view = findViewById(R.id.view);
        for (String sample : SAMPLE_NAMES) {
            Button button = new Button(this);
            button.setText(sample);
            button.setTransformationMethod(null);
            button.setGravity((Gravity.HORIZONTAL_GRAVITY_MASK & Gravity.START) | (Gravity.VERTICAL_GRAVITY_MASK & Gravity.CENTER));
            button.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT));
            button.setOnClickListener(listener);
            view.addView(button);
        }

    }

}
