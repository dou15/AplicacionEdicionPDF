package com.aaa.editorpdf_192307;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ImageWriter;
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
import android.widget.TextView;
import android.widget.Toast;

//import com.jaiselrahman.filepicker.activity.FilePickerActivity;
//import com.jaiselrahman.filepicker.config.Configurations;
//import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.cos.COSName;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.rendering.PDFRenderer;


public class Picture extends Fragment {
    // inicializa variables
    Button btfileimg, btextraerimg;
    TextView txt_path_show;
    Intent myFileIntent;
    String path1 = "";
    Bitmap bitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picture, container, false);
        btfileimg =  view.findViewById(R.id.bt_file_img);
        btextraerimg =  view.findViewById(R.id.bt_extraer_img);
        txt_path_show = view.findViewById(R.id.img_path_file);

        btfileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("application/pdf");
                startActivityForResult(myFileIntent, 101);
            }
        });

        btextraerimg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    extraerImagenes(path1);
                    txt_path_show.setText("Imagenes del PDF extraidas");
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
        }
    }

    private static final String OUTPUT_DIR = "/storage/emulated/0/";

    public static void extraerImagenes(String path1)throws IOException {

        //Loading an existing document
        /*File file = new File(path1);
        PDDocument doc = PDDocument.load(file);

        //Instantiating the PDFRenderer class
        PDFRenderer renderer = new PDFRenderer(doc);

        //Rendering an image from the PDF document
        //BufferedImage image = renderer.renderImage(2);

        //Writing the image to a file
      //  ImageIO.write(image, "JPEG", new File("/eclipse-workspace/my_image.jpeg"));

        System.out.println("Image created successfully.");

        //Closing the document
        doc.close(); */

        /******************************************************************/
        // ERROR : try (final PDDocument document = PDDocument.load(new File(path1))){
        // java.lang.ClassNotFoundException: Didn't find class "java.awt.Point" on path
        /******************************************************************/
        try (final PDDocument document = PDDocument.load(new File(path1))){
            // The page tree, which defines the ordering of pages in the document in an efficient manner.
            PDPageTree list = document.getPages();
                // A page in a PDF document.
                for (PDPage page : list) {
                    // A set of resources available at the page/pages/stream level.
                    PDResources pdResources = page.getResources();
                    int i = 1;
                    // A PDF Name object.
                    for (COSName name : pdResources.getXObjectNames()) {
                        // An external object, or "XObject".
                        PDXObject o = pdResources.getXObject(name);
                        if (o instanceof PDImageXObject) {
                            // An Image XObject.
                            PDImageXObject image = (PDImageXObject)o;
                            String filename = OUTPUT_DIR + "extracted-image-" + i + ".png";
                            //ImageIO.write(image.getImage(), "png", new File(filename));
                            // newInstance(Surface surface, int maxImages, int format)
                            //ImageWriter newInstance(image, int maxImages, "png");
                            COSStream img = image.getCOSObject();
                            FileOutputStream fos = null;
                            try {
                                FileOutputStream out = new FileOutputStream(filename);
                                // Creates Bitmap objects from various sources, including files, streams, and byte-arrays.
                                Bitmap bitmap= BitmapFactory.decodeFile(String.valueOf(image));
                                // Write a compressed version of the bitmap to the specified outputstream
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                // Flushes the output stream and forces any buffered output bytes to be written out
                                out.flush();
                                out.close();
                            } catch (java.io.IOException e) {
                                e.printStackTrace();
                            }
                            i++;
                        }
                    }
                }

            } catch (IOException e){

         System.err.println("Fail image extract" + e);
            }
    }

    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}

