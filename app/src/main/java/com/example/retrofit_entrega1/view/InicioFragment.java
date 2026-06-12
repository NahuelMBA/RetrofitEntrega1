package com.example.retrofit_entrega1.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.retrofit_entrega1.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);

        WebSettings webSettings = binding.webViewMapa.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.webViewMapa.setWebViewClient(new WebViewClient());

        String mapHtml = "<html><body style='margin:0;padding:0;'><iframe width='100%' height='100%' src='https://maps.google.com/maps?q=Universidad+de+la+Punta,San+Luis,Argentina&z=16&output=embed' frameborder='0' style='border:0'></iframe></body></html>";
        binding.webViewMapa.loadData(mapHtml, "text/html", "utf-8");

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
