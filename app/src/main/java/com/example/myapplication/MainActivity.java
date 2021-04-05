package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements produktdialog.Produktlistener {
    public List<Lebensmittel> Lebensmittelliste;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
starteevents();
       Lebensmittelliste = new ArrayList<>();
       lesen();
       refreshtable();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result.getContents() != null) {
            Lebensmittel Produkt = getLebensmittel(result.getContents());
            produktdialog dialog = new produktdialog(Produkt);
            dialog.show(getSupportFragmentManager(), "Produkt Interface");
        }
    }
    public Lebensmittel getLebensmittel(String Barcode)
    {
        int index = -1;
        for(int i = 0;i < Lebensmittelliste.size(); i++)
        {
            if(Lebensmittelliste.get(i).Bar.equals(Barcode))
                index = i;
        }
        Lebensmittel Produkt;
        if(index != -1)
        {
            Produkt = Lebensmittelliste.get(index);
        }
        else
            {
                Produkt = new Lebensmittel(Barcode,"unbekannt", "100", 1, "01/2020", "01/2021", "Glas", "100");
            }
        return Produkt;
    }
    public void addtolay(TableRow Zeile, String Content)
    {
        TextView Contentview = new TextView(this);
        Drawable Cellshape = ContextCompat.getDrawable(this, R.drawable.cell_shape);
        Contentview.setBackground(Cellshape);
        Contentview.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
        Contentview.setText(Content);
        Contentview.setGravity(Gravity.CENTER);
        Contentview.setAutoSizeTextTypeUniformWithConfiguration(1, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
        Contentview.setPadding(8,8,8,8);
        Contentview.setMaxLines(1);
        Zeile.addView(Contentview);
    }
    public void lesen() {
        int fehler = 0;
        try {
            File inputfile = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/lager.sort");
            FileInputStream input = new FileInputStream(inputfile);
            InputStreamReader stream = new InputStreamReader(input, "utf8");
            BufferedReader reader = new BufferedReader(stream);
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] Content = line.split(";");
                    Lebensmittel listitem = new Lebensmittel(Content[0], Content[1], Content[3], Integer.parseInt(Content[4]), Content[5], Content[6], Content[2], Content[7]);
                    Lebensmittelliste.add(listitem);
                } catch (Exception e) {
                    fehler++;
                }
            }
            if(fehler > 0){Toast.makeText(getBaseContext(), "Es wurden ("+Lebensmittelliste.size()+")Produkte geladen"+"\nEs konnten ("+fehler+")Produkte nicht geladen werden.", 3).show();}else{Toast.makeText(getBaseContext(), "Es wurden "+Lebensmittelliste.size()+" Produkte geladen", 3).show();}
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), 3).show();
        }
    }
    public void Speichern()
    {
        Toast.makeText(getBaseContext(), "...speichert", 2).show();
        try {
            File Speichern = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/lager.sort");
            FileOutputStream stream = new FileOutputStream(Speichern);
            OutputStreamWriter writer = new OutputStreamWriter(stream, "utf8");
            for (Lebensmittel item:Lebensmittelliste) {
                String Content = item.Bar+";"+item.Nam+";"+item.VPE+";"+item.Inh+";"+item.Anz+";"+item.Min+";"+item.Max+";"+item.Ene+";\n";
                writer.write(Content);
            }
            writer.close();
            Toast.makeText(getBaseContext(), "Speichern erfolgreich!", 2).show();
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Speichern fehlgeschlagen!", 2).show();
        }
    }
public void refreshtable()
{
    TabLayout Tabs = findViewById(R.id.tabLayout);
    SearchView suche = findViewById(R.id.Suchen);
    TableLayout layout = findViewById(R.id.Tablay);
    layout.removeAllViews();
    TableRow Row = new TableRow(this);
    addtolay(Row,"Barcode");addtolay(Row, "Name");addtolay(Row,"Anzahl");
    if(Tabs.getSelectedTabPosition() == 1){addtolay(Row,"Inhalt");addtolay(Row,"Energie");addtolay(Row,"VPE");addtolay(Row, "MhdMin");addtolay(Row,"MhdMax");}
    layout.addView(Row);
    for(Lebensmittel Lebensmitt:Lebensmittelliste) {

        if (Lebensmitt.Nam.toLowerCase().contains(suche.getQuery().toString().toLowerCase()) && Filtern(Lebensmitt)) {
                TableRow zeile = new TableRow(this);
                addtolay(zeile, Lebensmitt.Bar);
                addtolay(zeile, Lebensmitt.Nam);
                addtolay(zeile, String.valueOf(Lebensmitt.Anz));
                if (Tabs.getSelectedTabPosition() == 1) {
                    addtolay(zeile, Lebensmitt.Inh + "g");
                    addtolay(zeile, Lebensmitt.Ene + "kcal/100g");
                    addtolay(zeile, Lebensmitt.VPE);
                    addtolay(zeile, Lebensmitt.Min);
                    addtolay(zeile, Lebensmitt.Max);
                }
                layout.addView(zeile);
        }
    }
}
public boolean Filtern(Lebensmittel item){
        CheckBox Filteran = findViewById(R.id.filtern);
        SeekBar Anzahl = findViewById(R.id.Anzahlrat);
        if(Filteran.isChecked())
        {
            if(Anzahl.getProgress() == item.Anz)
            return true;
        }
        else{return true;}
            return false;
}
    @Override
    public void lebensmittelinterface(Lebensmittel Produkt) {
    int index = -1;
    for(Lebensmittel leb: Lebensmittelliste)
    {
        if(leb.Bar.equals(Produkt.Bar)){index = Lebensmittelliste.indexOf(leb);}
    }
    if(index == -1){
        Lebensmittelliste.add(Produkt);
    }
else {
    Lebensmittelliste.set(index, Produkt);
    } refreshtable();   }
    void starteevents() {
        FloatingActionButton Scan = findViewById(R.id.Scan);
        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator inter = new IntentIntegrator(MainActivity.this);
                inter.setPrompt("Bitte Scanen sie den Produkt Barcode.");
                inter.setBeepEnabled(false);
                inter.setOrientationLocked(true);
                inter.setCaptureActivity(Capture.class);
                inter.initiateScan();
            }
        });
        FloatingActionButton Speichern = findViewById(R.id.Speichern);
        Speichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Speichern();
            }
        });
        FloatingActionButton Reload = findViewById(R.id.Neuladen);
        Reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshtable();
            }
        });
        FloatingActionButton Import = findViewById(R.id.Filter);
        Import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        FloatingActionButton Export = findViewById(R.id.Info);
        Export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final SearchView Suche = findViewById(R.id.Suchen);
        Suche.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                refreshtable();
                return false;
            }
        });
        FloatingActionButton filter = findViewById(R.id.Filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout nav = findViewById(R.id.drawerlayout);
                if (nav.isDrawerOpen(GravityCompat.START)) {
                    nav.closeDrawer(GravityCompat.START);
                } else {
                    nav.openDrawer(GravityCompat.START);
                }
            }
        });
        FloatingActionButton info = findViewById(R.id.Info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageInfo information = getBaseContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                    Toast.makeText(getBaseContext(), "v." + information.versionName, 10).show();
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
        });
        TabLayout tab = findViewById(R.id.tabLayout);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                refreshtable();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        CheckBox checkboxfilter = findViewById(R.id.filtern);
        checkboxfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshtable();
            }
        });
        SeekBar anzahl = findViewById(R.id.Anzahlrat);
        anzahl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView anzeige = findViewById(R.id.Anzahlanzeige);
                anzeige.setText(String.valueOf(progress));
                refreshtable();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    }