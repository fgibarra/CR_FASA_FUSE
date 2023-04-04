package cl.ahumada.fuse.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Constantes {

	public final static String FASA_PATTERN = "ddMMyyyyHHmmss";
	public final static DateFormat FASA_FORMATTER = new SimpleDateFormat(FASA_PATTERN);

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
	
    /**
     * Dump hexadecimal y ascii del buffer
     * @param dato
     * @return
     */
    public String dumpHexadecimal(byte[] dato) {
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
}
