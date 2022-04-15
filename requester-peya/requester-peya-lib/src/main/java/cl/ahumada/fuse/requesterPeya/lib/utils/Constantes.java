package cl.ahumada.fuse.requesterPeya.lib.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* para dumpXml (requiere jaxb-api en el pom)
<!-- https://mvnrepository.com/artifact/javax.xml.bind/jaxb-api -->
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
*/
import org.apache.log4j.Logger;

public final class Constantes {

	public static final int STX = 2, ETX = 3, CR = 13, LF = 10, ETB = 27, FS = 28, GS = 29, RS = 30, US = 31;
	// FS : File sepaator, GS = Group separtor, RS = record separator, US = Unit separator

    public static final int PORT = 3100;
    public static final String VERSION = "1";

    public static final String RESULTADO_OK = "0";
    public static final String RESULTADO_ERROR_BD = "ERROR 1";
    public static final String RESULTADO_ERROR_TIMEOUT = "ERROR 2";
    public static final String FORMATO_FECHA_ERRONEO = "ERROR 3";
    public static final String CAMPO_OBLIGATORIO = "ERROR 4";
    public static final String NUMERO_FUERA_RANGO = "ERROR 5";
    public static final String RESULTADO_SP_ERROR = "ERROR 6";

	public final static String FASA_PATTERN = "ddMMyyyyHHmmss";
	public final static DateFormat FASA_FORMATTER = new SimpleDateFormat(FASA_PATTERN);

    protected static Logger logger = Logger.getLogger("Constantes");

    public static Map<String, String> mapDescripciones = new HashMap<String,String>();
    static {
    	mapDescripciones.put(RESULTADO_OK, "ACK");
    	mapDescripciones.put(RESULTADO_ERROR_BD, "Error al accesar la Base de datos. Causa: {@} ");
    	mapDescripciones.put(RESULTADO_ERROR_TIMEOUT, "Error al comunicarse con servicio {@}. Causa: NO RESPONDE ");
    	mapDescripciones.put(FORMATO_FECHA_ERRONEO, "Formato de fecha erroneo. Campo {@}. Espera YYYYMMDD");
    	mapDescripciones.put(CAMPO_OBLIGATORIO, "Campo {@} omitido. Es obligatorio");
    	mapDescripciones.put(NUMERO_FUERA_RANGO, "Campo {@} contiene un numero fuera de rango.");
    	mapDescripciones.put(RESULTADO_SP_ERROR, "Store Procedure {@} indica Error.");
    }

    /*
    public static String dumpXml(Object xml) {
		try {
			JAXBContext jc = JAXBContext.newInstance(xml.getClass().getPackage().getName());
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			m.marshal(xml, new PrintStream(out));
			m.marshal(xml, System.out);
			return out.toString();
		} catch (Exception e) {
			logger.error("dumpXml", e);
			return null;
		}
    }
*/
    /**
     * devuelve representacion hex del string entregado como parametro
     */
    public static String representacionHexadecimal(String dato) {
    	if (dato == null) return null;
        return representacionHexadecimal(dato.getBytes());
    }

    /**
     * devuelve representacion hex del string entregado como parametro
     */
    public static String representacionHexadecimal(StringBuffer dato) {
    	if (dato == null) return null;
        int len = dato.length();
        char[] x = new char[len];

        for (int i = 0; i < len; i++)
            x[i] = dato.charAt(i);

        return representacionHexadecimal(x);
    }

   /**
     * devuelve representacion hex del string entregado como parametro
     */
    public static String representacionHexadecimal(byte[] dato) {
    	if (dato == null) return null;
        StringBuffer sb = new StringBuffer().append('|');

        for (int len = dato.length, i = 0; i < len; i++) {
            char c = (char) (dato[i] & '\u00FF');
            String s = Integer.toHexString((int)c);

            if (s.length() < 2) {
                sb.append('0');
            } else if (s.length() > 2) {
                s = s.substring(s.length() - 2);
            }

            sb.append(s).append('|');
        }

        return sb.toString();
    }

    /**
     * devuelve la representacion hexadecimal
     * @param dato
     * @return
     */
    public static String representacionHexadecimal(char[] dato) {
    	if (dato == null) return null;
        return representacionHexadecimal(dato, dato.length);
    }

