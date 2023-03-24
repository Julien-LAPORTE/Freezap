package com.openclassrooms.freezap.Controllers;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.openclassrooms.freezap.R;
import com.openclassrooms.freezap.Utils.MyAsyncTask;
import com.openclassrooms.freezap.Utils.MyAsyncTaskLoader;
import com.openclassrooms.freezap.Utils.MyHandlerThread;
import com.openclassrooms.freezap.Utils.Utils;

public class MainActivity extends AppCompatActivity implements MyAsyncTask.Listeners,
        LoaderManager.LoaderCallbacks<Long> {
private static final int TASK_ID = 100;

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
        //Try to resume possible loading AsyncTask
        resumeAsyncTaskLoaderIfPossible();
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
                startAsyncTask();
                break;
            case 70:
                startAsyncTaskLoader();
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

    // 7 - Resume previous AsyncTaskLoader if still running
    private void resumeAsyncTaskLoaderIfPossible(){
        if (getSupportLoaderManager().getLoader(TASK_ID) != null && getSupportLoaderManager()
                .getLoader(TASK_ID).isStarted()) {
            getSupportLoaderManager().initLoader(TASK_ID, null, this);
            updateUIBeforeTask();
        }
    }
    private void startAsyncTaskLoader() {
        getSupportLoaderManager().restartLoader(TASK_ID, null, this);
    }
    // 2 - Implements callback methods
    @Override
    public Loader<Long> onCreateLoader(int id, Bundle args) {
        Log.e("TAG", "On Create !");
        updateUIBeforeTask();
        return new MyAsyncTaskLoader(this); // 5 - Return a new AsyncTaskLoader
    }

    @Override
    public void onLoadFinished(Loader<Long> loader, Long data) {
        Log.e("TAG", "On Finished !");
        loader.stopLoading(); // 6 - Force loader to stop
        updateUIAfterTask(data);
    }

    @Override
    public void onLoaderReset(Loader<Long> loader) {

    }

}
