package com.aaa.filepicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    Button btFile1, btFile2;
    String path1 = "";
    String path2 = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mergepdf);

        // asinando variables
        btFile1 = findViewById(R.id.bt_file1);
        btFile2 = findViewById(R.id.bt_file2);
        //btMerge = findViewById(R.id.bt_merge);

        // Boton cargar pdf 1
        btFile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mergepdf.this, FilePickerActivity.class);
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

        // boton cargar pdf 2
        btFile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mergepdf, container, false);

        return view;
    }

    // acciones presionar botones cargar pdf 1, pdf 2
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            String path = mediaFiles.get(0).getPath();

            switch (requestCode) {

                case 102:
                    displatToast("File path: " + path);
                    path1 = path;
                    Toast.makeText(getApplicationContext(),"Direccion archivo " + path1,Toast.LENGTH_SHORT).show();
                    break;

                case 103:
                    displatToast("File path: " + path);
                    path2 = path;
                    Toast.makeText(getApplicationContext(),"Direccion archivo " + path2,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**********************************************************************************************/
    // imprime texto en pantalla
    private void displatToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    /**********************************************************************************************/
    // Une dos archivos pdf en uno solo
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void pdfMerge(View v) throws IOException {
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
        Toast.makeText(getApplicationContext(),"Archivos PDF unidos",Toast.LENGTH_SHORT).show();

    }
    /**********************************************************************************************/

}
