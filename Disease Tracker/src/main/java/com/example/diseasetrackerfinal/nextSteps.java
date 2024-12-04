package com.example.diseasetrackerfinal;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.graphics.pdf.PdfRenderer;
import android.graphics.Point;
import android.widget.ImageView;
import android.os.ParcelFileDescriptor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.diseasetrackerfinal.R;
import com.github.barteksc.pdfviewer.PDFView;


import java.io.File;
import java.io.IOException;

public class nextSteps extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_steps);

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("closeContact.pdf").load();

    }

    public void onBackPressed(){
        finish();
    }


}
