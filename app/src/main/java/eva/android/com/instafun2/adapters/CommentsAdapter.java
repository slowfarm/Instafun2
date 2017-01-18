package eva.android.com.instafun2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eva.android.com.instafun2.R;
import eva.android.com.instafun2.data.Comments;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

        private Comments mComments;

        public CommentsAdapter(Comments comments) {
            mComments = comments;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_comments_list_item, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.userNameText.setText(mComments.name.get(i));
            viewHolder.commentText.setText(mComments.text.get(i));
        }

        @Override
        public int getItemCount() {
            return mComments.name.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView userNameText;
            private final TextView commentText;

            ViewHolder(View v) {
                super(v);
                userNameText = (TextView)v.findViewById(R.id.username_text);
                commentText = (TextView) v.findViewById(R.id.comment_text);
            }
        }
    }
