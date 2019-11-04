package com.example.leiaaqui;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class MaskEditUtil {

    /** Validação para aplicar a máscara **/
    public static final String FORMAT_CPF = "###.###.###-##";
    public static final String FORMAT_FONE = "(##) # ####-####";
    public static final String FORMAT_DATE = "##/##/####";

    public static TextWatcher mask(final EditText ediTxt, final TextInputLayout inpLayout, final String mask, final boolean validar) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void afterTextChanged(final Editable s) {}

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                final String str = MaskEditUtil.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (final char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (final Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());

                /* Validar data, cpf e telefone */
                if(mask.equals(FORMAT_DATE) && ediTxt.length() < 10) {
                    inpLayout.setError("Data inválida");
                } else if (mask.equals(FORMAT_CPF) && ediTxt.length() < 14) {
                    inpLayout.setError("CPF inválido");
                } else if (mask.equals(FORMAT_FONE) && ediTxt.length() < 16) {
                    inpLayout.setError("Telefone inválido");
                } else {
                    inpLayout.setError("");
                }
            }
        };
    }

    public static String unmask(final String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
    }

}
