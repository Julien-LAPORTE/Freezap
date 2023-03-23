package com.openclassrooms.freezap.Utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import java.lang.ref.WeakReference;

public class MyHandlerThread extends HandlerThread {

    private WeakReference<ProgressBar> progressBarWeakReference;

    // 1 - Constructor
    public MyHandlerThread(String name, ProgressBar progressBar) {
        super(name);
        progressBarWeakReference = new WeakReference<>(progressBar);
    }

    // 2 - Public method that will start handler
    public void startHandler(){
        // 2.1 - Checking if progressbar is accessible, and setting it visible
        if (progressBarWeakReference.get() != null) progressBarWeakReference.get().setVisibility(View.VISIBLE);
        // 2.2 - Il est possible que nous ayons déjà lancé précédemment notre HandlerThread.
        // Pour cela, nous vérifions s'il fonctionne déjà et le lançons dans le cas échéant.
        if (!this.isAlive()) this.start();
        // 2.3 - Nous créons un nouvel Handler, afin de lui affecter par la suite, un exécutable contenant notre tâche longue.
        // Par défaut, un Handler doit être créé avec un Looper :
        // Nous prenons ici le Looper défini automatiquement par notre HandlerThread.
        Handler handler = new Handler(this.getLooper());
        // 2.4 - Puis nous affectons à cet Handler un nouvel exécutable via la méthode  post()
        handler.post(new Runnable(){
            @Override
            public void run() {
                // 2.5 - Cet exécutable lancera notre fameuse méthode de 7 secondes...
                Utils.executeLongActionDuring7seconds();
                // 2.6 - Une fois terminé, on mettra à jour notre interface graphique en stoppant notre ProgressBar.
                // Attention ici, vous ne POUVEZ PAS modifier un élément de votre UI en dehors du MainThread.
                // Comme ici vous êtes dans un thread différent, il faut que vous reveniez dans le MainThread,
                // en lançant un nouvel Handler utilisant le Looper de MainThread.
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (progressBarWeakReference.get() != null) progressBarWeakReference.get().setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
