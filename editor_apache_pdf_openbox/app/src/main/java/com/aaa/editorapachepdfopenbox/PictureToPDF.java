/*****************************************************************************
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Douglas González Parra 2021
 douglas.gonzalezparra@ucr.ac.cr
 *****************************************************************************/

package com.aaa.editorapachepdfopenbox;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSStream;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.PDPageTree;
import com.tom_roush.pdfbox.pdmodel.PDResources;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.pdmodel.graphics.PDXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.rendering.PDFRenderer;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/****************************************************************************
 *  Convierte imagenes a PDF
 *****************************************************************************/
public class PictureToPDF extends Fragment {
    // inicializa variables
    private Button btfileimgToPDF, btconvertirimgToPDF; // Botones para seleccionar imagenes y convertir a pdf
    private TextView txt_path_show_imgToPDF;         // Muestra el path del archivo seleccionado
    private String pathPDFtoPictures;       // Path del archivo PDF seleccionado

    // Localización donde almacena el archivo PDF creado a partir de imagenes
    private static final String OUTPUT_DIR = "/storage/emulated/0/Documents/";

    // Contrato previo de actividad a realizar al seleccionar el archivo PDF.
    ActivityResultLauncher<String> PictureToPDFfLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    //path1 = data.getData().getPath();
                    pathPDFtoPictures = "/storage/emulated/0/Pictures/img1.jpg";
                    txt_path_show_imgToPDF.setText(pathPDFtoPictures);
                    displatToast(pathPDFtoPictures);
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picturetopdf, container, false);
        btfileimgToPDF = view.findViewById(R.id.bt_file_imgToPDF);
        btconvertirimgToPDF = view.findViewById(R.id.bt_convertir_pdf_to_imgToPDF);
        txt_path_show_imgToPDF = view.findViewById(R.id.img_path_fileimgtoPDF);

        // selecciona archivo PDF
        btfileimgToPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureToPDFfLauncher.launch("img/*");
            }
        });

        // Invoca el la función que convierte imagenes a pdf
        btconvertirimgToPDF.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    PDFBoxResourceLoader.init(getActivity());
                    convertirImgToPDF(pathPDFtoPictures);
                    txt_path_show_imgToPDF.setText("PDF creado");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    // Convertir Imagenes a PDF
    public static void convertirImgToPDF(String pathPDFtoPictures)throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDImageXObject image = PDImageXObject.createFromFile(pathPDFtoPictures, doc);
        PDPageContentStream contents = new PDPageContentStream(doc,page);
        PDRectangle pageSize = PDRectangle.A4;
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        float pageWidth = pageSize.getWidth();
        float pageHeight = pageSize.getHeight();
        float ratio = Math.min(pageWidth/originalWidth, pageHeight/originalHeight);
        float scaleWidth = originalWidth * ratio;
        float scaleHeight = originalHeight * ratio;
        float x = (pageWidth - scaleWidth)/2;
        float y = (pageHeight - scaleHeight)/2;
        contents.drawImage(image,x,y,scaleWidth,scaleHeight);
        contents.close();
        doc.save(new File(OUTPUT_DIR + "imgTopdf.pdf"));
    }
}


