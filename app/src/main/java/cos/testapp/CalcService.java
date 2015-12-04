package cos.testapp;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CalcService extends Service {

    ExecutorService executorService;
    public CalcService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newFixedThreadPool(1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int numbA = intent.getIntExtra(MainActivity.PARAM_NUMB_A, 0);
        int numbB = intent.getIntExtra(MainActivity.PARAM_NUMB_B, 0);
        int time = intent.getIntExtra(MainActivity.PARAM_PAUSE, 0);

        PendingIntent pi = intent.getParcelableExtra(MainActivity.PARAM_PINTENT);

        CalcRun calcRun = new CalcRun(numbA, numbB, time, startId, pi);
        executorService.execute(calcRun);

        return super.onStartCommand(intent, flags, startId);
    }

    public class CalcRun implements Runnable {

        private int numbA;
        private int numbB;
        private int time;
        private int startId;
        PendingIntent pendingIntent;

        public CalcRun(int numbA, int numbB, int time, int startId, PendingIntent pendingIntent) {
            this.numbA = numbA;
            this.numbB = numbB;
            this.time = time;
            this.pendingIntent = pendingIntent;
            this.startId = startId;
        }

        @Override
        public void run() {
            int res = numbA + numbB;
            try {
                TimeUnit.SECONDS.sleep(time);
                Intent intent = new Intent().putExtra(MainActivity.PARAM_RESULT, res);
                pendingIntent.send(CalcService.this, MainActivity.STATUS_FINISH, intent);
            } catch (InterruptedException | PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            stopSelfResult(startId);
        }
    }
}
