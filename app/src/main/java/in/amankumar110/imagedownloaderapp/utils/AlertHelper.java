package in.amankumar110.imagedownloaderapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import in.amankumar110.imagedownloaderapp.R;
import in.amankumar110.imagedownloaderapp.databinding.AlertLayoutBinding;

public class AlertHelper {
    private static String title = "Alert";
    private static String message = "Something went wrong!";
    private static String positiveButtonText = "OK";
    private static String negativeButtonText = "Cancel";
    private static boolean isCancelable = true;

    // Callbacks for button actions
    private static DialogInterface.OnClickListener positiveButtonCallback;
    private static DialogInterface.OnClickListener negativeButtonCallback;

    // Private constructor to prevent instantiation
    private AlertHelper() {
    }

    // Static method to set the title
    public static void setTitle(String title) {
        AlertHelper.title = title;
    }

    // Static method to set the message
    public static void setMessage(String message) {
        AlertHelper.message = message;
    }

    // Static method to set the positive button text
    public static void setPositiveButtonText(String positiveButtonText) {
        AlertHelper.positiveButtonText = positiveButtonText;
    }

    // Static method to set the negative button text
    public static void setNegativeButtonText(String negativeButtonText) {
        AlertHelper.negativeButtonText = negativeButtonText;
    }

    // Static method to set the cancelable flag
    public static void setCancelable(boolean cancelable) {
        AlertHelper.isCancelable = cancelable;
    }

    // Static method to set the positive button callback
    public static void setPositiveButtonCallback(DialogInterface.OnClickListener callback) {
        AlertHelper.positiveButtonCallback = callback;
    }

    // Static method to set the negative button callback
    public static void setNegativeButtonCallback(DialogInterface.OnClickListener callback) {
        AlertHelper.negativeButtonCallback = callback;
    }

    // Static method to show the alert
    public static void showAlert(Context context) {

        // Inflate the custom layout using View Binding
        AlertLayoutBinding binding = AlertLayoutBinding.inflate(LayoutInflater.from(context));

        // Create and configure the AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(binding.getRoot());

        // Set title and message from the static fields
        binding.tvHeading.setText(title);
        binding.tvMessage.setText(message);

        // Set button texts
        binding.btnPositive.setText(positiveButtonText);
        binding.btnNegative.setText(negativeButtonText);

        // Show or hide the negative button based on text
        binding.btnNegative.setVisibility(negativeButtonText.isEmpty() ? View.GONE : View.VISIBLE);

        builder.setCancelable(isCancelable);

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();



        // Set button click listeners
        if (positiveButtonCallback != null) {
            binding.btnPositive.setOnClickListener(v -> {
                positiveButtonCallback.onClick(alertDialog, DialogInterface.BUTTON_POSITIVE);
                // Dismiss dialog after positive action if needed
                alertDialog.dismiss();
            });
        }

        if (negativeButtonCallback != null) {
            binding.btnNegative.setOnClickListener(v -> {
                negativeButtonCallback.onClick(alertDialog, DialogInterface.BUTTON_NEGATIVE);
                // Dismiss dialog after negative action if needed
                alertDialog.dismiss();
            });
        }

        // Show the AlertDialog
        alertDialog.show();

    }


    // Method to reset the variables to default
    public static void reset() {
        title = "Alert";
        message = "Something went wrong!";
        positiveButtonText = "OK";
        negativeButtonText = "Cancel";
        isCancelable = true;
        positiveButtonCallback = null;
        negativeButtonCallback = null;
    }
}
