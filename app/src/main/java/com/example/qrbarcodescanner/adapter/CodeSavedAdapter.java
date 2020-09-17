package com.example.qrbarcodescanner.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
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
import com.example.qrbarcodescanner.model.CodeSaved;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CodeSavedAdapter extends RecyclerView.Adapter<CodeSavedAdapter.ViewHolder> {
    public ArrayList<CodeSaved> ListCode;
    public Context context;
    public DBManager dbManager;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, type, content;
        ImageView icontype, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            icontype = itemView.findViewById(R.id.icontype);
            type = itemView.findViewById(R.id.type);
            content = itemView.findViewById(R.id.content1);
            delete = itemView.findViewById(R.id.icondelete);
        }
    }

    public CodeSavedAdapter(ArrayList<CodeSaved> listCode, Context Context, DBManager Manager) {
        this.ListCode = listCode;
        this.context = Context;
        this.dbManager = Manager;
    }

    @NonNull
    @Override
    public CodeSavedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.qrcode, parent, false);
        return new CodeSavedAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeSavedAdapter.ViewHolder holder, int position) {
        CodeSaved code = ListCode.get(position);
        holder.time.setText(code.getTimeGen());
        if (code.getContent().length() > 18) {
            holder.content.setText(code.getContent().substring(0, 15) + "...");
        } else {
            holder.content.setText(code.getContent());
        }
        holder.type.setText(code.getType());
        Bitmap bitmap = BitmapFactory.decodeByteArray(code.img, 0, code.img.length);
        switch (code.getType()) {
            case "QR code":

                break;
            case "Barcode":
            case "Barcode-39":
                holder.icontype.getLayoutParams().height = 30;

                break;
            case "PDF 417":

                break;
            case "Barcode-93":

                break;

        }

        holder.icontype.setImageBitmap(bitmap);
        holder.delete.setImageResource(R.drawable.ic_baseline_more_vert_24);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup;
                popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.qrcode1, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.share1) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            String s = null;
                            try {
                                s = new String(code.img, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Uri uri = Uri.parse(s);
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                            intent.setType("image/jpeg");
                            context.startActivity(Intent.createChooser(intent, "Send To"));
                        }
                        if (item.getItemId() == R.id.delete2) {
                            dbManager.deleteQrCodeSaved(code.getId());
                            ListCode.remove(position);
                            notifyDataSetChanged();
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return ListCode.size();
    }

}
