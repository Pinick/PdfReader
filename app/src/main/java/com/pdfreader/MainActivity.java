package com.pdfreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

public class MainActivity extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener, OnErrorListener {

    private PDFView pdfview;
    private TextView text_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfview = (PDFView) findViewById(R.id.pdfView);
        text_show = (TextView) findViewById(R.id.text_show);
        if (pdfview != null)
            pdfview.postDelayed(new Runnable() {
                @Override
                public void run() {

                        if (pdfview != null)
                            pdfview.fromAsset("demo.pdf")
                                    .defaultPage(0)
                                    .enableSwipe(true)
                                    .enableAnnotationRendering(true)
                                    .onLoad(MainActivity.this)
                                    .onPageChange(MainActivity.this)
                                    .onError(MainActivity.this)
                                    .scrollHandle(new DefaultScrollHandle(MainActivity.this))
                                    .load();

                }
            }, 1000);
    }
    @Override
    public void onDestroy() {
        pdfview.recycle();
        pdfview = null;
        super.onDestroy();
        //  dialog.dismiss();
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        text_show.setText(page+"/"+pageCount);
    }

    @Override
    public void onError(Throwable t) {

    }
}
