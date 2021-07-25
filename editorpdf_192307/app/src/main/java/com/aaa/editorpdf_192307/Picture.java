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

import org.apache.pdfbox.rendering.PDFRenderer;


public class Picture extends Fragment {
    // inicializa variables
    Button btfileimg, btextraerimg;


    String path1 = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picture, container, false);
        btfileimg = (Button) view.findViewById(R.id.bt_file_img);
        btextraerimg= (Button) view.findViewById(R.id.bt_extraer_img);

        btfileimg.setOnClickListener(new View.OnClickListener() {
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

        btextraerimg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getContext(), FilePickerActivity.class);
                startActivity(intent);
                startActivityForResult(intent, 104);*/
                try {
                    extraerImagenes(path1);
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

    public static void extraerImagenes(String path1)throws IOException {

        //Loading an existing document
        File file = new File("/eclipse-workspace/blank.pdf");
        PDDocument doc = PDDocument.load(file);

        //Instantiating the PDFRenderer class
        PDFRenderer renderer = new PDFRenderer(doc);

        //Rendering an image from the PDF document
       // BufferedImage image = renderer.renderImage(2);

        //Writing the image to a file
      //  ImageIO.write(image, "JPEG", new File("/eclipse-workspace/my_image.jpeg"));

        System.out.println("Image created successfully.");

        //Closing the document
        doc.close();
    }
}

