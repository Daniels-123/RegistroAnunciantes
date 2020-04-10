package com.digitalandroidyweb.registroanunciantes.Adaptor;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitalandroidyweb.registroanunciantes.R;
import java.util.ArrayList;

public class ExampleAdaptor extends RecyclerView.Adapter<ExampleAdaptor.ExampleViewHolder> {


    private Context mContext;
    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickItemListener(OnItemClickListener listener) {
        mListener = listener;

    }

    public ExampleAdaptor(Context context, ArrayList<ExampleItem> exampleList){
        mContext = context;
        mExampleList = exampleList;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);

        String id = currentItem.getId_Nombre();
        String nombre = currentItem.getNombre();
        String direccion = currentItem.getDireccion();
        String barrio = currentItem.getBarrio();
        String categoria = currentItem.getCategoria();
        String subcategoria = currentItem.getSubCategoria();

        holder.mId.setText(id);
        holder.mNombre.setText(nombre);
        holder.mDireccion.setText(direccion);
        holder.mBarrio.setText(barrio);
        holder.mCategoria.setText(categoria);
        holder.mSubCategoria.setText(subcategoria);


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


    public class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView mId, mNombre, mDireccion, mBarrio, mCategoria, mSubCategoria;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mId = itemView.findViewById(R.id.text_id);
            mNombre= itemView.findViewById(R.id.text_nombre);
            mDireccion= itemView.findViewById(R.id.text_direccion);
            mBarrio= itemView.findViewById(R.id.text_barrio);
            mCategoria= itemView.findViewById(R.id.text_categoria);
            mSubCategoria = itemView.findViewById(R.id.text_subcategoria);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
