package ps.hell.util.file.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DateConverter implements Converter {
	@Override
	public boolean canConvert(Class clazz) {
		return (Date.class).equals(clazz);
	}

	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Date date = (Date) object;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		writer.setValue(format.format(calendar.getTime()));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0,
			UnmarshallingContext arg1) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		try {
			return format.parse(arg0.getValue());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}