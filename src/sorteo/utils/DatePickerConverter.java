/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;

public class DatePickerConverter extends StringConverter {

    // Default Date Pattern
    private String pattern = "MM/dd/yyyy";

    // The Date Time Converter
    private DateTimeFormatter dtFormatter;

    public DatePickerConverter() {
        dtFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    public DatePickerConverter(String pattern) {
        this.pattern = pattern;
        dtFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    // Change String to LocalDate
    public LocalDate fromString(String text) {
        LocalDate date = null;
        if (text != null && !text.trim().isEmpty()) {
            date = LocalDate.parse(text, dtFormatter);
        }
        return date;

    }

    // Change LocalDate to String
    public String toString(LocalDate date) {
        String text = null;
        if (date != null) {
            text = dtFormatter.format(date);

        }
        return text;
    }

    @Override
    public String toString(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
