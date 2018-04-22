/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.TextInputControl;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

/**
 *
 * @author esanchez
 */
public class Formater {

    private static Formater INSTANCE = null;

    public SimpleDateFormat formatHour = new SimpleDateFormat("hh:mm a");
    public SimpleDateFormat formatFecha = new SimpleDateFormat("dd-MM-yyyy");
    public SimpleDateFormat formatFechaCorta = new SimpleDateFormat("dd/MM/yyyy");
    public SimpleDateFormat formatFechaHora = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
    public DateFormat formatoHora = new SimpleDateFormat("hh:mm a");
    public DateTimeFormatter formatoHora24 = DateTimeFormatter.ofPattern("HH:mm:ss");
    public DateTimeFormatter formatterFechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public DateTimeFormatter formatDateShort = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    public DateTimeFormatter formatDateMedium = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
    public DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00");
    public DecimalFormat integerFormat = new DecimalFormat("#,###,###,##0");

    /**
     * Constructor privado para evitar que se instancien objetos
     */
    private Formater() {
    }

    /**
     * Metodo para crear una instancia
     */
    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (Formater.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new Formater();
                }
            }
        }
    }

    /**
     * Metodo para obtener una instancia
     *
     * @return Instacia del contexto de la aplicación
     */
    public static Formater getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     * El método "clone" es sobreescrito por el siguiente que arroja una
     * excepción:
     *
     * @return Error en caso de intentar clonar
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Crea un formato numérico con dos decimales
     *
     * @return Formato numérico con dos decimales
     */
    public TextFormatter twoDecimalFormat() {
        UnaryOperator<Change> filter = (Change c) -> {
            if (c.getControlNewText().isEmpty()) {
                c.setText("0");
                c.setCaretPosition(1);
                return c;
            }

            if (c.getControlNewText().contains(",")) {
                ParsePosition parsePosition = new ParsePosition(0);
                Object object = decimalFormat.parse(c.getControlNewText(), parsePosition);

                if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                    return null;
                } else {
                    //return c;
                    Pattern validDoubleText = Pattern.compile("^[0-9]*+(\\.[0-9]{0,2})?$");
                    if (validDoubleText.matcher(c.getControlNewText().replace(",", "")).matches()) {
                        return c;
                    } else {
                        return null;
                    }
                }
            } else {
                Pattern validDoubleText = Pattern.compile("^[0-9]*+(\\.[0-9]{0,2})?$");
                if (validDoubleText.matcher(c.getControlNewText().replace(",", "")).matches()) {
                    return c;
                } else {
                    return null;
                }
            }
        };

        return new TextFormatter<>(new DoubleStringConverter(), 0.00, filter);
    }

    /**
     * Crea un formato numérico sin decimales
     *
     * @return Formato numérico sin decimales
     */
    public TextFormatter integerFormat() {
        return integerFormat(null);
    }

    /**
     * Crea un formato numérico sin decimales
     *
     * @param length Cantidad máxima de caracteres
     * @return Formato numérico sin decimales
     */
    public TextFormatter integerFormat(Integer length) {
        TextFormatter numericFormat = new TextFormatter<>(c
                -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            if (length != null) {
                if (((TextInputControl) c.getControl()).getLength() >= length && !c.isDeleted()) {
                    return null;
                }
                if (c.getText().length() > length && !c.isDeleted()) {
                    return null;
                }
            }

            Pattern validDoubleText = Pattern.compile("\\d+");
            if (validDoubleText.matcher(c.getControlNewText()).matches()) {
                return c;
            } else {
                return null;
            }
        });
        return numericFormat;
    }

    /**
     * Crea un formato para el patron de los folios (A######)
     *
     * @return Formato especial para folios
     */
    public TextFormatter folioFormat() {
        TextFormatter maxLength7Format = new TextFormatter<>(c
                -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            if (((TextField) c.getControl()).getLength() >= 7 && !c.isDeleted()) {
                return null;
            }
            if (c.getText().length() > 7 && !c.isDeleted()) {
                return null;
            }

            c.setText(c.getText().toUpperCase());
            return c;
        });
        return maxLength7Format;
    }

    public TextFormatter maxLengthFormat(Integer length) {
        TextFormatter maxLengthFormat = new TextFormatter<>(c
                -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            if (((TextInputControl) c.getControl()).getLength() >= length && !c.isDeleted()) {
                return null;
            }
            if (c.getText().length() > length && !c.isDeleted()) {
                return null;
            }
            return c;
        });
        return maxLengthFormat;
    }

    public TextFormatter upperCase(Integer length) {
        TextFormatter maxLengthFormat = new TextFormatter<>(c
                -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            c.setText(c.getText().toUpperCase());

            if (length != null) {
                if (((TextInputControl) c.getControl()).getLength() >= length && !c.isDeleted()) {
                    return null;
                }
                if (c.getText().length() > length && !c.isDeleted()) {
                    return null;
                }
            }
            return c;
        });
        return maxLengthFormat;
    }

    public <ROW, T extends Temporal> Callback<TableColumn<ROW, T>, TableCell<ROW, T>> getDateCell(DateTimeFormatter format) {
        return column -> {
            return new TableCell<ROW, T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(format.format(item));
                    }
                }
            };
        };
    }

    public StringConverter<String> stringConverter() {

        StringConverter<String> converter = new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        };

        return converter;
    }

    public String lpad(String text, Integer length, String caracter) {
        return String.format("%" + length + "s", text).replace(" ", caracter);
    }
}
