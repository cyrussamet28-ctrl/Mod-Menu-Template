package com.van.modmenu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ekran tasarımı: Şık bir başlat butonu oluşturuyoruz
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(0xFF09090B);

        Button btnBaslat = new Button(this);
        btnBaslat.setText("OYUNU BAŞLAT & MENÜYÜ AÇ");
        btnBaslat.setTextColor(0xFFFFFFFF);
        btnOfisRenk: btnBaslat.setBackgroundColor(0xFFFF3B30);
        btnBaslat.setPadding(40, 40, 40, 40);

        btnBaslat.setOnClickListener(v -> oyunuVeMenuyuBaslat());

        layout.addView(btnBaslat);
        setContentView(layout);
    }

    private void oyunuVeMenuyuBaslat() {
        // 1. "Diğer uygulamalar üzerinde göster" izni kontrolü
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(permissionIntent);
            Toast.makeText(this, "Önce 'Diğer uygulamalar üzerinde göster' iznini ver kanka!", Toast.LENGTH_LONG).show();
            return;
        }

        // 2. Standoff 2'yi fırlat
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.axlebolt.standoff2");
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // 3. Menü Servisini Başlat
            try {
                Intent serviceIntent = new Intent(this, Class.forName("com.van.modmenu.Menu"));
                startService(serviceIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Menü tetiklendi!", Toast.LENGTH_SHORT).show();
            }

            finish(); // Loader ekranını kapatıp oyuna geçiş yapar
        } else {
            Toast.makeText(this, "Cihazda Standoff 2 kurulu değil!", Toast.LENGTH_LONG).show();
        }
    }
}
