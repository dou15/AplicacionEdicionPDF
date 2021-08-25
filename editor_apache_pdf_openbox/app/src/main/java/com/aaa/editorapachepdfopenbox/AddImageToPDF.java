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
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.PDPageTree;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

/****************************************************************************
 *  Agrega una imagen como página al pdf
 *****************************************************************************/
public class AddImageToPDF extends Fragment {

    private Button btfileaddimgtopage, btcargarimagen, btaddimgpage; // Botones seleccion pdf, imagenes, crear pdf con nueva pagina
    private TextView txt_path_show;         // Muestra el path del archivo seleccionado
    private String pathPDFAddImateToPage, pictureToPagesOnPdf;       // Path del archivo PDF

    // Localización donde guarda archivo con el nuevo PDF
    private static final String OUTPUT_DIR = "/storage/emulated/0/Documents/";

    // Posición donde agregar nuevas páginas
    private int begin_page;

    EditText addpagein;

    // Contrato previo de actividad a realizar al seleccionar el archivo PDF.
    ActivityResultLauncher<String> PDFAddImageToPages = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    //path1 = data.getData().getPath();
                    pathPDFAddImateToPage = "/storage/emulated/0/Documents/preciosMayoristas.pdf";
                    txt_path_show.setText(pathPDFAddImateToPage);
                    displatToast(pathPDFAddImateToPage);
                }
            });

    ActivityResultLauncher<String> AddImageToPages = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    //path1 = data.getData().getPath();
                    pictureToPagesOnPdf = "/storage/emulated/0/Pictures/img1.jpg";
                    txt_path_show.setText(pictureToPagesOnPdf);
                    displatToast(pictureToPagesOnPdf);
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addimagetopdf, container, false);
        btfileaddimgtopage = view.findViewById(R.id.bt_file_addImageToPDF);
        btcargarimagen = view.findViewById(R.id.bt_img_addImageToPDF);
        btaddimgpage = view.findViewById(R.id.bt_addImageToPDF);
        txt_path_show = view.findViewById(R.id.addImageToPDF_path_file);

        addpagein = view.findViewById(R.id.number_page_begin_addImageToPDF);

        // selecciona archivo PDF
        btfileaddimgtopage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PDFAddImageToPages.launch("application/pdf");
            }
        });
        btcargarimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddImageToPages.launch("img/*");
            }
        });

        btaddimgpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    begin_page = Integer.valueOf(addpagein.getText().toString());
                    AddImagePagePDF(begin_page, pathPDFAddImateToPage, pictureToPagesOnPdf);
                    txt_path_show.setText("Imagen agregada como página al PDF");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public static void AddImagePagePDF(int begin_page, String pathPDFAddImateToPage, String pictureToPagesOnPdf)throws IOException {

        // cargar archivo PDF
        File file = new File(pathPDFAddImateToPage);
        PDDocument docPDF = PDDocument.load(file);

        //Nueva página
        //PDDocument docNuevaPagina = new PDDocument();
        PDPage blankPage = new PDPage();
        //docNuevaPagina.addPage(blankPage);
        PDPageTree pages = docPDF.getDocumentCatalog().getPages();
        pages.insertAfter(blankPage, pages.get(begin_page-1));

        PDPage Newpage = docPDF.getPage(begin_page);
        PDImageXObject image = PDImageXObject.createFromFile(pictureToPagesOnPdf, docPDF);
        PDPageContentStream contents = new PDPageContentStream(docPDF, Newpage);
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

        // save New PDF
        docPDF.save(new File(OUTPUT_DIR + "addpagesPDF.pdf"));
    }

    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
