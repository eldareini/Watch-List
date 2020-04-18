package example.com.eldareini.eldarmovieapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.objects.ReviewItem;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private Context context;
    private ArrayList<ReviewItem> reviewItems = new ArrayList<>();

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    public void addAll(ArrayList<ReviewItem> reviewItems){
        this.reviewItems.addAll(reviewItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.review_list_layout, viewGroup, false);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i) {

        reviewHolder.bind(reviewItems.get(i));

    }

    @Override
    public int getItemCount() {
        return reviewItems.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        private TextView reviewWriter, reviewComment;
        private ReviewItem currentReview;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);

            reviewWriter = itemView.findViewById(R.id.reviewWriter);
            reviewComment = itemView.findViewById(R.id.reviewComment);
            itemView.setOnClickListener(this);
            itemView.setOnTouchListener(this);
        }

        public void bind(ReviewItem reviewItem){

            currentReview = reviewItem;
            reviewWriter.setText(reviewItem.getAuthor() + ':');
            reviewComment.setText(reviewItem.getContent());

        }

        @Override
        public void onClick(View v) {

            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
                v.setSelected(false);
            }
            else {
                selectedItems.put(getAdapterPosition(), true);
                v.setSelected(true);
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.review_dialog_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView textAuthor = dialog.findViewById(R.id.textAuthorDialogReview);
                textAuthor.setText(currentReview.getAuthor());
                TextView textContent = dialog.findViewById(R.id.textContentDialogReview);
                textContent.setText(currentReview.getContent());
                FrameLayout frameLayout = dialog.findViewById(R.id.reviewDialogContainer);

                frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView btnExit = dialog.findViewById(R.id.btnExitReview);
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnExit.setOnTouchListener(this);


                dialog.show();


            }

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setAlpha(0.5f);
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    v.setAlpha(1f);
                    break;
                }
            }
            return false;
        }
    }
}
