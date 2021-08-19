package com.aaa.editorapachepdfopenbox;

// Importa paquetes necesarios
import android.content.Intent;
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

import com.tom_roush.pdfbox.io.MemoryUsageSetting;
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;

/****************************************************************************
 *  Une dos archivos PDF en uno solo
 *****************************************************************************/
public class MergePDF extends Fragment {
    // inicializa variables
    Button btFile1, btFile2,btmerge; // Botones seleccionar archivos PDF y realizar la unión de los archivos

    TextView txt_path_show; // mostrar path

    //String path1="/storage/emulated/0/Documents/edesur.pdf";
    //String path2="/storage/emulated/0/Documents/preciosMayoristas.pdf";
    String path1;
    String path2;

    // Contracto cargar archivo PDF uno
    ActivityResultLauncher<String> filePdf_1 = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    //path1 = data.getData().getPath();
                    path1 = "/storage/emulated/0/Documents/edesur.pdf";
                    txt_path_show.setText(path1);
                    displatToast(path1);
                }
            });

    // Contracto cargar archivo PDF dos
    ActivityResultLauncher<String> filePdf_2 = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    //path2 = data.getData().getPath();
                    path2 = "/storage/emulated/0/Documents/preciosMayoristas.pdf";
                    txt_path_show.setText(path2);
                    displatToast(path2);
                }
            });

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

        // Responde a precionar seleccionar archivo PDF uno
        btFile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePdf_1.launch("application/pdf");
            }
        });

        // Responde a precionar seleccionar archivo PDF uno
        btFile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePdf_2.launch("application/pdf");
            }
        });

        // Responde a precionar unir PDFs
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

    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    // Realiza la unión de dos archivos PDF
    public void pdfMerge(String path1, String path2) throws Exception{

        try {
            // Instantiate a new PDFMergerUtility.
            PDFMergerUtility pdfMerger = new PDFMergerUtility();
            //Set the name of the destination file.
            pdfMerger.setDestinationFileName("/storage/emulated/0/Documents/merged_biblio_tom.pdf");

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