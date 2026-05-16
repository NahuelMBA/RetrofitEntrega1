package com.example.retrofit_entrega1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InicioFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        WebView webView = view.findViewById(R.id.webViewMapa);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String mapHtml = "<html><body style='margin:0;padding:0;'><iframe width='100%' height='100%' src='https://maps.google.com/maps?q=Universidad+de+la+Punta,San+Luis,Argentina&z=16&output=embed' frameborder='0' style='border:0'></iframe></body></html>";
        webView.loadData(mapHtml, "text/html", "utf-8");

        return view;
    }
}