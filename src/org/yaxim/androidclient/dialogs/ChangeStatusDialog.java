package org.yaxim.androidclient.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.yaxim.androidclient.MainWindow;
import org.yaxim.androidclient.R;
import org.yaxim.androidclient.util.StatusMode;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;

public class ChangeStatusDialog extends AlertDialog {

	private final Spinner mStatus;

	private final AutoCompleteTextView mMessage;

	private final MainWindow mContext;

	public ChangeStatusDialog(final MainWindow context, final StatusMode status_mode,
			final String status_message, final String[] status_message_history) {
		super(context);

		mContext = context;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View group = inflater.inflate(R.layout.statusview, null, false);

		List<StatusMode> modes = new ArrayList<StatusMode>(
				Arrays.asList(StatusMode.values()));
		// the user can not set statusmode "subscribe", it is only for incoming presences
		modes.remove(StatusMode.subscribe);

		Collections.sort(modes, new Comparator<StatusMode>() {
			public int compare(StatusMode object1, StatusMode object2) {
				return object2.compareTo(object1);
			}
		});

		mStatus = (Spinner) group.findViewById(R.id.statusview_spinner);
		StatusModeAdapter mStatusAdapter;
		mStatusAdapter = new StatusModeAdapter(context, android.R.layout.simple_spinner_item, modes);
		mStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStatus.setAdapter(mStatusAdapter);

		for (int i = 0; i < modes.size(); i++) {
			if (modes.get(i).equals(status_mode)) {
				mStatus.setSelection(i);
			}
		}

		mMessage = (AutoCompleteTextView) group.findViewById(R.id.statusview_message);
		mMessage.setText(status_message);
		mMessage.setAdapter(new ArrayAdapter<String>(context,
					android.R.layout.simple_dropdown_item_1line, status_message_history));
		mMessage.setThreshold(1);

		setTitle(R.string.statuspopup_name);
		setView(group);

		setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok),
				new OkListener());

		setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel),
				(OnClickListener) null);
	}

	private class OkListener implements OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			StatusMode status = (StatusMode) mStatus.getSelectedItem();
			String message = mMessage.getText().toString();

			mContext.setAndSaveStatus(status, message);
		}
	}

	private class StatusModeAdapter extends ArrayAdapter<StatusMode> {

		public StatusModeAdapter(Context context, int textViewResourceId,
				List<StatusMode> modes) {

			super(context, textViewResourceId, modes);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {

			TextView textView = (TextView) super.getDropDownView(position,
					convertView, parent);
			textView.setText(getItem(position).getTextId());
			return textView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView textView = (TextView) super.getView(position, convertView,
					parent);
			textView.setText(getItem(position).getTextId());
			textView.setPadding(0, 0, 0, 0);
			return textView;
		}

	}

}
