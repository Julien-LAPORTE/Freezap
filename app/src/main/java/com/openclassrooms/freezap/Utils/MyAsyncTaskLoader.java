package com.openclassrooms.freezap.Utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class MyAsyncTaskLoader extends AsyncTaskLoader<Long> {

    public MyAsyncTaskLoader( Context context) {
        super(context);
    }

    @Nullable
    @Override
    public Long loadInBackground() {
        return Utils.executeLongActionDuring7seconds();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
