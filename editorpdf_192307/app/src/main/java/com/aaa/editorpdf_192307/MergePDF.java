package com.aaa.editorpdf_192307;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MergePDF extends Fragment {
    // inicializa variables
    Button btFile1, btFile2,btmerge;


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
        btFile1 = (Button) view.findViewById(R.id.bt_file1);
        btFile2 = (Button) view.findViewById(R.id.bt_file2);
        btmerge = (Button) view.findViewById(R.id.bt_merge);

        btFile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FilePickerActivity.class);
                //Intent intent = new Intent(this, FilePickerActivity.class);
                //startActivity(intent);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowFiles(true)
                        .setShowImages(false)
                        .setShowVideos(false)
                        .setMaxSelection(1)
                        .setSuffixes("pdf")
                        .setSkipZeroSizeFiles(true)
                        .build());

                startActivityForResult(intent, 102);
            }
        });

        btFile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FilePickerActivity.class);
                startActivity(intent);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowFiles(true)
                        .setShowImages(false)
                        .setShowVideos(false)
                        .setMaxSelection(1)
                        .setSuffixes("pdf")
                        .setSkipZeroSizeFiles(true)
                        .build());

                startActivityForResult(intent, 103);
            }
        });

        btmerge.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getContext(), FilePickerActivity.class);
                startActivity(intent);
                startActivityForResult(intent, 104);*/
                try {
                    pdfMerge(path1,path2);
                } catch (IOException e) {
                    Log.e("Fail program", "Exception thrown while pdf merger", e);
                }
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && data != null) {
            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            String path = mediaFiles.get(0).getPath();

            switch (requestCode) {

                case 102:
                    displatToast("File path: " + path);
                    path1 = path;
                    Toast.makeText(getActivity(),"Direccion archivo " + path1,Toast.LENGTH_SHORT).show();
                    break;

                case 103:
                    displatToast("File path: " + path);
                    path2 = path;
                    Toast.makeText(getActivity(),"Direccion archivo " + path2,Toast.LENGTH_SHORT).show();
                    /*try {
                        pdfMerge(path1,path2);
                    } catch (IOException e) {
                        Log.e("Fail program", "Exception thrown while PDF Merger", e);
                    }*/
                    break;

               /* case 104:
                    try {
                        pdfMerge(path1,path2);
                        displatToast("PDFs unidos");
                    } catch (IOException e) {
                        Log.e("Fail program", "Exception thrown while pdf merger", e);
                    }*/
            }
        }
    }

    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    // Une dos archivos pdf en uno solo
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void pdfMerge(String path1, String path2) throws IOException {
        //carga en un List<InputStream> archivos pdf a unir
        InputStream in1 = Files.newInputStream(Paths.get(path1));
        InputStream in2 = Files.newInputStream(Paths.get(path2));
        List<InputStream> archivos = new ArrayList<InputStream>();
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

    }
}
