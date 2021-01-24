package com.geoffreyfrey.bidfivehundredscorekeeper;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by geoff on 5/10/16.
 */
public class SetPlayerQuantity extends DialogFragment {

    //set Listener
    PlayerQuantityListener fragmentCommander;

    //create interface
    public interface PlayerQuantityListener{
        void setPlayerQuantity(int playerQuantity);
    }

    //On Attach to send info back to MainActivity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            fragmentCommander = (PlayerQuantityListener) activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString());
        }

    }

    //Creates the dialog.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        //string sequence with the options.
        final CharSequence player_quantity_list[] = getResources().getStringArray(R.array.player_quantity_options);

        //creating the builder
        AlertDialog.Builder player_quantity_builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.select_player_quantity);

        //populating the options
        player_quantity_builder.setItems(player_quantity_list,
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                //the which argument contains the index position of the selected item.
                int quantity;

                switch (which) {
                    case 0: {
                        quantity = 4;
                        break;
                    }
                    case 1: {
                        quantity = 5;
                        break;
                    }
                    case 2: {
                        quantity = 6;
                        break;
                    }default:
                        quantity = 0;
                }

                //sendint the chosen value to the activity through the buttonClicked method defined below.
                buttonClicked(quantity);
            }
        });
        return player_quantity_builder.create();

    }

    public void buttonClicked(int quantity){
        fragmentCommander.setPlayerQuantity(quantity);
    }
}
