package com.salatiga.code;

import java.util.List;
import java.util.Scanner;
import java.util.zip.Inflater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Class ini adalah Adapter untuk daftar gempa
 * @author Hendro Steven Tampake
 * @version 1.0
 *
 */
public class GempaListAdapter extends ArrayAdapter<Gempa>{

	private final Context context;
	private final List<Gempa> listGempa;
	private Scanner scanner;
	
	public GempaListAdapter(Context context, List<Gempa> values) {
		super(context, R.layout.gempa_item, values);
		this.context = context;
		this.listGempa = values;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView==null){
			convertView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.gempa_item, parent, false);
			holder = new ViewHolder();
			holder.txtWilayah =  (TextView) convertView.findViewById(R.id.txtWilayah);
			holder.txtTanggalJam = (TextView)convertView.findViewById(R.id.txtTanggalJam);		
			holder.txtMagnitude = (TextView)convertView.findViewById(R.id.txtMagnitude);
			holder.txtSr = (TextView)convertView.findViewById(R.id.txtSr);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		/*LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.gempa_item, parent, false);
		TextView txtWilayah = (TextView) rowView.findViewById(R.id.txtWilayah);
		TextView txtTanggalJam = (TextView)rowView.findViewById(R.id.txtTanggalJam);		
		TextView txtMagnitude = (TextView)rowView.findViewById(R.id.txtMagnitude);
		TextView txtSr = (TextView)rowView.findViewById(R.id.txtSr);
		*/
		
		Gempa gempa = listGempa.get(position);	
		
		holder.txtWilayah.setText(gempa.getKeterangan());
		holder.txtTanggalJam.setText(gempa.getTanggal());
		scanner = new Scanner(gempa.getMagnitude());
		double magnitude = scanner.nextDouble();
		
		holder.txtMagnitude.setText(magnitude+"");
		if(magnitude<3.9){
			holder.txtMagnitude.setTextColor(context.getResources().getColor(R.color.level1));
			holder.txtSr.setTextColor(context.getResources().getColor(R.color.level1));
		}else if(magnitude>=4.0 && magnitude<=5.9){
			holder.txtMagnitude.setTextColor(context.getResources().getColor(R.color.level2));
			holder.txtSr.setTextColor(context.getResources().getColor(R.color.level2));
		}else if(magnitude>=6.0 && magnitude<=6.9){
			holder.txtMagnitude.setTextColor(context.getResources().getColor(R.color.level3));
			holder.txtSr.setTextColor(context.getResources().getColor(R.color.level3));
		}else if(magnitude>=6.9){
			holder.txtMagnitude.setTextColor(context.getResources().getColor(R.color.level4));
			holder.txtSr.setTextColor(context.getResources().getColor(R.color.level4));
		}
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView txtWilayah;
		TextView txtTanggalJam; 	
		TextView txtMagnitude;
		TextView txtSr;
	}
}


