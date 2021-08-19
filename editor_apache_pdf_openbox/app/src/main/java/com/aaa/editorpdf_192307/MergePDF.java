package com.aaa.editorpdf_192307;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
//import android.content.res.Configuration;
import android.os.Build;
//import android.provider.MediaStore;
//import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.jaiselrahman.filepicker.activity.FilePickerActivity;
//import com.jaiselrahman.filepicker.config.Configurations;
//import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MergePDF extends Fragment {
    // inicializa variables
    Button btFile1, btFile2,btmerge;

    TextView txt_path_show;
    Intent myFileIntent;


    String path1 = "";
    String path2 = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mergepdf, container, false);
        btFile1 =  view.findViewById(R.id.bt_file1);
        btFile2 =  view.findViewById(R.id.bt_file2);
        btmerge =  view.findViewById(R.id.bt_merge);
        txt_path_show = view.findViewById(R.id.txt_path_merger);

        btFile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("application/pdf");
                startActivityForResult(myFileIntent, 101);
            }
        });

        btFile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("application/pdf");
                startActivityForResult(myFileIntent, 102);
            }
        });

        btmerge.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    pdfMerge(path1, path2);
                    txt_path_show.setText("PDFs unidos");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                    String path1 = data.getData().getPath();
                    txt_path_show.setText(path1);
                    displatToast(path1);
                    break;

            case 102:
                    String path2 = data.getData().getPath();
                    txt_path_show.setText(path2);
                    displatToast(path2);
                    break;
        }
    }

    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    // Une dos archivos pdf en uno solo
    /****************************************************************/
    // Example:
    // https://svn.apache.org/viewvc/pdfbox/trunk/examples/src/main/java/org/apache/pdfbox/examples/util/PDFMergerExample.java?revision=1871046&view=markup
    /****************************************************************/

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    public void pdfMerge(String path1, String path2) throws IOException {
        //carga en un List<InputStream> archivos pdf a unir
        Path p1 = Paths.get(path1);
        Path p2 = Paths.get(path1);
        InputStream in1 = Files.newInputStream(p1);
        InputStream in2 = Files.newInputStream(p2);
        List<InputStream> archivos = new ArrayList<>();
        archivos.add(in1);
        archivos.add(in2);
        // instancia PDFMergerUtility, empleado para unir varios archivos pdf en uno solo
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        // agrega los archivos a unir
        pdfMerger.addSources(archivos);
        // ByteArrayOutputStream path directorio a almacenar pdf final
        String pathSave = "/storage/emulated/0/merged.pdf";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] array = pathSave.getBytes();
        out.write(array);
        // salva el archivo unido
        pdfMerger.setDestinationStream(out);
        out.close();
        // mensaje final
        Toast.makeText(getActivity(),"Archivos PDF unidos",Toast.LENGTH_SHORT).show();
    }*/

    // Merge two pdf files
    // https://pdfbox.apache.org/docs/2.0.13/javadocs/
    // org.apache.pdfbox.multipdf
    // PDFMergerUtility

    /****************************************************************/
    // **** Error
    // at com.aaa.editorpdf_192307.MergePDF.pdfMerge(MergePDF.java:171)
    // Caused by: java.lang.ClassNotFoundException: Didn't find class
    // "java.awt.Point" on path: DexPathList[[dex file
    // "/data/data/com.aaa.editorpdf_192307/code_cache/.overlay/base.apk/classes3.dex",
    // zip file "/data/app/~~Lvblub44mRNQUOBPSavIrw==/com.aaa.editorpdf_192307-E1Dr4u8JmVqJsrY67u9quA==/base.apk"],
    // nativeLibraryDirectories=[/data/app/~~Lvblub44mRNQUOBPSavIrw==/com.aaa.editorpdf_192307-E1Dr4u8JmVqJsrY67u9quA==/lib/x86,
    // /system/lib, /system_ext/lib]]
    // at com.aaa.editorpdf_192307.MergePDF.pdfMerge(MergePDF.java:187)
    //      pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
    /****************************************************************/
    public void pdfMerge(String path1, String path2) throws Exception{

        try {
            // Instantiate a new PDFMergerUtility.
            PDFMergerUtility pdfMerger = new PDFMergerUtility();
            //Set the name of the destination file.
            pdfMerger.setDestinationFileName("/storage/emulated/0/merged.pdf");

            // Add a source file to the list of files to merge.
            pdfMerger.addSource(new File(path1));
            pdfMerger.addSource(new File(path2));

            //Merge the list of source documents, saving the result in the destination file.
            pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

        } catch (IOException e){
            // Error message
            System.err.println("Fail merger pdf" + e);
        }
    }
}
