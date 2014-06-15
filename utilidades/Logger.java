package utilidades;

import java.io.DataOutputStream;

import java.io.FileOutputStream;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class Logger {
	
	static String fichero="log-SREC.txt";	// sirve si cogemos archivos LOG a través de web, cogeremos IP desde allí
	//static String fichero="log-SREC-"+ServiciosString.direccionIP()+".txt";

	boolean debug=true;
	


	
	public static void log_write(String s)
	{
		final String cadena=s;
		new Thread() {
			public void run()
			{
		
				Calendar c = new GregorianCalendar();
				String fecha[]=getFechaActual(c);
				String hora[]=getHoraActual(c);
				
				
				String cadena2=fecha[0]+"/"+fecha[1]+"/"+fecha[2]+" "+hora[0]+":"+hora[1]+":"+hora[2]+" | "+cadena+"\r\n";
				try {
					  FileOutputStream file = new FileOutputStream(fichero,true); 
					  DataOutputStream out   = new DataOutputStream(file); 
					  out.writeBytes(cadena2); 
					  out.flush(); 
					  out.close(); 
				} catch (Exception e) {System.out.println("Exception en log_write");}
			}
		}.start();
	}

	
	
	public static String[] getFechaActual(Calendar c)
	{
		int fecha[]=new int[3];
		String fech[]=new String[3];
				
		fecha[0]=c.get(Calendar.DAY_OF_MONTH);
		fecha[1]=(c.get(Calendar.MONTH)+1);
		fecha[2]=c.get(Calendar.YEAR);
				
		if (fecha[0]<10)
			fech[0]="0"+fecha[0];
		else
			fech[0]=""+fecha[0];
			
		if (fecha[1]<10)
			fech[1]="0"+fecha[1];
		else
			fech[1]=""+fecha[1];
		
		fech[2]=""+fecha[2];
		return fech;
	}
	
	public static String[] getHoraActual(Calendar c)
	{
		int hora[]=new int[3];
		String h[]=new String[3];
				
		hora[0]=c.get(Calendar.HOUR_OF_DAY);
		hora[1]=c.get(Calendar.MINUTE);
		hora[2]=c.get(Calendar.SECOND);
				
		if (hora[0]<10)
			h[0]="0"+hora[0];
		else
			h[0]=""+hora[0];

			
		if (hora[1]<10)
			h[1]="0"+hora[1];
		else
			h[1]=""+hora[1];
		
		if (hora[2]<10)
			h[2]="0"+hora[2];
		else
			h[2]=""+hora[2];
		
		return h;
	}

}
