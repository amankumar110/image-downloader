package in.amankumar110.imagedownloaderapp;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import in.amankumar110.imagedownloaderapp.databinding.ImageItemBinding;
import in.amankumar110.imagedownloaderapp.models.Image;


public class ImageAdapter extends PagingDataAdapter<Image, ImageAdapter.ImageViewHolder> {

    private RequestManager glide;
    private onImageClicked onImageClicked;

    public ImageAdapter(RequestManager requestManager, onImageClicked onImageClicked) {
        super(new ImagesComparator());
        this.onImageClicked = onImageClicked;
        this.glide = requestManager;
    }

    public interface onImageClicked {
        void onClick(Image image);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageItemBinding binding = ImageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Image image = getItem(position);
        bind(holder,image);
    }

    public void bind(ImageViewHolder holder, Image image) {


        if(image.getUrls()!=null) {
            glide.load(image.getUrls().getRegular()).placeholder(R.drawable.image_placeholder).into(holder.binding.image);


            holder.binding.image.setOnClickListener(v -> onImageClicked.onClick(image));
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageItemBinding binding;
        public ImageViewHolder(@NonNull ImageItemBinding imageItemBinding) {
            super(imageItemBinding.getRoot());
            this.binding = imageItemBinding;
        }
    }

    private static class ImagesComparator extends DiffUtil.ItemCallback<Image> {

        @Override
        public boolean areItemsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
            return oldItem.getUrls().getFull().equals(newItem.getUrls().getFull());
        }
    }
}
