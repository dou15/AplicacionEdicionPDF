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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.tom_roush.pdfbox.multipdf.Splitter;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SplitPDF extends Fragment {

    // inicializa variables
    private Button btfilesplit, btsplitdocument; // Botones para seleccionar archivo PDF y convertir a imagenes
    private TextView txt_path_show;         // Muestra el path del archivo seleccionado
    private String pathPDFtoSplit;       // Path del archivo PDF seleccionado

    // Locacion donde almacena las imagenes al convertir el archivo PDF a imagenes
    private static final String OUTPUT_DIR = "/storage/emulated/0/Documents/";

    // Pagina inicio final
    private int begin_page;
    private int end_page;
    int RESULT_OK=-1;

    EditText editText;
    EditText beginNumberInput;
    EditText endNumberInput;

    // Contrato previo de actividad a realizar al seleccionar el archivo PDF.
    /*ActivityResultLauncher<String> PDFtoPicturesfLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    //path1 = data.getData().getPath();
                    pathPDFtoSplit = "/storage/emulated/0/Documents/preciosMayoristas.pdf";
                    txt_path_show.setText(pathPDFtoSplit);
                    displatToast(pathPDFtoSplit);
                }
            });*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splitpdf, container, false);
        btfilesplit =  view.findViewById(R.id.bt_file_split);
        btsplitdocument =  view.findViewById(R.id.bt_split);
        txt_path_show = view.findViewById(R.id.split_path_file);

        beginNumberInput = view.findViewById(R.id.number_page_begin_split);
        endNumberInput = view.findViewById(R.id.number_page_end_split);

        // selecciona archivo PDF
        btfilesplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PDFtoPicturesfLauncher.launch("application/pdf");
                filePicker1();
            }
        });

        btsplitdocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    begin_page = Integer.valueOf(beginNumberInput.getText().toString());
                    end_page = Integer.valueOf(endNumberInput.getText().toString());
                    splitPDF(begin_page, end_page, pathPDFtoSplit);
                    txt_path_show.setText("PDF split");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    //filePicker1(): El contenido de la función no se coloca en btFile1.setOnClickListener
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
                pathPDFtoSplit = filePath;
                txt_path_show.setText(pathPDFtoSplit);
                displatToast("path: " + pathPDFtoSplit);
            }
        }

    public static void splitPDF(int begin_page, int end_page, String pathPDFtoSplit)throws IOException {

        int totalPageSplit = (end_page - begin_page) + 3;

        // carga el archivo
        File pdfFile = new File(pathPDFtoSplit);
        PDDocument pdfDocument = PDDocument.load(pdfFile);

        // crea un nuevo documento para split
        Splitter splitter = new Splitter();

        // pagina de inicio, fin, total de páginas a split
        splitter.setStartPage(begin_page);
        splitter.setEndPage(end_page);
        splitter.setSplitAtPage(totalPageSplit);

        // split paginas del pdf
        List<PDDocument> splitList =splitter.split(pdfDocument);

        // guarda paginas extraidas
        PDDocument pdfDocSlit = splitList.get(0);
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss"); // add S if you need milliseconds
        String filename = "splitFile" + df.format(new Date()) + "." + "pdf";
        File fileSplit = new File(OUTPUT_DIR + filename);
        pdfDocSlit.save(fileSplit);
    }

    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