    /**
     * devuelve la representacion hexadecimal
     * @param dato
     * @param count
     * @return
     */
    public static String representacionHexadecimal(char[] dato, int count) {
    	if (dato == null) return null;
        StringBuffer sb = new StringBuffer().append('|');

        for (int len = count, i = 0; i < len; i++) {
            char c = (char) (dato[i] & '\u00FF');
            String s = Integer.toHexString((int)c);

            if (s.length() < 2) {
                sb.append('0');
            } else if (s.length() > 2) {
                s = s.substring(s.length() - 2);
            }

            sb.append(s).append('|');
        }

        return sb.toString();
    }

    /**
     * @param dato
     * @return
     */
    public static String dumpHexadecimal(StringBuffer dato) {
    	if (dato != null)
    		return dumpHexadecimal(dato.toString().getBytes());
    	return null;
    }
   /**
     * @param dato
     * @return
     */
    public static String dumpHexadecimal(String dato) {
    	if (dato != null)
    		return dumpHexadecimal(dato.getBytes());
    	return null;
    }
    /**
     * Dump hexadecimal y ascii del buffer
     * @param dato
     * @return
     */
    public static String dumpHexadecimal(byte[] dato) {
        StringBuffer sb = new StringBuffer();
        StringBuffer ascii = new StringBuffer().append('|');
        sb.append(
            "0--|--|--|--|--|--|--|--|--|--10-|--|--|--|--|--|--|--|--|--")
          .append("20-|--|--|--|--|--|--|--|--|--").append("30-|--|--|--|--|--|--|--|--|--|");

        sb.append('\n').append('|');

        for (int len = dato.length, i = 0, j = 39; i < len; i++, j--) {
            byte c = dato[i];
            String s = Integer.toHexString((int) c);

            if (s.length() < 2) {
                sb.append('0');
            } else if (s.length() > 2) {
                s = s.substring(s.length() - 2);
            }

            sb.append(s).append('|');

            // parte ascii
            char ch = (char) c;

            if (((int) ch >= 32) && ((int) ch < 128)) {
                ascii.append(' ').append(ch);
            } else {
                ascii.append("  ");
            }

            ascii.append('|');

            if (j <= 0) {
                sb.append('\n').append(ascii).append('\n').append('|');
                ascii = new StringBuffer().append('|');
                j = 40;
            }
        }

        sb.append('\n').append(ascii).append('\n').append('|'); // el resto de los 40

        return sb.toString();
    }

	public static String toString(Object obj) {
		if (obj == null || obj instanceof String)
			return (String) obj;

		if (obj instanceof Integer || obj instanceof Float || obj instanceof Double || obj instanceof BigDecimal
				|| obj instanceof Short)
			return obj.toString();

		if (obj instanceof Timestamp)
			return FASA_FORMATTER.format(obj);

		return obj.toString();
	}

	public static Long toLong(Object obj) {
		if (obj == null)
			return (Long)null;

		if (obj instanceof String)
			try {
				return Long.valueOf((String)obj);
			} catch (NumberFormatException e) {
				return (Long)null;
			}
		if (obj instanceof Integer)
			return Long.valueOf(((Integer)obj).longValue());
		if (obj instanceof Float)
			return Long.valueOf(((Float)obj).longValue());
		if (obj instanceof Double)
			return Long.valueOf(((Double)obj).longValue());
		if (obj instanceof BigDecimal)
			return Long.valueOf(((BigDecimal)obj).longValue());
		if (obj instanceof Short)
			return Long.valueOf(((Short)obj).longValue());
		return (Long)obj;
	}

	public static String obj2String(Object obj) {
		if (obj == null) return null;
		if (obj instanceof String) return (String)obj;
		if (obj instanceof Double) return String.format("%d", ((Double)obj).longValue());
		if (obj instanceof Float) return String.format("%d", ((Float)obj).longValue());
		if (obj instanceof Integer) return String.format("%d", (Integer)obj);
		if (obj instanceof Long) return String.format("%d", (Long)obj);
		return obj.toString();
	}

	public static String toComaSeparator(Object[] datos) {
		StringBuffer sb = new StringBuffer();
		if (datos != null) {
			for (Object d : datos) {
				sb.append(d!=null?d.toString():"NULO").append(',');
			}
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String toComaSeparator(List<?> datos) {
		StringBuffer sb = new StringBuffer();
		if (datos != null) {
			for (Object d : datos) {
				sb.append(d!=null?d.toString():"NULO").append(',');
			}
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String rellena(String valor, int largo, char c) {
        if (valor == null) valor="";
        if (valor.length() < largo) {
            StringBuffer sb = new StringBuffer();
            for (int j=largo - valor.length(); j>0; j--)
                sb.append('0');
            sb.append(valor);
            return sb.toString();
        }

        return valor;
	}
}
