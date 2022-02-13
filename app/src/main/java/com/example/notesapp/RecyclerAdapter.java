package com.example.notesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NoteHolder> implements Filterable {

    // This is for the Recycler View to make it a staggered layout and for the items
    Context context;
    Activity activity;
    List<Model> noteList;
    List<Model> newNoteList;

    public RecyclerAdapter(Context context, Activity activity, List<Model> noteList) {
        this.context = context;
        this.activity = activity;
        this.noteList = noteList;
        newNoteList = new ArrayList<>(noteList);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.title.setText(noteList.get(position).getTitle());
        holder.description.setText(noteList.get(position).getDescription());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateNoteActivity.class); // I might need to fix these
                intent.putExtra("title", noteList.get(holder.getBindingAdapterPosition()).getTitle());
                intent.putExtra("description", noteList.get(holder.getBindingAdapterPosition()).getDescription());
                intent.putExtra("id", noteList.get(holder.getBindingAdapterPosition()).getId());

                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    @Override
    public Filter getFilter() {
        return expFilter;
    }

    private Filter expFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Model> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(newNoteList);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Model item : newNoteList) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            noteList.clear();
            noteList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class NoteHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        RelativeLayout layout;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.title_descript);
            layout = itemView.findViewById(R.id.note_layout);
        }

    }

    public List<Model> getNoteList() {
        return noteList;
    }

    public void removeNote(int position) {
        noteList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreNote(Model item, int position) {
        noteList.add(position, item);
        notifyItemInserted(position);
    }
}
