package com.aaa.editorapachepdfopenbox;

import android.net.Uri;
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
import androidx.fragment.app.Fragment;

import com.tom_roush.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

/****************************************************************************
 *  Remueve un rango de páginas del pdf
 *****************************************************************************/
public class RemovePagePdf extends Fragment {

    // inicializa variables
    private Button btfilepageremove, btremovepage; // Botones para seleccionar archivo PDF y remover páginas
    private TextView txt_path_show;         // Muestra el path del archivo seleccionado
    private String pathPDFToRemovepage;       // Path del archivo PDF remover páginas

    // Localización donde guarda archivo con las páginas removidas
    private static final String OUTPUT_DIR = "/storage/emulated/0/Documents/";

    // Rango páginas a remover
    private int begin_page;
    private int end_page;

    EditText beginNumberInput;
    EditText endNumberInput;

    // Contrato previo de actividad a realizar al seleccionar el archivo PDF.
    ActivityResultLauncher<String> PDFRemovePages = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    //path1 = data.getData().getPath();
                    pathPDFToRemovepage = "/storage/emulated/0/Documents/preciosMayoristas.pdf";
                    txt_path_show.setText(pathPDFToRemovepage);
                    displatToast(pathPDFToRemovepage);
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.removepage, container, false);
        btfilepageremove = view.findViewById(R.id.bt_file_removepage);
        btremovepage = view.findViewById(R.id.bt_removepage);
        txt_path_show = view.findViewById(R.id.removepage_path_file);

        beginNumberInput = view.findViewById(R.id.number_page_begin_remove);
        endNumberInput = view.findViewById(R.id.number_page_end_remove);

        // selecciona archivo PDF
        btfilepageremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PDFRemovePages.launch("application/pdf");
            }
        });

        btremovepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    begin_page = Integer.valueOf(beginNumberInput.getText().toString());
                    end_page = Integer.valueOf(endNumberInput.getText().toString());
                    removePagesPDFDocument(begin_page, end_page, pathPDFToRemovepage);
                    txt_path_show.setText("Paginas removidas del PDF");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public static void removePagesPDFDocument(int begin_page, int end_page, String pathPDFToRemovepage)throws IOException {

        // cargar archivo PDF
        File file = new File(pathPDFToRemovepage);
        PDDocument docPDF = PDDocument.load(file);

        // Total de páginas archivo PDF
        int count = begin_page;
        int endCount = end_page - 1;

        // Remover páginas
        for(int i=begin_page; i<=end_page; i++){
            docPDF.removePage(begin_page-1);
        }

        docPDF.save(new File(OUTPUT_DIR + "PDFpagesRemoved.pdf"));
    }

    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}