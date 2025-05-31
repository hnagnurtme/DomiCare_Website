import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

const BookingList = () => {
  const [bookings, setBookings] = useState([]);
  const [stompClient, setStompClient] = useState(null);
  const [wsStatus, setWsStatus] = useState({ connected: false, message: 'Disconnected' });

  const formatBookingData = (booking) => {
    if (booking.user && !booking.userDTO) {
      return {
        ...booking,
        userDTO: booking.user
      };
    }
    return booking;
  };

  const connectWebSocket = () => {
    console.log('🔌 Connecting to WebSocket...');
    setWsStatus({ connected: false, message: 'Connecting...' });

    const socket = new SockJS('http://localhost:8080/ws');
    const client = Stomp.over(socket);

    client.debug = (str) => console.log('STOMP:', str);

    let messageCount = 0;

    client.connect({}, () => {
      console.log('✅ WebSocket connected');
      setWsStatus({ connected: true, message: 'Connected' });
      setStompClient(client);

      client.subscribe('/topic/bookings/new', (message) => {
        messageCount++;
        const newBooking = JSON.parse(message.body);
        const formatted = formatBookingData(newBooking);

        console.log(`📨 Message #${messageCount}:`, formatted);

        setBookings(prev => {
          const ids = prev.map(b => b.id);
          if (ids.includes(formatted.id)) {
            console.log(`⚠️ Booking ID=${formatted.id} already exists. Skipping.`);
            return prev;
          }
          const updated = [formatted, ...prev];
          console.log('✅ Updated bookings:', updated.map(b => b.id));
          return updated;
        });
      });
    }, (error) => {
      console.error('❌ WebSocket error:', error);
      setWsStatus({ connected: false, message: 'Error: ' + error });
      setTimeout(connectWebSocket, 5000);
    });
  };

  useEffect(() => {
    connectWebSocket();
    return () => {
      if (stompClient && stompClient.connected) {
        stompClient.disconnect();
        console.log('🔌 WebSocket disconnected');
        setWsStatus({ connected: false, message: 'Disconnected' });
      }
    };
  }, []);

  useEffect(() => {
    fetch('http://localhost:8080/api/bookings')
      .then(res => res.json())
      .then(result => {
        const list = result.data?.data || [];
        const formatted = list.map(formatBookingData);
        setBookings(formatted);
      })
      .catch(err => {
        console.error('❌ API fetch error:', err);
      });
  }, []);

  return (
    <div style={{ padding: '20px' }}>
      <h2>DomiCare Booking Dashboard 🧼</h2>
      <div style={{ marginBottom: '15px' }}>
        <span style={{
          display: 'inline-block',
          width: '10px',
          height: '10px',
          borderRadius: '50%',
          backgroundColor: wsStatus.connected ? 'green' : 'red',
          marginRight: '8px'
        }}></span>
        <span>WebSocket: {wsStatus.message}</span>
        {!wsStatus.connected && (
          <button onClick={connectWebSocket} style={{
            marginLeft: '10px',
            padding: '5px 10px',
            backgroundColor: '#4CAF50',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer'
          }}>
            Retry Connection
          </button>
        )}
      </div>

      <ul style={{ listStyleType: 'none', padding: 0 }}>
        {bookings.map((booking) => (
          <li key={booking.id} style={{
            padding: '10px',
            margin: '5px 0',
            backgroundColor: '#f9f9f9',
            borderRadius: '4px',
            border: '1px solid #eee'
          }}>
            <div><strong>ID:</strong> {booking.id}</div>
            <div><strong>🧑 Khách hàng:</strong> {booking.userDTO?.name || 'N/A'}</div>
            <div><strong>💵 Tổng tiền:</strong> {booking.totalPrice?.toLocaleString() || 'N/A'} đ</div>
            <div><strong>📌 Trạng thái:</strong> {booking.bookingStatus}</div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default BookingList;
