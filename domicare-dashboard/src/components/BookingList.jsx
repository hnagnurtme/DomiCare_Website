import React, { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axios from 'axios';

const BookingList = () => {
  const [bookings, setBookings] = useState([]);
  const [tokenInput, setTokenInput] = useState('');
  const [token, setToken] = useState(localStorage.getItem('accessToken') || '');

  useEffect(() => {
    if (!token) return;

    // Save token to localStorage for future use
    localStorage.setItem('accessToken', token);

    // Fetch initial data with Authorization header
    axios.get('http://localhost:8080/api/bookings', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(response => {
        const data = response.data?.data?.data || [];
        setBookings(data);
      })
      .catch(error => console.error('Lỗi khi tải danh sách bookings:', error));

    // Setup STOMP client
    const stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      debug: (str) => console.log('[STOMP] ' + str),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('✅ WebSocket connected');

        stompClient.subscribe('/topic/bookings/new', (message) => {
          const newBooking = JSON.parse(message.body);
          setBookings(prev => {
            const exists = prev.find(b => b.id === newBooking.id);
            if (exists) return prev;
            return [newBooking, ...prev];
          });
        });

        stompClient.subscribe('/topic/bookings/delete', (message) => {
          const deletedId = JSON.parse(message.body);
          setBookings(prev => prev.filter(b => b.id !== deletedId));
        });

        stompClient.subscribe('/topic/bookings/update', (message) => {
          const updatedBooking = JSON.parse(message.body);
          setBookings(prev => prev.map(b => b.id === updatedBooking.id ? updatedBooking : b));
        });
      },
    });

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [token]);

  if (!Array.isArray(bookings)) {
    return <div>❌ Dữ liệu bookings không hợp lệ</div>;
  }

  return (
    <div>
      <h2>📋 Danh sách đặt dịch vụ</h2>

      {!token && (
        <div>
          <label htmlFor="token-input"><strong>🔐 Nhập Access Token:</strong></label><br />
          <input
            type="text"
            id="token-input"
            value={tokenInput}
            onChange={(e) => setTokenInput(e.target.value)}
            style={{ width: '300px', margin: '10px 0' }}
          />
          <button onClick={() => setToken(tokenInput)}>Lưu Token</button>
        </div>
      )}

      {bookings.map((booking) => (
        <div key={booking.id} style={{ border: '1px solid #ccc', padding: '10px', margin: '10px' }}>
          <div><strong>ID:</strong> {booking.id}</div>
          <div><strong>🧑 Khách hàng:</strong> {booking.userDTO?.name || 'N/A'}</div>
          <div><strong>💵 Tổng tiền:</strong> {booking.totalPrice?.toLocaleString() || 'N/A'} đ</div>
          <div><strong>📌 Trạng thái:</strong> {booking.bookingStatus}</div>
          <div><strong>📅 Ngày tạo:</strong> {new Date(booking.createAt).toLocaleString()}</div>
          <div><strong>📍 Địa chỉ:</strong> {booking.address || 'N/A'}</div>
          <div><strong>📞 Số điện thoại:</strong> {booking.userDTO?.phone || 'N/A'}</div>
          <div><strong>📝 Ghi chú:</strong> {booking.note || 'N/A'}</div>
          <div><strong>🧼 Dịch vụ:</strong> {booking.products?.[0]?.name || 'N/A'}</div>
        </div>
      ))}
    </div>
  );
};

export default BookingList;
