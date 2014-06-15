


package opciones;


import org.w3c.dom.*;

public class Vista {

	final public static int TIPO_REC=1;
	final public static int TIPO_DYV=2;
	
	final public static String codigos[]={	"V_ARBOL",	"V_PILA",	"V_TRAZA",	"V_ESTRUC"};
									//			0			1			2			3	
	String codigo;
	
	boolean activa=false;
	
	int tipo=Vista.TIPO_REC;
	
	int panel=1;
	
	public Vista(String codigo)
	{
		this.codigo=codigo;
		
	}
	
	
	public String getCodigo()
	{
		return this.codigo;
	}
	
	public void setActiva(boolean valor)
	{
		this.activa=valor;
	}
	
	public boolean esActiva()
	{
		return this.activa;
	}
	
	public void setTipo(int tipo)
	{
		switch(tipo)
		{
			case Vista.TIPO_REC:
				this.tipo=TIPO_REC;
				break;
			case Vista.TIPO_DYV:
				this.tipo=TIPO_DYV;
				break;
			default:
				this.tipo=TIPO_REC;
		}
	}
	
	public int getTipo()
	{
		return this.tipo;
	}
	
	public void setPanel(int x)
	{
		if (x==2)
			this.panel=2;
		else
			this.panel=1;
	}
	
	public int getPanel()
	{
		return this.panel;
	}
	
	
	
	public Element getRepresentacionElement(Document d)
	{
		Element e=d.createElement("Vista");
		
		e.setAttribute("codigo", this.getCodigo());
		e.setAttribute("activa", ""+this.esActiva());
		e.setAttribute("tipo", ""+this.getTipo());
		e.setAttribute("panel", ""+this.getPanel());
		
		
		return e;
	}
	
	
	
	public static int getPosic(String nombre)
	{
		for (int i=0; i<codigos.length; i++)
			if (nombre.equals(codigos[i]))
				return i;

		return -1;
	}
	
}