package pl.patrykheciak.projektowe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.patrykheciak.projektowe.model.MyMovie;

public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.ViewHolder> {

    private final List<MyMovie> movies;
    private final LayoutInflater inflater;
    private OnItemClickListener callback;
    private int sortType = 0;


    public MyRVAdapter(Context context) {
        movies = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(movies.get(position).getTitle());
    }

    public void setMovies(List<MyMovie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        sort();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
        sort();
        notifyDataSetChanged();
    }

    private void sort() {
        Collections.sort(movies, new Comparator<MyMovie>() {
            @Override
            public int compare(MyMovie o1, MyMovie o2) {
                if (sortType == 0) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
                if (sortType == 1) {
                    return -o1.getTitle().compareTo(o2.getTitle());
                }

                if (sortType == 2) {
                    return o1.getRecordedAt().compareTo(o2.getRecordedAt());
                }
                if (sortType == 3) {
                    return -o1.getRecordedAt().compareTo(o2.getRecordedAt());
                }

                if (sortType == 4) {
                    return o1.getDuration() - o2.getDuration();
                }
                if (sortType == 5) {
                    return -(o1.getDuration() - o2.getDuration());
                }

                return 0;
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener callback) {
        this.callback = callback;
    }

    public void removeByPosition(int adapterPosition) {
        movies.remove(adapterPosition);
        notifyDataSetChanged();
        sort();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.movie_list_item_title);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onItemClick(view, getAdapterPosition(), movies.get(getAdapterPosition()));
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (callback != null){
                        callback.onItemLongClick(view, getAdapterPosition(), movies.get(getAdapterPosition()));
                        return true;
                    } else
                    return false;
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int adapterPosition, MyMovie movie);
        void onItemLongClick(View view, int adapterPosition, MyMovie movie);
    }
}
