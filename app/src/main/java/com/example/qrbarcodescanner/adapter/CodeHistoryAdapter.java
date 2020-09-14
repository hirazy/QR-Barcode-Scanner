package com.example.qrbarcodescanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrbarcodescanner.R;
import com.example.qrbarcodescanner.db.DBManager;
import com.example.qrbarcodescanner.model.Code;

import java.util.ArrayList;

public class CodeHistoryAdapter extends RecyclerView.Adapter<CodeHistoryAdapter.ViewHolder> {
    public ArrayList<Code> ListCode;
    public Context context;
    public DBManager dbManager;
    public OnCode oncode;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, type, content;
        ImageView icontype, delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time1);
            icontype = itemView.findViewById(R.id.icontype1);
            type = itemView.findViewById(R.id.type1);
            content = itemView.findViewById(R.id.content2);
            delete = itemView.findViewById(R.id.icondelete1);
        }
    }

    public CodeHistoryAdapter(ArrayList<Code> listCode,OnCode onCode, Context Context, DBManager Manager) {
        this.ListCode = listCode;
        this.oncode =onCode;
        this.context = Context;
        this.dbManager = Manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.qrcode1, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Code code = ListCode.get(position);
        holder.time.setText(code.getTimeGen());
        if(code.getContent().length()>18) {
            holder.content.setText(code.getContent().substring(0,15)+"...");
        }
        else {
            holder.content.setText(code.getContent());
        }
        holder.type.setText(code.getType());

        //holder.icontype.set
        if(Checkphonenumber(code.getContent())){
            holder.icontype.setImageResource(R.mipmap.ic_phone);
            holder.type.setText("Phone");
        }
        else{
            if(code.getContent().length()>=7&&code.getContent().substring(0,7).equals("http://")){
                holder.icontype.setImageResource(R.mipmap.ic_url);
                holder.type.setText("Weblink");
            }
            else{
                holder.icontype.setImageResource(R.mipmap.ic_text);
                holder.type.setText("Text");
            }
        }
        //holder.icontype.
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup;
                popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.qrcode, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.delete2) {
                            dbManager.deleteQrCodeHistory(code.getId());
                            ListCode.remove(position);
                            notifyDataSetChanged();
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncode.OnCodeClick(position);
            }
        });
    }
    public interface OnCode{
        public void OnCodeClick(int position);
    }

    @Override
    public int getItemCount() {
        return ListCode.size();
    }



    public boolean Checkphonenumber(String s){
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)<'0'||s.charAt(i)>'9'){
                return false;
            }
        }
        return true;
    }


}
