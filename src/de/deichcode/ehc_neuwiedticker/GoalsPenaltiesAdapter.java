package de.deichcode.ehc_neuwiedticker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GoalsPenaltiesAdapter extends ArrayAdapter<Tick> {
	private Context context;
	private int layoutRessourceId;   
	private Tick data[] = null;
	
	public GoalsPenaltiesAdapter (Context context, int layoutRessourceId, Tick[] data){
		super(context,layoutRessourceId, data);
		this.layoutRessourceId = layoutRessourceId;
        this.context = context;
        this.data = data;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TickHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            row = inflater.inflate(this.layoutRessourceId, parent, false);
           
            holder = new TickHolder();
            holder.first = (TextView)row.findViewById(R.id.goals_penalties_first);
            holder.second = (TextView)row.findViewById(R.id.goals_penalties_second);
            holder.third = (TextView)row.findViewById(R.id.goals_penalties_third);
           
            row.setTag(holder);
        }
        else
        {
            holder = (TickHolder)row.getTag();
        }
       
        Tick ticker = this.data[position];
        holder.first.setText(ticker.getHeadline());
        holder.second.setText(ticker.getTime());
        holder.third.setText(ticker.getText());
       
        return row;
    }
	
	static class TickHolder {
		TextView first;
		TextView second;
		TextView third;
	}

}
