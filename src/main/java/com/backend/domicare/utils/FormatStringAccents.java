package com.backend.domicare.utils;

import java.text.Normalizer;

public class FormatStringAccents {
    public static String removeTones(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        // Chuẩn hóa và loại bỏ dấu
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Thay thế ký tự đặc biệt: đ → d
        str = str.replaceAll("đ", "d");
        str = str.replaceAll("Đ", "D");

        return str;
    }
}
