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

// Importa paquetes necesarios
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSStream;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageTree;
import com.tom_roush.pdfbox.pdmodel.PDResources;
import com.tom_roush.pdfbox.pdmodel.graphics.PDXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.rendering.PDFRenderer;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/****************************************************************************
*  Convierte un archivo PDF a imagenes en formato PNG.
*****************************************************************************/
public class Picture extends Fragment {
    // inicializa variables
    private Button btfileimg, btconvertirpdftoimg; // Botones para seleccionar archivo PDF y convertir a imagenes
    private TextView txt_path_show;         // Muestra el path del archivo seleccionado
    private String pathPDFtoPictures;       // Path del archivo PDF seleccionado
    private String name = "preciosMayoristas";

    int RESULT_OK = -1;

    // Locacion donde almacena las imagenes al convertir el archivo PDF a imagenes
    private static final String OUTPUT_DIR = "/storage/emulated/0/Documents";

    // Contrato previo de actividad a realizar al seleccionar el archivo PDF.
    /*ActivityResultLauncher<String> PDFtoPicturesfLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    //path1 = data.getData().getPath();
                    pathPDFtoPictures = "/storage/emulated/0/Documents/preciosMayoristas.pdf";
                    txt_path_show.setText(pathPDFtoPictures);
                    displatToast(pathPDFtoPictures);
                }
            });*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picture, container, false);
        btfileimg =  view.findViewById(R.id.bt_file_img);
        btconvertirpdftoimg =  view.findViewById(R.id.bt_convertir_pdf_to_img);
        txt_path_show = view.findViewById(R.id.img_path_file);

        // selecciona archivo PDF
        btfileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PDFtoPicturesfLauncher.launch("application/pdf");
                filePicker1();
            }
        });

        // Invoca el método que convierte el archivo PDF a imagenes
        btconvertirpdftoimg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    PDFBoxResourceLoader.init(getActivity()); //initialize the library's resource loader
                    convertirPDFImagenes(pathPDFtoPictures);
                    txt_path_show.setText("PDF convertido a imagenes");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    //filePicker1() filePicker2(): El contenido de la función no se coloca en btFile1.setOnClickListener
    //devido a que MaterialFilePicker() se utiliza en un fragment, para ser utilizado
    //dentro de setOnClickListener se debe especificar Activity
    private void filePicker1() {
        new MaterialFilePicker()
                //.withActivity(getActivity())
                .withSupportFragment(this)
                .withHiddenFiles(true)
                .withRequestCode(1000)
                .start();
    }

    //Responde según las solicitudes en onClick
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            //path PDF 1
            pathPDFtoPictures = filePath;
            txt_path_show.setText(pathPDFtoPictures);
            displatToast("path: " + pathPDFtoPictures);
        }
    }

    // Convierte el archivo PDF a imagenes
    public static void convertirPDFImagenes(String pathPDFtoPictures)throws IOException {

        // Cargando documento PDF
        File filePDF = new File(pathPDFtoPictures);
        PDDocument docPDF = PDDocument.load(filePDF);

        // Invocando PDFRenderer
        PDFRenderer renderer = new PDFRenderer(docPDF);

        for (int i = 0; i < docPDF.getNumberOfPages(); i++)
        {
            Bitmap image = renderer.renderImage(i);

            File renderFile = new File(OUTPUT_DIR, filePDF.getName() + "-" + (i + 1) + ".png");
            FileOutputStream fileOut = new FileOutputStream(renderFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
            fileOut.close();
        }
    }

    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

}