package com.backend.domicare.utils;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QueryAdvance {

    /**
     * Builds a dynamic Predicate based on the search string.
     * 
     * @param searchString Chuỗi tìm kiếm (e.g., "price>20", "description=abc", "name")
     * @param root Root của entity trong Criteria API
     * @param query CriteriaQuery object
     * @param cb CriteriaBuilder object
     * @return Predicate tương ứng hoặc null nếu không có điều kiện
     */
    public static Predicate buildDynamicPredicate(String searchString, Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        // Kiểm tra chuỗi rỗng hoặc null
        if (searchString == null || searchString.trim().isEmpty()) {
            return null;
        }

        searchString = searchString.trim();
        
        // Các toán tử hỗ trợ
        String[] operators = {">=", "<=", ">", "<", "="};
        String operator = "="; // Toán tử mặc định
        String fieldName = searchString;
        String value = "";

        // Tìm toán tử trong chuỗi
        for (String op : operators) {
            if (searchString.contains(op)) {
                operator = op;
                String[] parts = searchString.split(op, 2); // Tách thành field và value
                fieldName = parts[0].trim();
                value = parts[1].trim();
                break;
            }
        }

        // Nếu không có toán tử rõ ràng, mặc định là "like" với field "description"
        if (operator.equals("=") && !searchString.contains("=")) {
            fieldName = "description"; // Field mặc định
            value = searchString;
            return cb.like(cb.lower(root.get(fieldName)), "%" + value.toLowerCase() + "%");
        }

        // Xử lý dựa trên kiểu dữ liệu của field
        try {
            Class<?> fieldType = root.get(fieldName).getJavaType();

            // Xử lý số (Integer, Double, Long, v.v.)
            if (Number.class.isAssignableFrom(fieldType)) {
                double numericValue = Double.parseDouble(value);
                switch (operator) {
                    case ">":
                        return cb.greaterThan(root.get(fieldName), numericValue);
                    case ">=":
                        return cb.greaterThanOrEqualTo(root.get(fieldName), numericValue);
                    case "<":
                        return cb.lessThan(root.get(fieldName), numericValue);
                    case "<=":
                        return cb.lessThanOrEqualTo(root.get(fieldName), numericValue);
                    case "=":
                        return cb.equal(root.get(fieldName), numericValue);
                    default:
                        throw new IllegalArgumentException("Toán tử không hỗ trợ cho số: " + operator);
                }
            }

            // Xử lý chuỗi (String)
            if (fieldType == String.class) {
                switch (operator) {
                    case "=":
                        return cb.equal(cb.lower(root.get(fieldName)), value.toLowerCase());
                    case ">":
                    case ">=":
                    case "<":
                    case "<=":
                        throw new IllegalArgumentException("Toán tử " + operator + " không áp dụng cho chuỗi");
                    default:
                        return cb.like(cb.lower(root.get(fieldName)), "%" + value.toLowerCase() + "%");
                }
            }

            // Xử lý các kiểu dữ liệu khác (boolean, date, v.v.) nếu cần
            if (fieldType == Boolean.class) {
                boolean boolValue = Boolean.parseBoolean(value);
                return cb.equal(root.get(fieldName), boolValue);
            }

        } catch (IllegalArgumentException e) {
            // Nếu không parse được số hoặc field không hợp lệ, xử lý như chuỗi với like hoặc equal
            return cb.like(cb.lower(root.get(fieldName)), "%" + value.toLowerCase() + "%");
        }

        return null; // Trường hợp không xử lý được
    }

    /**
     * Áp dụng Predicate vào Specification hiện tại.
     * 
     * @param spec Specification hiện tại
     * @param searchString Chuỗi tìm kiếm
     * @return Specification đã được cập nhật
     */
    public static <T> Specification<T> applySearch(Specification<T> spec, String searchString) {
        if (searchString != null && !searchString.trim().isEmpty()) {
            return spec == null 
                ? (root, query, cb) -> buildDynamicPredicate(searchString, root, query, cb)
                : spec.and((root, query, cb) -> buildDynamicPredicate(searchString, root, query, cb));
        }
        return spec;
    }
}

// Interface Specification (nếu chưa có trong project)
interface Specification<T> {
    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);

    default Specification<T> and(Specification<T> other) {
        return (root, query, cb) -> {
            Predicate thisPredicate = this.toPredicate(root, query, cb);
            Predicate otherPredicate = other.toPredicate(root, query, cb);
            return cb.and(thisPredicate, otherPredicate);
        };
    }
}