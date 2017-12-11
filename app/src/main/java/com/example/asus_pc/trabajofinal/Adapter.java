package com.example.asus_pc.trabajofinal;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus_pc on 10/11/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.itemPregunta> implements View.OnClickListener {
    private List<Preguntas> dataset;
    private View.OnClickListener listener;

    public Adapter(List<Preguntas> dataset) {
        super();
        this.dataset = dataset;
    }

    @Override
    public itemPregunta onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        view.setOnClickListener(this);
        return new itemPregunta(view);
    }

    @Override
    public void onBindViewHolder(itemPregunta holder, int position) {
        Preguntas preguntas = dataset.get(position);
        holder.cardnombre.setText(preguntas.getNombre());
        holder.cardpregunta.setText(preguntas.getPregunta());
        holder.cardcategoria.setText(preguntas.getCategoria());
        holder.cardfecha.setText(preguntas.getFecha());
        holder.cardrespuesta.setText(preguntas.getRespuesta());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setDataset(List<Preguntas> dataset) {
        this.dataset = dataset;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);

    }


    public class itemPregunta extends RecyclerView.ViewHolder {
        @BindView(R.id.cardnombre)
        TextView cardnombre;
        @BindView(R.id.cardpregunta)
        TextView cardpregunta;
        @BindView(R.id.cardcategoria)
        TextView cardcategoria;
        @BindView(R.id.cardrespuesta)
        TextView cardrespuesta;
        @BindView(R.id.cardfecha)
        TextView cardfecha;

        public itemPregunta(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }


}
