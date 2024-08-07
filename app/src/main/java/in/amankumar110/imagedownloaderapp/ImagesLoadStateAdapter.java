package in.amankumar110.imagedownloaderapp;

import static in.amankumar110.imagedownloaderapp.utils.Constants.ERROR_MESSAGE;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.RecyclerView;

import in.amankumar110.imagedownloaderapp.databinding.LoadStateItemBinding;

public class ImagesLoadStateAdapter extends androidx.paging.LoadStateAdapter<ImagesLoadStateAdapter.LoadstateViewHolder> {

    private final View.OnClickListener retryCalback;

    public ImagesLoadStateAdapter(View.OnClickListener retryCallback) {
        this.retryCalback = retryCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull LoadstateViewHolder holder, @NonNull LoadState loadState) {

        if(loadState.getEndOfPaginationReached())
            holder.binding.getRoot().setVisibility(View.GONE);
         else if(loadState instanceof LoadState.Loading)
            bindLoadingState(holder.binding);
         else if(loadState instanceof LoadState.Error)
            bindErrorState(holder.binding,loadState);
    }

    private void bindLoadingState(LoadStateItemBinding binding) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnRetry.setVisibility(View.GONE);
        binding.tvError.setVisibility(View.GONE);
    }

    private void bindErrorState(LoadStateItemBinding binding, LoadState loadState) {
        binding.progressBar.setVisibility(View.GONE);
        binding.btnRetry.setVisibility(View.VISIBLE);
        binding.tvError.setVisibility(View.VISIBLE);

        LoadState.Error errorState = (LoadState.Error) loadState;
        binding.tvError.setText(ERROR_MESSAGE);
    }


    @NonNull
    @Override
    public LoadstateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull LoadState loadState) {
        LoadStateItemBinding binding = LoadStateItemBinding.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                viewGroup,
                false);

        binding.btnRetry.setOnClickListener(retryCalback);
        return new LoadstateViewHolder(binding);
    }

    public static class LoadstateViewHolder extends RecyclerView.ViewHolder {
        private final LoadStateItemBinding binding;
        public LoadstateViewHolder(@NonNull LoadStateItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
