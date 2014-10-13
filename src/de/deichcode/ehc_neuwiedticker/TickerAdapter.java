package de.deichcode.ehc_neuwiedticker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TickerAdapter extends ArrayAdapter<Tick> {
	private Context context;
	private int layoutRessourceId;
	private Tick data[] = null;
	private TickHolder holder = null;

	public TickerAdapter(Context context, int layoutRessourceId, Tick[] data) {
		super(context, layoutRessourceId, data);
		this.layoutRessourceId = layoutRessourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutRessourceId, parent, false);

			holder = new TickHolder();
			holder.headline = (TextView) row.findViewById(R.id.ticker_headline);
			holder.time = (TextView) row.findViewById(R.id.ticker_time);
			holder.text = (TextView) row.findViewById(R.id.ticker_text);

			row.setTag(holder);
		}
		else {
			holder = (TickHolder) row.getTag();
		}

		Tick ticker = data[position];
		holder.headline.setText(ticker.getHeadline());
		holder.time.setText(ticker.getTime());
		holder.text.setText(ticker.getText());
		
		return row;
	}
	
	static class TickHolder {
		TextView headline;
		TextView time;
		TextView text;
	}

}
