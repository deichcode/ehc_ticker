package de.deichcode.ehc_neuwiedticker;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TickerFragment extends Fragment {
	private static View v;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.ticker, container, false);

		ParsAnzeige score = new ParsAnzeige(v); // Parsen der Toranzeige
		score.execute();

		ParsTicker ticker = new ParsTicker(v); // Parsen der Ticker-News
		ticker.execute();

		TextView txt = (TextView) v.findViewById(R.id.textViewScore);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/minisystem.regular.otf");
		txt.setTypeface(font);
		txt = (TextView) v.findViewById(R.id.textViewFirsthThirdScore);
		txt.setTypeface(font);
		txt = (TextView) v.findViewById(R.id.textViewSecondThirdScore);
		txt.setTypeface(font);
		txt = (TextView) v.findViewById(R.id.textViewThirdThirdScore);
		txt.setTypeface(font);
		txt = (TextView) v.findViewById(R.id.textViewOvertimeScore);
		txt.setTypeface(font);

		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		DownloadImage setHomeImage = new DownloadImage(0, (ImageView) getActivity().findViewById(R.id.imageViewHome), getActivity().findViewById(R.id.progressHome)); // Load
		DownloadImage setGuestImage = new DownloadImage(1, (ImageView) getActivity().findViewById(R.id.imageViewGuest), getActivity().findViewById(R.id.progressGuest)); // Load
		setHomeImage.execute();
		setGuestImage.execute();
		
		final ListView lv = (ListView) getView().findViewById(R.id.listViewTicker);
		lv.setLongClickable(true);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
            	Toast.makeText(getActivity(), "Text in Zwischenablage kopiert", Toast.LENGTH_SHORT).show();
            	TextView text = (TextView) arg1.findViewById(R.id.ticker_text);
            	String copy = text.getText().toString();
            	copyStringToClipboard(copy);               	
                return true;
            }
        }); 
	}

	protected void copyStringToClipboard(String copy) {
		ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("copy", copy);
		clipboard.setPrimaryClip(clip);
	}

	public static void refresh() {
		ParsAnzeige score = new ParsAnzeige(v); // Parsen der Toranzeige
		score.execute();
		ParsTicker ticker = new ParsTicker(v); // Parsen der Ticker-News
		ticker.execute();
	}

}
