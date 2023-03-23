package com.openclassrooms.freezap.Controllers;


import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.freezap.R;
import com.openclassrooms.freezap.Utils.MyAsyncTask;
import com.openclassrooms.freezap.Utils.MyHandlerThread;
import com.openclassrooms.freezap.Utils.Utils;

public class MainActivity extends AppCompatActivity implements MyAsyncTask.Listeners {

    //FOR DESIGN
    private ProgressBar m_progressBar;
    //FOR DATA
    private MyHandlerThread m_handlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get progressbar from layout
        m_progressBar = findViewById(R.id.activity_main_progress_bar);
        //Configure Handler Thread
        configureHandlerThread();
    }

    @Override
    protected void onDestroy() {
        // 3 - QUIT HANDLER THREAD (Free precious resources)
        m_handlerThread.quit();
        super.onDestroy();
    }
    // ------------
    // ACTIONS
    // ------------

    public void onClickButton(View v){
        int buttonTag = Integer.valueOf(v.getTag().toString());
        switch (buttonTag){
            case 10: // CASE USER CLICKED ON BUTTON "EXECUTE ACTION IN MAIN THREAD"
                Utils.executeLongActionDuring7seconds();
                break;
            case 20: // CASE USER CLICKED ON BUTTON "EXECUTE ACTION IN BACKGROUND"
                startHandlerThread();
                break;
            case 30:
                break;
            case 40:
                break;
            case 50:
                break;
            case 60: // CASE USER CLICKED ON BUTTON "EXECUTE ASYNCTASK"
                this.startAsyncTask();
                break;
            case 70:
                break;
        }
    }

    // 3 - We create and start our AsyncTask
    private void startAsyncTask() {
        new MyAsyncTask(this).execute();
    }
    private void configureHandlerThread() {
    m_handlerThread = new MyHandlerThread("MyAwesomeHanderThread", m_progressBar);
    }
    private void startHandlerThread() {
        m_handlerThread.startHandler();
    }

    @Override
    public void onPreExecute() {
// 2.1 - We update our UI before task (starting ProgressBar)
        updateUIBeforeTask();
    }

    @Override
    public void doInBackground() {

    }

    @Override
    public void onPostExecute(Long taskEnd) {
        updateUIAfterTask(taskEnd);
    }

    private void updateUIAfterTask(Long taskEnd) {
        m_progressBar.setVisibility(View.GONE);
        Toast.makeText(this,
                        "Task is finally finished at : "+taskEnd+" !", Toast.LENGTH_SHORT)
                .show();
    }

    private void updateUIBeforeTask() {
        m_progressBar.setVisibility(View.VISIBLE);
    }
}
