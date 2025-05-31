let stompClient = null;
let bookingList = [];

function connectSocket() {
    const socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        console.log('🔌 WebSocket connected');

        stompClient.subscribe('/topic/bookings/new', (message) => {
            const newBooking = JSON.parse(message.body);
            bookingList.unshift(newBooking);
            renderBookingList();
            alert(`📦 Đơn mới từ ${newBooking.user.name}`);
        });
    });
}

function loadInitialBookings() {
    fetch('http://localhost:8080/api/bookings')
        .then(res => res.json())
        .then(data => {
            bookingList = data;
            renderBookingList();
        });
}

function renderBookingList() {
    const ul = document.getElementById("booking-list");
    ul.innerHTML = '';
    bookingList.forEach(booking => {
        const li = document.createElement("li");
        li.textContent = `${booking.id} - ${booking.user.name} - ${booking.totalPrice} đ`;
        ul.appendChild(li);
    });
}

window.onload = () => {
    loadInitialBookings();
    connectSocket();
};

<ul id="booking-list"></ul>
