package cos.testapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etNumbA, etNumbB, etPause, etResult;

    final int TASK_CODE = 1;
    public final static int
            STATUS_FINISH = 200;
    public final static String PARAM_NUMB_A = "numb_a",
            PARAM_NUMB_B = "numb_b",
            PARAM_PAUSE = "pause",
            PARAM_PINTENT = "pendingIntent",
            PARAM_RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumbA = (EditText)findViewById(R.id.etNumA);
        etNumbB = (EditText)findViewById(R.id.etNumB);
        etPause = (EditText)findViewById(R.id.etNumC);
        etResult = (EditText)findViewById(R.id.etNumD);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numbA = checkNumb(etNumbA.getText().toString());
                etNumbA.setText(String.valueOf(numbA));
                int numbB = checkNumb(etNumbB.getText().toString());
                etNumbB.setText(String.valueOf(numbB));
                int pause = checkNumb(etPause.getText().toString());
                etPause.setText(String.valueOf(pause));

                PendingIntent pendingIntent = createPendingResult(TASK_CODE, new Intent(), 0);
                Intent intent = new Intent(MainActivity.this, CalcService.class)
                        .putExtra(PARAM_NUMB_A, numbA)
                        .putExtra(PARAM_NUMB_B, numbB)
                        .putExtra(PARAM_PAUSE, pause)
                        .putExtra(PARAM_PINTENT, pendingIntent);
                startService(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((resultCode == STATUS_FINISH) && (requestCode == TASK_CODE)){

            int result = data.getIntExtra(PARAM_RESULT, 0);
            etResult.setText(String.valueOf(result));
            Toast.makeText(getApplicationContext(), "Сумма равна " + result,
                    Toast.LENGTH_SHORT).show();
        }

    }

    private int checkNumb(String numbStr) {
        return numbStr.isEmpty() ? 0 : Integer.parseInt(numbStr);
    }

}
