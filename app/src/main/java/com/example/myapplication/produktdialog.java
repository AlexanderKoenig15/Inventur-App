package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import org.jetbrains.annotations.NotNull;

public class produktdialog extends AppCompatDialogFragment {
    Lebensmittel Produkt;
    Produktlistener listener;
    public produktdialog(Lebensmittel Result)
    {
        Produkt = Result;
    }
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.produkt_dialog, null);
        final EditText barcodeview =view.findViewById(R.id.Barcodefeld);
        final EditText nameview = view.findViewById(R.id.namefeld);
        final EditText anzahlview = view.findViewById(R.id.anzahlfeld);
        final EditText Inhalt = view.findViewById(R.id.inhaltfeld);
        final EditText Energie = view.findViewById(R.id.Energiefeld);
        final EditText  VPE = view.findViewById(R.id.vpefeld);
        final EditText  MhdMin = view.findViewById(R.id.mhdMinfeld);
        final EditText  MhdMax = view.findViewById(R.id.mhdMaxfeld);
        Energie.setText(Produkt.Ene);
        VPE.setText(Produkt.VPE);
        MhdMin.setText(Produkt.Min);
        MhdMax.setText(Produkt.Max);
        Inhalt.setText(Produkt.Inh);
        nameview.setText(Produkt.Nam);
        anzahlview.setText(String.valueOf(Produkt.Anz));
        barcodeview.setText(Produkt.Bar);
        builder.setView(view).setTitle("Produkt Interface").setPositiveButton("Bestätigen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Produkt.Bar = barcodeview.getText().toString();
                Produkt.Nam = nameview.getText().toString();
                Produkt.Anz = Integer.parseInt(anzahlview.getText().toString(), 10);
                Produkt.Ene = Energie.getText().toString();
                Produkt.Inh = Inhalt.getText().toString();
                Produkt.VPE = VPE.getText().toString();
                Produkt.Min = MhdMin.getText().toString();
                Produkt.Max = MhdMax.getText().toString();
             listener.lebensmittelinterface(Produkt);
            }
        }).setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
            super.onAttach(context);
        try {
            listener = (Produktlistener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface Produktlistener
    {
        void lebensmittelinterface(Lebensmittel Produkt);
    }
}
