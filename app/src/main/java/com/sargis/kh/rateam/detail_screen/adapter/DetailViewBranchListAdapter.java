package com.sargis.kh.rateam.detail_screen.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sargis.kh.rateam.R;
import com.sargis.kh.rateam.models.response_branches.Branch;

import java.util.ArrayList;
import java.util.List;

public class DetailViewBranchListAdapter extends RecyclerView.Adapter<DetailViewBranchListAdapter.DataAdapterViewHolder> {

    public interface BranchSelectedInterface {
        void onBranchListItemClicked(Branch branch);
    }

    private BranchSelectedInterface branchSelectedInterface;

    private List<Branch> branchList;

    public DetailViewBranchListAdapter(BranchSelectedInterface branchSelectedInterface) {
        this.branchSelectedInterface = branchSelectedInterface;
        branchList = new ArrayList<>();
    }

    @NonNull
    @Override
    public DataAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_view_branch_detail_view, parent, false);
        return new DataAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapterViewHolder holder, int position) {
        holder.bindData(branchList.get(position));
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public void updateData(List<Branch> branchList) {
        this.branchList = branchList;
        notifyDataSetChanged();
    }

    public class DataAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewBranchName;

        public DataAdapterViewHolder(View itemView) {
            super(itemView);
            textViewBranchName = itemView.findViewById(R.id.textViewBranchName);
        }

        public void bindData(final Branch branch) {
            String title = (branch.title.am != null && !branch.title.am.isEmpty()) ? branch.title.am : branch.title.ru;
            title = (title != null && !title.isEmpty()) ? title: branch.title.en;
            textViewBranchName.setText(title);

            itemView.setOnClickListener(v -> branchSelectedInterface.onBranchListItemClicked(branch));
        }
    }
}