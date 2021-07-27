package com.aaa.editorpdf_192307;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class Editor extends Fragment {
    Button boton_mergerpdf, boton_extraer_imagenes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editor, container, false);

        boton_mergerpdf = view.findViewById(R.id.bt_mergerpdf_editor);
        boton_mergerpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_mergepdf);
            }
        });

        boton_extraer_imagenes = view.findViewById(R.id.bt_extraer_img_editor);
        boton_extraer_imagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_extraer_imagenes);
            }
        });

        return view;
    }
}
