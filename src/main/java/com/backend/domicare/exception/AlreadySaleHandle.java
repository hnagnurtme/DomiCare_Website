package com.backend.domicare.exception;

public class AlreadySaleHandle extends RuntimeException {
    public AlreadySaleHandle(String message) {
        super("Đã có nhân viên khác xử lý đơn hàng này, không thể thực hiện thao tác này");
    }
    
}
